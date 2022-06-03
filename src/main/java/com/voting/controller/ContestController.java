package com.voting.controller;

import com.voting.bom.Contest;
import com.voting.bom.Performance;
import com.voting.service.ContestService;
import com.voting.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/contests")
public class ContestController {

    @Autowired
    ContestService service;

    @Autowired
    PerformanceService performanceService;

    @PostMapping
    public ResponseEntity<Long> create(@ModelAttribute Contest contest) {
        return new ResponseEntity<>(service.create(contest), HttpStatus.CREATED);
    }

    @PostMapping("/performances")
    public ResponseEntity updatePerformancesOrder(@RequestBody List<Performance> performances) {
        performanceService.updateAll(performances);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{contestId}/setActivePerformance")
    public ResponseEntity<Contest> setActivePerformanceForContest(@PathVariable Long contestId, @Nullable @RequestBody Performance performance) {
        return new ResponseEntity<>(service.setActivePerformanceForContest(contestId, performance), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Contest>> getAll() {

        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{contestId}/performances")
    public ResponseEntity<List<Performance>> getAllByContest(@PathVariable Long contestId) {
        return new ResponseEntity<>(service.getAllPerformancesForContest(contestId), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Long> update(@ModelAttribute Contest contest) {
        return new ResponseEntity<>(service.update(contest), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@NonNull @PathVariable() Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
