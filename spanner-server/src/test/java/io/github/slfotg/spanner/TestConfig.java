package io.github.slfotg.spanner;

public final class TestConfig {

    // Prevent instantiation
    private TestConfig() {
        throw new AssertionError("Cannot instantiate " + getClass());
    }

    public static final String SPANNER_EMULATOR_ENDPOINT = "spanner.emulator.endpoint";
    public static final String SPANNER_EMULATOR_PROJECT_ID = "spanner.emulator.projectId";
    public static final String SPANNER_EMULATOR_INSTANCE_ID = "spanner.emulator.instanceId";
    public static final String SPANNER_EMULATOR_DATABASE_ID = "spanner.emulator.databaseId";
}
