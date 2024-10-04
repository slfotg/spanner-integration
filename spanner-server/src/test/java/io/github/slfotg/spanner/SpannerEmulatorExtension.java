package io.github.slfotg.spanner;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.SpannerEmulatorContainer;
import org.testcontainers.utility.DockerImageName;

public class SpannerEmulatorExtension implements BeforeEachCallback, AfterEachCallback {

    private static final String PROJECT_ID = "test-project";
    private static final String INSTANCE_ID = "test-instance";
    private static final String DATABASE_ID = "test-database";

    private SpannerEmulatorContainer emulator;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        emulator = new SpannerEmulatorContainer(
                DockerImageName.parse("gcr.io/cloud-spanner-emulator/emulator:1.5.6"));
        emulator.start();
        System.setProperty(TestConfig.SPANNER_EMULATOR_ENDPOINT, emulator.getEmulatorGrpcEndpoint());
        System.setProperty(TestConfig.SPANNER_EMULATOR_PROJECT_ID, PROJECT_ID);
        System.setProperty(TestConfig.SPANNER_EMULATOR_INSTANCE_ID, INSTANCE_ID);
        System.setProperty(TestConfig.SPANNER_EMULATOR_DATABASE_ID, DATABASE_ID);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        emulator.stop();
    }

}
