package io.github.slfotg.spanner.extension;

import java.net.ServerSocket;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

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

        ExtensionContextUtils.setHelloServerHost(context, host);
        ExtensionContextUtils.setHelloServerPort(context, port);

        var client = ExtensionContextUtils.getSpannerDatabaseClient(context);

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
