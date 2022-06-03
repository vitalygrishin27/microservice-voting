package com.voting.controller;

import com.voting.bom.Jury;
import com.voting.service.JuryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/juries")
public class JuryController {

    @Autowired
    JuryService service;

    @PostMapping
    public ResponseEntity<Long> create(@ModelAttribute Jury jury) {
        return new ResponseEntity<>(service.create(jury), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Jury>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Long> update(@ModelAttribute Jury jury) {
        return new ResponseEntity<>(service.update(jury), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@NonNull @PathVariable() Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
