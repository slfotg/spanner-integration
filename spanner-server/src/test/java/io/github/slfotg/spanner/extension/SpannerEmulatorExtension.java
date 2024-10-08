package io.github.slfotg.spanner.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.SpannerEmulatorContainer;
import org.testcontainers.utility.DockerImageName;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class SpannerEmulatorExtension implements BeforeEachCallback, AfterEachCallback {

    private SpannerEmulatorContainer emulator;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.OFF);
        emulator = new SpannerEmulatorContainer(
                DockerImageName.parse("gcr.io/cloud-spanner-emulator/emulator:1.5.6"));
        emulator.start();
        ExtensionContextUtils.setSpannerEmulatorEndpoint(context, emulator.getEmulatorGrpcEndpoint());
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        emulator.stop();
    }

}
