package com.voting.controller;

import com.voting.bom.Criteria;
import com.voting.service.CriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/criteria")
@CrossOrigin
public class CriteriaController {

    @Autowired
    CriteriaService criteriaService;

    @PostMapping
    public ResponseEntity<Long> create(@NonNull @RequestBody Criteria criteria) {
        return new ResponseEntity<>(criteriaService.create(criteria), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Criteria>> getAll() {
        return new ResponseEntity<>(criteriaService.getAll(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Long> update(@NonNull @RequestBody Criteria criteria) {
        return new ResponseEntity<>(criteriaService.update(criteria), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@NonNull @PathVariable() Long id) {
        criteriaService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
