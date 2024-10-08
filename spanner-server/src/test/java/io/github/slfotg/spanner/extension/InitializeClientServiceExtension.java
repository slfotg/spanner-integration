package io.github.slfotg.spanner.extension;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import io.github.slfotg.spanner.api.HelloService;
import io.github.slfotg.spanner.client.ClientHelloService;

public class InitializeClientServiceExtension implements BeforeEachCallback, ParameterResolver {

    HelloService service;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        var port = ExtensionContextUtils.getHelloServerPort(context);
        var host = ExtensionContextUtils.getHelloServerHost(context);
        service = new ClientHelloService(host, port);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(HelloService.class);
    }

    @Override
    public HelloService resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return service;
    }

}
