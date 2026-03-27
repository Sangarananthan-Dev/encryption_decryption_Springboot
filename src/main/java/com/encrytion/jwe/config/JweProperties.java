package com.encrytion.jwe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwe")
public record JweProperties(String keyId) {
}
