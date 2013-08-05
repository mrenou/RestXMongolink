package restx.mongolink;

import com.google.common.base.Charsets;
import restx.SignatureKey;
import restx.factory.Module;
import restx.factory.Provides;
import javax.inject.Named;

@Module
public class MongolinkModule {
    @Provides
    @Named("restx.admin.password")
    public String restxAdminPassword() {
        return "5222";
    }

    @Provides
    public SignatureKey signatureKey() {
         return new SignatureKey("4172252699184060280 restx-mongolink 45a8a793-b6c7-484c-87fc-898284887837 restx-mongolink".getBytes(Charsets.UTF_8));
    }
}
