package com.ams.restapi.autograder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autograder")
public class AutograderController {

    private final AutograderService autograderService;
    
    public AutograderController(AutograderService autograderService) {
        this.autograderService = autograderService;
    }

    @PostMapping("/{sectionId}/{date}")
    public ResponseEntity<String> startAutograder(@PathVariable String sectionId, @PathVariable String date) {
        String key = sectionId + "-" + date;
        if (!autograderService.taskRunning(key)) {
            autograderService.executeTaskAsync(sectionId, date);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                String.format("Autograder for section %s on %s started.", sectionId, date));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                String.format("Autograder for section %s on %s is already running.", sectionId, date));
        }
    }

    @GetMapping("/{sectionId}/{date}")
    public ResponseEntity<String> checkAutograderStatus(@PathVariable String sectionId, @PathVariable String date) {
        String key = sectionId + "-" + date;
        if (autograderService.taskExists(key)) {
            if (autograderService.taskRunning(key)) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    String.format("Autograder for section %s on %s is still running.", sectionId, date));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(
                    String.format("Autograder for section %s on %s has finished.", sectionId, date));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                String.format("Autograder for section %s on %s not found.", sectionId, date));
        }
    }
}