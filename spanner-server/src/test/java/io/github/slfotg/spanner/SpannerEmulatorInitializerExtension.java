package io.github.slfotg.spanner;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import com.google.cloud.NoCredentials;
import com.google.cloud.spanner.DatabaseAdminClient;
import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.InstanceAdminClient;
import com.google.cloud.spanner.InstanceConfigId;
import com.google.cloud.spanner.InstanceId;
import com.google.cloud.spanner.InstanceInfo;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;

public class SpannerEmulatorInitializerExtension implements BeforeEachCallback, ParameterResolver {

    private Spanner spanner;
    private DatabaseClient client;
    private String endpoint;
    private String projectId;
    private String instanceId;
    private String databaseId;

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(DatabaseClient.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return client;
    }

    private void createInstance(Spanner spanner) throws InterruptedException, ExecutionException {
        InstanceConfigId instanceConfig = InstanceConfigId.of(projectId, "test-instance-config");
        InstanceAdminClient insAdminClient = spanner.getInstanceAdminClient();
        insAdminClient
                .createInstance(
                        InstanceInfo
                                .newBuilder(InstanceId.of(projectId, instanceId))
                                .setNodeCount(1)
                                .setDisplayName("Test instance")
                                .setInstanceConfigId(instanceConfig)
                                .build())
                .get();
    }

    private void createDatabase(Spanner spanner) throws InterruptedException, ExecutionException {
        DatabaseAdminClient dbAdminClient = spanner.getDatabaseAdminClient();
        dbAdminClient
                .createDatabase(
                        instanceId,
                        databaseId,
                        Arrays.asList(
                                "CREATE TABLE Users ("
                                        + "  Uuid      STRING(50),"
                                        + "  FirstName STRING(30),"
                                        + "  LastName  STRING(30),"
                                        + "  Age       INT64"
                                        + ") PRIMARY KEY (Uuid)"))
                .get();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        endpoint = System.getProperty(TestConfig.SPANNER_EMULATOR_ENDPOINT);
        projectId = System.getProperty(TestConfig.SPANNER_EMULATOR_PROJECT_ID);
        instanceId = System.getProperty(TestConfig.SPANNER_EMULATOR_INSTANCE_ID);
        databaseId = System.getProperty(TestConfig.SPANNER_EMULATOR_DATABASE_ID);

        SpannerOptions options = SpannerOptions
                .newBuilder()
                .setEmulatorHost(endpoint)
                .setCredentials(NoCredentials.getInstance())
                .setProjectId(projectId)
                .build();

        spanner = options.getService();
        createInstance(spanner);
        createDatabase(spanner);
        client = spanner.getDatabaseClient(DatabaseId.of(InstanceId.of(projectId, instanceId), databaseId));
        context.getStore(ExtensionContext.Namespace.GLOBAL).put(TestConfig.CONTEXT_SPANNER_CLIENT, client);
    }

}
