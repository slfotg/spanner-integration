package io.github.slfotg.spanner.extension;

import org.junit.jupiter.api.extension.ExtensionContext;

import com.google.cloud.spanner.DatabaseClient;

public class ExtensionContextUtils {

    // Prevent instantiation
    private ExtensionContextUtils() {
        throw new AssertionError("Cannot instantiate " + getClass());
    }

    private static final String SPANNER_EMULATOR_ENDPOINT = "spanner.emulator.endpoint";

    private static final String HELLO_SERVER_HOST = "hello.server.host";
    private static final String HELLO_SERVER_PORT = "hello.server.port";

    private static final String CONTEXT_SPANNER_DATABASE_CLIENT = "context.spanner.database.client";

    public static void setSpannerEmulatorEndpoint(ExtensionContext context, String endpoint) {
        context.getStore(ExtensionContext.Namespace.GLOBAL).put(SPANNER_EMULATOR_ENDPOINT, endpoint);
    }

    public static String getSpannerEmulatorEndpoint(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.GLOBAL).get(SPANNER_EMULATOR_ENDPOINT, String.class);
    }

    public static void setHelloServerHost(ExtensionContext context, String host) {
        context.getStore(ExtensionContext.Namespace.GLOBAL).put(HELLO_SERVER_HOST, host);
    }

    public static String getHelloServerHost(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.GLOBAL).get(HELLO_SERVER_HOST, String.class);
    }

    public static void setHelloServerPort(ExtensionContext context, int port) {
        context.getStore(ExtensionContext.Namespace.GLOBAL).put(HELLO_SERVER_PORT, port);
    }

    public static int getHelloServerPort(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.GLOBAL).get(HELLO_SERVER_PORT, Integer.class);
    }

    public static void setSpannerDatabaseClient(ExtensionContext context, DatabaseClient client) {
        context.getStore(ExtensionContext.Namespace.GLOBAL).put(CONTEXT_SPANNER_DATABASE_CLIENT, client);
    }

    public static DatabaseClient getSpannerDatabaseClient(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.GLOBAL).get(CONTEXT_SPANNER_DATABASE_CLIENT,
                DatabaseClient.class);
    }
}
