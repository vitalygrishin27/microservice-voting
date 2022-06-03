package com.voting.repository;

import com.voting.bom.Configuration;
import com.voting.bom.Contest;
import com.voting.bom.Member;
import com.voting.bom.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ConfigurationRepo extends JpaRepository<Configuration, Long> {
    @Query("select c from Configuration c where c.key = :key and c.parameter = :parameter")
    Configuration findByKeyAndParameter(String key, String parameter);
}


