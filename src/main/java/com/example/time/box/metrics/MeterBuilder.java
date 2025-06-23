package com.example.time.box.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration class is no longer needed as Spring Boot Actuator automatically provides a MeterRegistry bean.
 * It's kept for documentation purposes.
 */
@Configuration
public class MeterBuilder {
    // The MeterRegistry bean is automatically provided by Spring Boot Actuator
}
