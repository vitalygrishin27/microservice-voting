package com.voting.controller;

import com.voting.bom.Member;
import com.voting.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    MemberService service;

    @PostMapping
    public ResponseEntity<Long> create(@ModelAttribute Member member, @RequestParam(value = "performancesData", required = false) String performancesData) {
        return new ResponseEntity<>(service.create(member, performancesData), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Long> update(@ModelAttribute Member member, @RequestParam(value = "performancesData", required = false ) String performancesData ) {
        return new ResponseEntity<>(service.update(member, performancesData), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@NonNull @PathVariable() Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
