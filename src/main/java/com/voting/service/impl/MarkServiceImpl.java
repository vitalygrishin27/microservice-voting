package com.voting.service.impl;

import com.voting.bom.Jury;
import com.voting.bom.Mark;
import com.voting.bom.Performance;
import com.voting.exception.MarkException;
import com.voting.repository.MarkRepo;
import com.voting.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarkServiceImpl implements MarkService {
    @Autowired
    MarkRepo repo;

    @Override
    public Long create(Mark mark) {
        try {
            repo.saveAndFlush(mark);
        } catch (Exception e) {
            throw new MarkException(e.getMessage());
        }
        return mark.getId();
    }

    @Override
    public void deleteAllByPerformanceAndJury(Performance performance, Jury jury) {
        repo.deleteByPerformanceAndJury(performance, jury);
    }
}
