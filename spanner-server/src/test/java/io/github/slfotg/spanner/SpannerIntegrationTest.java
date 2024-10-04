package io.github.slfotg.spanner;

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

import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;

@SpannerEmulatorTest
public class SpannerIntegrationTest {

    private Server server;

    @BeforeEach
    public void createDatabase(DatabaseClient client) throws Exception {
        server = ServerBuilder.forPort(8888)
                .addService(new HelloService(client))
                .build();

        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.shutdown();
    }

    @Test
    public void testSimple(DatabaseClient client) throws ExecutionException, InterruptedException {

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

        ResultSet resultSet = client
                .readOnlyTransaction()
                .executeQuery(Statement.of("select * from Users"));
        resultSet.next();

        // assertThat(resultSet.getString(0), CoreMatchers.equalTo("123"));
        assertThat(resultSet.getString(1), CoreMatchers.equalTo("Sam"));
        assertThat(resultSet.getString(2), CoreMatchers.equalTo("Foster"));
        assertThat(resultSet.getLong(3), CoreMatchers.equalTo(38L));
    }

}