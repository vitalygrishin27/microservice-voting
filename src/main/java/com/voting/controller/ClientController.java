package com.voting.controller;

import com.voting.bom.Contest;
import com.voting.bom.Jury;
import com.voting.bom.Performance;
import com.voting.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
@CrossOrigin
public class ClientController {

    @Autowired
    ClientService clientService;

    @PostMapping("/loginIn")
    public ResponseEntity<String> loginIn(@NonNull @RequestBody Jury jury) {
        return new ResponseEntity<>(clientService.loginIn(jury), HttpStatus.OK);
    }

    @PostMapping("/contests")
    public ResponseEntity<List<Contest>> getAvailableContests(@NonNull @RequestBody Jury jury) {
        return new ResponseEntity<>(clientService.getAvailableContests(jury.getToken()), HttpStatus.OK);
    }

    @PostMapping("/marks")
    public ResponseEntity<Performance> createMarks(@NonNull @RequestBody Performance performance) {
        return new ResponseEntity<>(clientService.createMarks(performance), HttpStatus.OK);
    }

    @GetMapping("/contest/{contestId}/performance/{id}/{token}")
    public ResponseEntity<Performance> getActivePerformanceForContest(@NonNull @PathVariable Long contestId,
                                                                      @PathVariable Long id,
                                                                      @PathVariable String token) {
        return new ResponseEntity<>(clientService.getActivePerformanceIfChanged(contestId, id, token), HttpStatus.OK);
    }

}
