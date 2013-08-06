package restx.mongolink;

import java.net.UnknownHostException;

import org.mongolink.MongoSession;
import org.mongolink.MongoSessionManager;
import org.mongolink.Settings;
import org.mongolink.domain.UpdateStrategies;
import org.mongolink.domain.mapper.ContextBuilder;

import restx.factory.Component;

import com.google.common.collect.Lists;
import com.mongodb.ServerAddress;

import javax.inject.Named;


public class MongoSessionManagerProvider {

	private MongoSessionManager manager;
	private MongoSession session;
	
	public MongoSessionManagerProvider(
		@Named(MongolinkFactory.CONTEXT_NAME) String ctx,
		@Named(MongolinkFactory.MONGO_DB_NAME) String db,
		@Named(MongolinkFactory.MONGO_HOST) String host,
		@Named(MongolinkFactory.MONGO_PORT) String port,
		@Named(MongolinkFactory.MONGO_USER) String user,
		@Named(MongolinkFactory.MONGO_PASSWORD) String password
		) throws NumberFormatException, UnknownHostException{
		
		ContextBuilder builder = new ContextBuilder(ctx);
		
		Settings settings = Settings.defaultInstance()
		                    .withDefaultUpdateStrategy(UpdateStrategies.DIFF)
		                    .withDbName(db)
		                    .withAddresses(Lists.newArrayList(new ServerAddress(host, Integer.parseInt(port))));
		
		manager = MongoSessionManager.create(builder, settings);
		session = manager.createSession();
	}
	
	public MongoSession getSession(){
		return session;
	}
	
}
