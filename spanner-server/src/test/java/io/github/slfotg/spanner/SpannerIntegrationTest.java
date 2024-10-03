package io.github.slfotg.spanner;

import com.google.cloud.NoCredentials;
import com.google.cloud.spanner.*;

import io.github.slfotg.spanner.domain.HelloRequest;
import io.github.slfotg.spanner.grpc.HelloReply;
import io.github.slfotg.spanner.grpc.HelloServiceGrpc;
import io.github.slfotg.spanner.mapping.RequestMapper;
import io.github.slfotg.spanner.server.HelloService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.SpannerEmulatorContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SpannerIntegrationTest {

    private static final String PROJECT_ID = "test-project";
    private static final String INSTANCE_ID = "test-instance";
    private static final String DATABASE_ID = "test-database";

    private Spanner spanner;
    private DatabaseClient dbClient;
    private Server server;

    @Container
    public SpannerEmulatorContainer emulator = new SpannerEmulatorContainer(
            DockerImageName.parse("gcr.io/cloud-spanner-emulator/emulator:1.5.6"));

    @BeforeEach
    public void createDatabase() throws Exception {
        SpannerOptions options = SpannerOptions
                .newBuilder()
                .setEmulatorHost(emulator.getEmulatorGrpcEndpoint())
                .setCredentials(NoCredentials.getInstance())
                .setProjectId(PROJECT_ID)
                .build();

        spanner = options.getService();

        InstanceId instanceId = createInstance(spanner);

        createDatabase(spanner);

        DatabaseId databaseId = DatabaseId.of(instanceId, DATABASE_ID);
        dbClient = spanner.getDatabaseClient(databaseId);
        server = ServerBuilder.forPort(8888)
                .addService(new HelloService(dbClient))
                .build();

        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.shutdown();
    }

    private InstanceId createInstance(Spanner spanner) throws InterruptedException, ExecutionException {
        InstanceConfigId instanceConfig = InstanceConfigId.of(PROJECT_ID, "test-instance-config");
        InstanceId instanceId = InstanceId.of(PROJECT_ID, INSTANCE_ID);
        InstanceAdminClient insAdminClient = spanner.getInstanceAdminClient();
        insAdminClient
                .createInstance(
                        InstanceInfo
                                .newBuilder(instanceId)
                                .setNodeCount(1)
                                .setDisplayName("Test instance")
                                .setInstanceConfigId(instanceConfig)
                                .build())
                .get();
        return instanceId;
    }

    private void createDatabase(Spanner spanner) throws InterruptedException, ExecutionException {
        DatabaseAdminClient dbAdminClient = spanner.getDatabaseAdminClient();
        dbAdminClient
                .createDatabase(
                        INSTANCE_ID,
                        DATABASE_ID,
                        Arrays.asList(
                                "CREATE TABLE Users (Uuid STRING(50), FirstName STRING(30), LastName STRING(30), Age INT64) PRIMARY KEY (Uuid)"))
                .get();
    }

    @Test
    public void testSimple() throws ExecutionException, InterruptedException {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8888)
                .usePlaintext()
                .build();

        HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);

        var request = new HelloRequest("Sam", "Foster", 38);
        var mapper = RequestMapper.INSTANCE;

        var hello = mapper.toHelloRequest(request);

        HelloReply helloResponse = stub.sayHello(hello);

        System.out.println(helloResponse.getMessage());

        channel.shutdown();

        ResultSet resultSet = dbClient
                .readOnlyTransaction()
                .executeQuery(Statement.of("select * from Users"));
        resultSet.next();

        // assertThat(resultSet.getString(0), CoreMatchers.equalTo("123"));
        assertThat(resultSet.getString(1), CoreMatchers.equalTo("Sam"));
        assertThat(resultSet.getString(2), CoreMatchers.equalTo("Foster"));
        assertThat(resultSet.getLong(3), CoreMatchers.equalTo(38L));
    }

}