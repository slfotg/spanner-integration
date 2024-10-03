package io.github.slfotg.spanner;

import io.github.slfotg.spanner.grpc.HelloReply;
import io.github.slfotg.spanner.domain.HelloRequest;
import io.github.slfotg.spanner.grpc.HelloServiceGrpc;
import io.github.slfotg.spanner.mapping.RequestMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Main {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8888)
                .usePlaintext()
                .build();

        HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);

        var request = new HelloRequest("Nich", "Schuetze", 30);
        var mapper = RequestMapper.INSTANCE;

        var hello = mapper.toHelloRequest(request);

        HelloReply helloResponse = stub.sayHello(hello);

        System.out.println(helloResponse.getMessage());

        channel.shutdown();
    }
}