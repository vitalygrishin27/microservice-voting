package com.voting.service;

import com.voting.bom.Configuration;

public interface ConfigurationService {
    Configuration getValue(String key, String parameter);

    void save(Configuration configuration);

    void delete(Configuration configuration);
}
