package com.ams.restapi.sectionInfo;

import com.ams.restapi.timeConfig.TimeConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Controller for managing course metadata
 *
 * @author Harwinder Singh
 * @author Ryan Woo (rtwoo)
 */
@RestController
public class SectionInfoController {
    private final SectionInfoRepository repository;

    public SectionInfoController(SectionInfoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/courseInfo/{courseID}")
    SectionInfoDTO search(@PathVariable Long courseID) {
        SectionInfo course = repository.findById(courseID)
                .orElseThrow(() -> new SectionInfoNotFoundException(courseID));
        return new SectionInfoDTO(course);
    }

    @PutMapping("/courseInfo/{courseID}")
    SectionInfoDTO update(@PathVariable Long courseID,
                          @RequestBody SectionInfoDTO newInfo) {
        return new SectionInfoDTO(repository.save(newInfo.toEntity(courseID)));
    }

    @DeleteMapping("/courseInfo/{courseID}")
    ResponseEntity<String> delete(@PathVariable Long courseID) {
        if (!repository.existsById(courseID)) throw new SectionInfoNotFoundException(courseID);

        repository.deleteById(courseID);
        return ResponseEntity.ok("Deleted course info log " + courseID);
    }

    @GetMapping("/courseInfo/test")
    TimeConfig resolveTest(@RequestParam("room") String room,
            @RequestParam("date") LocalDate date,
            @RequestParam("time") LocalTime time) {
        return repository.resolve(room, date.getDayOfWeek(), time)
            .orElseThrow(() -> new RuntimeException("oogao"));
    }
}
