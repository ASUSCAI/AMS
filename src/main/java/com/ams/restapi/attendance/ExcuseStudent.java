package com.ams.restapi.attendance;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.NoSuchElementException;
import java.util.Optional;

@Entity
public class ExcuseStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long courseId;
    private Long sectionId;
    private String date;
    private String studentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public static ExcuseStudent orElseThrow(Optional<ExcuseStudent> optionalDismissedStudent, Long courseId, Long sectionId, String date, String studentId) {
        return optionalDismissedStudent.orElseThrow(() -> new NoSuchElementException(
                "Dismissed student not found for course " + courseId +
                        ", section " + sectionId + ", date " + date + ", student " + studentId));
    }
}