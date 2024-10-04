package io.github.slfotg.spanner;

import java.net.ServerSocket;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.google.cloud.spanner.DatabaseClient;

import io.github.slfotg.spanner.server.HelloGrpcService;
import io.github.slfotg.spanner.server.ServerHelloService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class SpannerHelloServerExtension implements BeforeEachCallback, AfterEachCallback {

    private String host = "localhost";
    private int port;
    private Server server;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        var socket = new ServerSocket(0);
        port = socket.getLocalPort();
        socket.close();

        System.setProperty(TestConfig.HELLO_SERVER_HOST, host);
        System.setProperty(TestConfig.HELLO_SERVER_PORT, String.valueOf(port));

        var client = (DatabaseClient) context.getStore(ExtensionContext.Namespace.GLOBAL)
                .get(TestConfig.CONTEXT_SPANNER_CLIENT);

        var service = new ServerHelloService(client);

        server = ServerBuilder.forPort(port)
                .addService(new HelloGrpcService(service))
                .build();

        server.start();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        server.shutdown();
    }

}
