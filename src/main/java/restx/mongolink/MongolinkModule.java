package restx.mongolink;

import javax.inject.Named;

import restx.factory.Module;
import restx.factory.Provides;

@Module
public class MongolinkModule {
	
	@Provides
	@Named(MongolinkFactory.MONGO_DB_NAME)
	public String db(){
		return "kowork";
	}
}
