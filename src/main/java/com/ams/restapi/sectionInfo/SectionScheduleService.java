package com.ams.restapi.sectionInfo;

import com.ams.restapi.espCommunication.ReaderService;
import com.ams.restapi.timeConfig.TimeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionScheduleService {

    @Autowired
    private SectionInfoRepository sectionInfoRepository;

    @Autowired
    private ReaderService readerService;

    @Scheduled(fixedDelay = 60000) //check every minute
    public void updateReaderAssignments() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // Get the current day of the week
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        // Find all sections active at the current time
        List<SectionInfo> activeSections = sectionInfoRepository.findAll().stream()
                .filter(section -> section.getDaysOfWeek().contains(dayOfWeek))
                .filter(section -> isSectionActive(section, now))
                .collect(Collectors.toList());

        // Update reader assignments based on active sections
        for (SectionInfo section : activeSections) {
            String designatedReaderId = section.getReaderId();
            readerService.updateReaderSection(designatedReaderId, section.getId().toString());
        }
    }

    private boolean isSectionActive(SectionInfo section, LocalTime now) {
        TimeConfig defaultTimeConfig = section.getDefaultTimeConfig();
        // Check if current time is within the section's active time, considering the buffer
        return now.isAfter(defaultTimeConfig.getBeginIn()) && now.isBefore(defaultTimeConfig.getEndOut());
    }
}

