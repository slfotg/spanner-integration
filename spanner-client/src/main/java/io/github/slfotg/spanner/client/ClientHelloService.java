package io.github.slfotg.spanner.client;

import io.github.slfotg.spanner.api.HelloService;
import io.github.slfotg.spanner.domain.HelloReply;
import io.github.slfotg.spanner.domain.HelloRequest;
import io.github.slfotg.spanner.grpc.HelloServiceGrpc;
import io.github.slfotg.spanner.mapper.DomainMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ClientHelloService implements HelloService {

    private final DomainMapper mapper = DomainMapper.INSTANCE;

    private final String host;
    private final int port;

    public ClientHelloService(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public HelloReply sayHello(HelloRequest request) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        var stub = HelloServiceGrpc.newBlockingStub(channel);
        var hello = mapper.domainToGrpcHelloRequest(request);
        var reply = mapper.grpcToDomainHelloReply(stub.sayHello(hello));
        channel.shutdown();
        return reply;
    }

}
