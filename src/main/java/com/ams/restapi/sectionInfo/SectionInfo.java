package com.ams.restapi.sectionInfo;

import com.ams.restapi.timeConfig.DateSpecificTimeConfig;
import com.ams.restapi.timeConfig.TimeConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SectionInfo {

    public static final Long DEFAULT_TOLERANCE = 5L;
    public static final Long DEFAULT_LATE_TOLERANCE = 15L;

    private @Id Long id;
    private String name, readerId;
    // @CollectionTable(joinColumns = @JoinColumn(name = "courseinfo_id"))
    @ElementCollection(fetch = FetchType.EAGER)
    private List<DayOfWeek> daysOfWeek;
    private LocalTime startTime, endTime;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    private TimeConfig defaultTimeConfig;

    public TimeConfig getDefaultTimeConfig() {
        return defaultTimeConfig;
    }

    public void setDefaultTimeConfig(TimeConfig defaultTimeConfig) {
        this.defaultTimeConfig = defaultTimeConfig;
    }

    @JsonIgnore    
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<DateSpecificTimeConfig> dateSpecificTimeConfigs;

    public List<DateSpecificTimeConfig> getDateSpecificTimeConfigs() {
        return dateSpecificTimeConfigs;
    }

    public void setDateSpecificTimeConfigs(List<DateSpecificTimeConfig> dateSpecificTimeConfigs) {
        this.dateSpecificTimeConfigs = dateSpecificTimeConfigs;
    }

    public SectionInfo() {
    }

    public SectionInfo(Long id, String name, String readerId,
                       List<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.name = name;
        this.readerId = readerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.daysOfWeek = daysOfWeek;
        defaultTimeConfig = getDefaultTimeConfig(this, startTime, endTime);
        dateSpecificTimeConfigs = new ArrayList<>();
    }

    public SectionInfo(Long id, String name, String readerId,
                       List<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime,
                       TimeConfig defaultTimeConfig) {
        this.id = id;
        this.name = name;
        this.readerId = readerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.daysOfWeek = daysOfWeek;
        this.defaultTimeConfig = defaultTimeConfig;
        dateSpecificTimeConfigs = new ArrayList<>();
    }

    public static TimeConfig getDefaultTimeConfig(SectionInfo course, LocalTime startTime, LocalTime endTime) {
        TimeConfig config = new TimeConfig(course,
                startTime.minusMinutes(DEFAULT_TOLERANCE),
                startTime.plusMinutes(DEFAULT_TOLERANCE),
                startTime.plusMinutes(DEFAULT_LATE_TOLERANCE),
                endTime.minusMinutes(DEFAULT_TOLERANCE),
                endTime.plusMinutes(DEFAULT_TOLERANCE));

        if (course.getDefaultTimeConfig() != null) config.setId(course.getDefaultTimeConfig().getId());

        return config;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
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

    public String getName() {
        return name;
    }

    public void setName(String courseName) {
        this.name = courseName;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    @Override
    public String toString() {
        return "CourseInfo [id=" + id + ", room=" + readerId + ", courseName=" + name + ", daysOfWeek="
                + daysOfWeek + ", startTime=" + startTime + ", endTime=" + endTime + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SectionInfo other = (SectionInfo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    

    
}
