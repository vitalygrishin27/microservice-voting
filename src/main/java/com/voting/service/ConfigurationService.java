package com.voting.service;

import com.voting.bom.Configuration;

public interface ConfigurationService {
    Configuration getConfiguration(String key, String parameter);

    void save(Configuration configuration);

    void delete(Configuration configuration);
}
