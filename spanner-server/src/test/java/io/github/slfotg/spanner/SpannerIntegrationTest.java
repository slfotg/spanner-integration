package io.github.slfotg.spanner;

import com.google.cloud.spanner.*;

import io.github.slfotg.spanner.client.ClientHelloService;
import io.github.slfotg.spanner.domain.HelloRequest;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;

@SpannerEmulatorTest
public class SpannerIntegrationTest {

    private String host;
    private int port;

    @BeforeEach
    public void createDatabase() throws Exception {
        host = System.getProperty(TestConfig.HELLO_SERVER_HOST);
        port = Integer.parseInt(System.getProperty(TestConfig.HELLO_SERVER_PORT));
    }

    @Test
    public void testSimple(DatabaseClient client) throws ExecutionException, InterruptedException {

        ClientHelloService service = new ClientHelloService(host, port);
        var request = new HelloRequest("Sam", "Foster", 38);
        var reply = service.sayHello(request);

        System.out.println(reply.message());

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