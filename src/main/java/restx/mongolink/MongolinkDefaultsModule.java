package restx.mongolink;

import javax.inject.Named;
import restx.factory.Module;
import restx.factory.Provides;

@Module(priority=1000)
public class MongolinkDefaultsModule {
	
	@Provides @Named("mongo.db")
    public String db() { return "default"; }
	@Provides @Named("mongo.host")
    public String host() { return "localhost"; }
    @Provides @Named("mongo.port")
    public String port() { return "27017"; }
    @Provides @Named("mongo.user")
    public String user() { return ""; }
    @Provides @Named("mongo.password")
    public String password() { return ""; }
    @Provides @Named("domain.context")
    public String context() { return "domain"; }
}
