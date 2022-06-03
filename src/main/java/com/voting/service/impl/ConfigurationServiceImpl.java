package com.voting.service.impl;

import com.voting.bom.Configuration;
import com.voting.repository.ConfigurationRepo;
import com.voting.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {
    @Autowired
    ConfigurationRepo repo;

    @Override
    public Configuration getValue(String key, String parameter) {
        return repo.findByKeyAndParameter(key, parameter);
    }

    @Override
    public void save(Configuration configuration) {
        repo.saveAndFlush(configuration);
    }

    @Override
    public void delete(Configuration configuration) {
        repo.delete(configuration);
    }
}
