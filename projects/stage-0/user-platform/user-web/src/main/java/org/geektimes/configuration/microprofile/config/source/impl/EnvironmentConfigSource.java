package org.geektimes.configuration.microprofile.config.source.impl;

import org.geektimes.configuration.microprofile.config.source.AbstractMapConfigSource;

import java.util.Map;

/**
 * 操作系统环境变量 Source
 */
public class EnvironmentConfigSource extends AbstractMapConfigSource {

    public EnvironmentConfigSource() {
        super("EnvironmentConfigSource", 300);
    }

    @Override
    protected Map loadConfigs() {
        return System.getenv();
    }
}
