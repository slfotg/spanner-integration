package io.github.slfotg.spanner;

import com.google.cloud.spanner.*;

import io.github.slfotg.spanner.annotation.SpannerEmulatorTest;
import io.github.slfotg.spanner.api.HelloService;
import io.github.slfotg.spanner.domain.HelloRequest;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;

@SpannerEmulatorTest
public class SpannerIntegrationTest {

    @Test
    public void testSam(HelloService clientService, DatabaseClient client)
            throws ExecutionException, InterruptedException {

        var request = new HelloRequest("Sam", "Foster", 38);
        var reply = clientService.sayHello(request);

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

    @Test
    public void testNich(HelloService clientService, DatabaseClient client)
            throws ExecutionException, InterruptedException {

        var request = new HelloRequest("Nich", "Schuetze", 30);
        var reply = clientService.sayHello(request);

        System.out.println(reply.message());

        ResultSet resultSet = client
                .readOnlyTransaction()
                .executeQuery(Statement.of("select * from Users"));
        resultSet.next();

        // assertThat(resultSet.getString(0), CoreMatchers.equalTo("123"));
        assertThat(resultSet.getString(1), CoreMatchers.equalTo("Nich"));
        assertThat(resultSet.getString(2), CoreMatchers.equalTo("Schuetze"));
        assertThat(resultSet.getLong(3), CoreMatchers.equalTo(30L));
    }

}