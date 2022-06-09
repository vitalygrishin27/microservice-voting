package com.voting.service;

import com.voting.bom.Jury;
import com.voting.bom.Mark;
import com.voting.bom.Performance;

public interface MarkService {
    Long create(Mark mark);

    void deleteAllByPerformanceAndJury(Performance performance, Jury jury);
}
