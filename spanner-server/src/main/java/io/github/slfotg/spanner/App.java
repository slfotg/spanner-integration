package io.github.slfotg.spanner;

import com.google.cloud.NoCredentials;
import com.google.cloud.spanner.*;

import io.github.slfotg.spanner.server.HelloService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class App {

    private static final String PROJECT_ID = "test-project";
    private static final String INSTANCE_ID = "test-instance";
    private static final String DATABASE_ID = "test-database";

    public static void main(String[] args) throws Exception {
        SpannerOptions options = SpannerOptions
                .newBuilder()
                .setEmulatorHost("db:9010")
                .setCredentials(NoCredentials.getInstance())
                .setProjectId(PROJECT_ID)
                .build();

        Spanner spanner = options.getService();
        InstanceId instanceId = InstanceId.of(PROJECT_ID, INSTANCE_ID);
        DatabaseId databaseId = DatabaseId.of(instanceId, DATABASE_ID);
        DatabaseClient client = spanner.getDatabaseClient(databaseId);

        Server server = ServerBuilder.forPort(8888)
                .addService(new HelloService(client))
                .build();

        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started on port " + server.getPort());
        server.awaitTermination();
    }

}