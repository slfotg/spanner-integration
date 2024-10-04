package io.github.slfotg.spanner.server;

import io.github.slfotg.spanner.api.HelloService;
import io.github.slfotg.spanner.grpc.HelloReply;
import io.github.slfotg.spanner.grpc.HelloRequest;
import io.github.slfotg.spanner.grpc.HelloServiceGrpc.HelloServiceImplBase;
import io.github.slfotg.spanner.mapper.DomainMapper;
import io.grpc.stub.StreamObserver;

public class HelloGrpcService extends HelloServiceImplBase {

    private final DomainMapper mapper = DomainMapper.INSTANCE;
    private final HelloService service;

    public HelloGrpcService(HelloService service) {
        this.service = service;
    }

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        mapper.grpcToDomainHelloRequest(request);
        responseObserver
                .onNext(mapper.domainToGrpcHelloReply(service.sayHello(mapper.grpcToDomainHelloRequest(request))));
        responseObserver.onCompleted();
    }

}
