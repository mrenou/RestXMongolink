package restx.mongolink;

import org.mongolink.MongoSession;

import restx.annotations.GET;
import restx.annotations.RestxResource;
import restx.factory.Component;

@RestxResource
@Component
public class Dummy {

	private final MongoSession session;
	
	public Dummy(MongoSession session){
		this.session = session;
	}
	
	@GET("/default")
	public String def(){
		return "";
	}
}
