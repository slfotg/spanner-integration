package io.github.slfotg.spanner.api;

import io.github.slfotg.spanner.domain.HelloReply;
import io.github.slfotg.spanner.domain.HelloRequest;

public interface HelloService {

    HelloReply sayHello(HelloRequest request);
}