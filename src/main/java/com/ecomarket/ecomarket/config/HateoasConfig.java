package com.ecomarket.ecomarket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;

/**
 * Configuration class to enable HATEOAS hypermedia support
 */
@Configuration
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class HateoasConfig {
    // This configuration ensures HAL (Hypertext Application Language) format is
    // enabled
    // which is the default format for Spring HATEOAS
}
