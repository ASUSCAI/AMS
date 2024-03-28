package com.ams.restapi.sectionInfo;

import com.ams.restapi.timeConfig.TimeConfigDTO;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class SectionInfoDTO {
    private String name, readerId;
    private List<DayOfWeek> daysOfWeek;
    private LocalTime startTime, endTime;
    private TimeConfigDTO defaultTimeConfig;

    public TimeConfigDTO getDefaultTimeConfig() {
        return defaultTimeConfig;
    }

    public void setDefaultTimeConfig(TimeConfigDTO defaultTimeConfig) {
        this.defaultTimeConfig = defaultTimeConfig;
    }

    public SectionInfoDTO() {
    }

    public SectionInfoDTO(SectionInfo sectionInfo) {
        name = sectionInfo.getName();
        readerId = sectionInfo.getReaderId();
        startTime = sectionInfo.getStartTime();
        endTime = sectionInfo.getEndTime();
        daysOfWeek = sectionInfo.getDaysOfWeek();
        defaultTimeConfig = new TimeConfigDTO(sectionInfo.getDefaultTimeConfig());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public SectionInfo toEntity(Long courseID) {
        SectionInfo course = new SectionInfo(courseID, name, readerId,
                daysOfWeek, startTime, endTime, null);
        if (defaultTimeConfig == null)
            course.setDefaultTimeConfig(
                    SectionInfo.getDefaultTimeConfig(course, startTime, endTime));
        else
            course.setDefaultTimeConfig(defaultTimeConfig.toEntity(course));
        return course;
    }

}
