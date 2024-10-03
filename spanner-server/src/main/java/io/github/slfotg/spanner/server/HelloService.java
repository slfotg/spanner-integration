package io.github.slfotg.spanner.server;

import java.util.UUID;

import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.Statement;

import io.github.slfotg.spanner.grpc.HelloReply;
import io.github.slfotg.spanner.grpc.HelloRequest;
import io.github.slfotg.spanner.grpc.HelloServiceGrpc.HelloServiceImplBase;
import io.grpc.stub.StreamObserver;

public class HelloService extends HelloServiceImplBase {

    private final DatabaseClient client;

    public HelloService(DatabaseClient client) {
        this.client = client;
    }

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        var uuid = UUID.randomUUID().toString();
        var firstName = request.getName().getFirst();
        var lastName = request.getName().getLast();
        var age = request.getAge();

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

        HelloReply response = HelloReply.newBuilder().setMessage("Record inserted with UUID: " + uuid).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
