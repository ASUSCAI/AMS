package com.ams.restapi.timeConfig;

import com.ams.restapi.sectionInfo.SectionInfo;
import com.ams.restapi.sectionInfo.SectionInfoRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Course specific time configuration endpoints
 * @author Gabriel Esparza Uriarte
 * @author Ryan Woo (rtwoo)
 */
@RestController
class TimeConfigController {

    private final SectionInfoRepository courseInfoRepo;
    private final DateSpecificTimeRepository dateSpecificTimeRepo;

    TimeConfigController(SectionInfoRepository courseInfoRepo,
                         DateSpecificTimeRepository dateSpecificTimeRepo) {
        this.courseInfoRepo = courseInfoRepo;
        this.dateSpecificTimeRepo = dateSpecificTimeRepo;
    }

    @GetMapping("/timeConfig/{courseID}")
    TimeConfigDTO search(@PathVariable Long courseID) {
        SectionInfo info = courseInfoRepo.findById(courseID)
                .orElseThrow(() ->
                        new TimeConfigNotFoundException(courseID));
        return new TimeConfigDTO(info.getDefaultTimeConfig());
    }

    @PutMapping("/timeConfig/{courseID}")
    TimeConfigDTO update(@PathVariable Long courseID,
            @Valid @RequestBody TimeConfigDTO timeConfig) {
        SectionInfo info = courseInfoRepo.findById(courseID).orElseThrow(() -> new TimeConfigNotFoundException(courseID));
        TimeConfig config = timeConfig.toEntity(info);
        config.setId(info.getDefaultTimeConfig().getId());
        info.setDefaultTimeConfig(config);
        return new TimeConfigDTO(courseInfoRepo.save(info).getDefaultTimeConfig());
    }

    @DeleteMapping("/timeConfig/{courseID}")
    TimeConfigDTO delete(@PathVariable Long courseID) {
        SectionInfo info = courseInfoRepo.findById(courseID).orElseThrow(() -> new TimeConfigNotFoundException(courseID));
        info.setDefaultTimeConfig(SectionInfo.getDefaultTimeConfig(info, info.getStartTime(), info.getEndTime()));
        return new TimeConfigDTO(courseInfoRepo.save(info).getDefaultTimeConfig());
    }

    @GetMapping("/timeConfig/{courseID}/{date}")
    TimeConfigDTO getTimeConfig(@PathVariable Long courseID, @PathVariable LocalDate date) {
        SectionInfo course = courseInfoRepo.findById(courseID)
                .orElseThrow(() -> new TimeConfigNotFoundException(courseID));
        return new TimeConfigDTO(dateSpecificTimeRepo.findByCourseAndDate(course, date)
                .orElseThrow(() -> new DateSpecificTimeConfigNotFoundException(courseID, date)).getConfig());
    }

    @PutMapping("/timeConfig/{courseID}/{date}")
    TimeConfigDTO newTimeConfigAndDate(@PathVariable Long courseID, @PathVariable LocalDate date,
            @RequestBody TimeConfigDTO timeConfig) {
        SectionInfo course = courseInfoRepo.findById(courseID)
                .orElseThrow(() -> new TimeConfigNotFoundException(courseID));

        Optional<DateSpecificTimeConfig> config = dateSpecificTimeRepo.findByCourseAndDate(course, date);

        if (config.isPresent()) {
            config.get().setConfig(timeConfig.toEntity(course));
            return new TimeConfigDTO(dateSpecificTimeRepo.save(config.get()).getConfig());
        }

        return new TimeConfigDTO(dateSpecificTimeRepo.save(
                new DateSpecificTimeConfig(course, date, timeConfig.toEntity(course))).getConfig());

    }


    @DeleteMapping("/timeConfig/{courseID}/{date}")
    void deleteTimeConfig(@PathVariable Long courseID, @PathVariable LocalDate date) {
        SectionInfo course = courseInfoRepo.findById(courseID)
                .orElseThrow(() -> new TimeConfigNotFoundException(courseID));
        DateSpecificTimeConfig config = dateSpecificTimeRepo.findByCourseAndDate(course, date)
                .orElseThrow(() -> new DateSpecificTimeConfigNotFoundException(courseID, date));
        dateSpecificTimeRepo.delete(config);
    }

    @GetMapping("/timeConfig/test")
    TimeConfig resolveTest(@RequestParam("room") String room,
            @RequestParam("date") LocalDate date,
            @RequestParam("time") LocalTime time) {
        return dateSpecificTimeRepo.resolve(room, date, time)
            .orElseThrow(() -> new RuntimeException("oogao"));
    }

}
