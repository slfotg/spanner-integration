package io.github.slfotg.spanner.server;

import java.util.UUID;

import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.Statement;

import io.github.slfotg.spanner.api.HelloService;
import io.github.slfotg.spanner.domain.HelloReply;
import io.github.slfotg.spanner.domain.HelloRequest;

public class ServerHelloService implements HelloService {

    private final DatabaseClient client;

    public ServerHelloService(DatabaseClient client) {
        this.client = client;
    }

    @Override
    public HelloReply sayHello(HelloRequest request) {
        var uuid = UUID.randomUUID().toString();
        var firstName = request.first();
        var lastName = request.last();
        var age = request.age();

        // Insert the record into the Spanner table
        client.readWriteTransaction().run(transaction -> {
            String sql = "INSERT INTO Users (Uuid, FirstName, LastName, Age) VALUES (@uuid, @firstName, @lastName, @age)";
            Statement statement = Statement.newBuilder(sql)
                    .bind("uuid").to(uuid)
                    .bind("firstName").to(firstName)
                    .bind("lastName").to(lastName)
                    .bind("age").to(age)
                    .build();
            transaction.executeUpdate(statement);
            return null;
        });

        return new HelloReply("Record inserted with UUID: " + uuid);
    }

}
