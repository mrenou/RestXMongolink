package restx.mongolink;

import java.net.UnknownHostException;

import org.mongolink.MongoSession;
import org.mongolink.MongoSessionManager;
import org.mongolink.Settings;
import org.mongolink.domain.UpdateStrategies;
import org.mongolink.domain.mapper.ContextBuilder;
import com.google.common.collect.Lists;
import com.mongodb.ServerAddress;
import restx.factory.BillOfMaterials;
import restx.factory.BoundlessComponentBox;
import restx.factory.ComponentBox;
import restx.factory.Factory;
import restx.factory.Machine;
import restx.factory.MachineEngine;
import restx.factory.Name;
import restx.factory.NamedComponent;
import restx.factory.SatisfiedBOM;
import restx.factory.SingleNameFactoryMachine;

@SuppressWarnings("unchecked")
@Machine
public class MongolinkFactory extends SingleNameFactoryMachine<MongoSession>{

	//String vars to keep the code clean
	public static final String MONGO_DB_NAME        = "mongo.db";
	public static final String MONGO_HOST           = "mongo.host";
	public static final String MONGO_PORT           = "mongo.port";
	public static final String MONGO_USER           = "mongo.user";
	public static final String MONGO_PASSWORD       = "mongo.host";
	
	//The CONTEXT_NAME specifies the "domain" package location
	public static final String CONTEXT_NAME         = "domain.context";
	
	//Method which is called to construct the MongoSessionManager
    public static final String MANAGER_BUILDER_NAME = "createManager";
    
    //Retrieves the defined @Named in modules
    public static final Name<String> DB         = Name.of(String.class, MONGO_DB_NAME);
    public static final Name<String> HOST       = Name.of(String.class, MONGO_HOST);
    public static final Name<String> PORT       = Name.of(String.class, MONGO_PORT);
    public static final Name<String> USER       = Name.of(String.class, MONGO_USER);
    public static final Name<String> PASSWORD   = Name.of(String.class, MONGO_PASSWORD);
    public static final Name<MongoSession> NAME = Name.of(MongoSession.class, "MongoSession");
    public static final Name<String> CONTEXT    = Name.of(String.class, CONTEXT_NAME);
    
	public MongolinkFactory() {
		
		super(0, new MachineEngine<MongoSession>(){
			
			//Retrieves the current defined @Named in module
			private Factory.Query<String> dbNameQuery = Factory.Query.byName(DB);
			private Factory.Query<String> dbHostQuery = Factory.Query.byName(HOST);
			private Factory.Query<String> dbPortQuery = Factory.Query.byName(PORT);
			private Factory.Query<String> dbUserQuery = Factory.Query.byName(USER);
			private Factory.Query<String> dbPassQuery = Factory.Query.byName(PASSWORD);
			private Factory.Query<String> context     = Factory.Query.byName(CONTEXT);
			
			@Override
			public BillOfMaterials getBillOfMaterial() {
				return BillOfMaterials.of(dbNameQuery,dbHostQuery,dbPortQuery,dbUserQuery,dbPassQuery,context);
			}

			@Override
			public Name<MongoSession> getName() {
				return NAME;
			}

			@Override
			public ComponentBox<MongoSession> newComponent(SatisfiedBOM satisfiedBOM) {				

				try{
					NamedComponent<MongoSession> nc = new NamedComponent<MongoSession>(NAME,doNewComponent(satisfiedBOM));
					return new BoundlessComponentBox<MongoSession>(nc){
						@Override
						public void close() {
							pick().get().getComponent().stop();
						}
					};
				}catch(UnknownHostException e){
					return null;
				}
			}
			
			public MongoSession doNewComponent(SatisfiedBOM sb) throws UnknownHostException{
												
				ContextBuilder builder = new ContextBuilder(sb.getOne(context).get().getComponent());
				
				Settings settings = Settings.defaultInstance()
				                    .withDefaultUpdateStrategy(UpdateStrategies.DIFF)
				                    .withDbName(sb.getOne(dbNameQuery).get().getComponent())
				                    .withAddresses(Lists.newArrayList(new ServerAddress(sb.getOne(dbHostQuery).get().getComponent(), Integer.parseInt(sb.getOne(dbPortQuery).get().getComponent()))))
									.withAuthentication(sb.getOne(dbUserQuery).get().getComponent(), sb.getOne(dbPassQuery).get().getComponent());
				
				MongoSessionManager mongoSessionManager = MongoSessionManager.create(builder, settings);
				MongoSession session = mongoSessionManager.createSession();
				session.start();
				return session;

			}

			@Override
			public String toString() {
				return "MongolinkFactoryMachineEngine";
			}
			
		});
		
	}
}
