package com.ams.restapi.timeConfig;

import com.ams.restapi.sectionInfo.SectionInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class DateSpecificTimeConfig {

    private @Id @GeneratedValue Long id;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne(cascade = CascadeType.ALL)
    private TimeConfig config;

    public TimeConfig getConfig() {
        return config;
    }

    public void setConfig(TimeConfig config) {
        this.config = config;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private SectionInfo course;

    public SectionInfo getCourse() {
        return course;
    }

    public void setCourse(SectionInfo course) {
        this.course = course;
    }

    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public DateSpecificTimeConfig() {
        System.out.println("JPA LOADED  ME");
    }

    public DateSpecificTimeConfig(SectionInfo course, LocalDate date, TimeConfig timeConfig) {
        this.course = course;
        this.date = date;
        this.config = timeConfig;
    }

    @Override
    public String toString() {
        return "DateSpecificTimeConfig [id=" + id + ", course=" + course + ", date=" + date + "]";
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
        DateSpecificTimeConfig other = (DateSpecificTimeConfig) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
