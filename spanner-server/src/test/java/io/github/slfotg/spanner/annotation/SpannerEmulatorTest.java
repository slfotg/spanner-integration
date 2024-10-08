package io.github.slfotg.spanner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import io.github.slfotg.spanner.extension.InitializeClientServiceExtension;
import io.github.slfotg.spanner.extension.SpannerEmulatorExtension;
import io.github.slfotg.spanner.extension.SpannerEmulatorInitializerExtension;
import io.github.slfotg.spanner.extension.SpannerHelloServerExtension;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({
        SpannerEmulatorExtension.class,
        SpannerEmulatorInitializerExtension.class,
        SpannerHelloServerExtension.class,
        InitializeClientServiceExtension.class
})
public @interface SpannerEmulatorTest {
}
