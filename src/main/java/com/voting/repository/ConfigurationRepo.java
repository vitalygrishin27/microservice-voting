package com.voting.repository;

import com.voting.bom.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConfigurationRepo extends JpaRepository<Configuration, Long> {
    @Query("select c from Configuration c where c.key = :key and c.parameter = :parameter")
    Configuration findByKeyAndParameter(String key, String parameter);
}


