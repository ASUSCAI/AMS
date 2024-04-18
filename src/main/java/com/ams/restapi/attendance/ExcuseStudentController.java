package com.ams.restapi.attendance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.HttpStatus;

/**
 * Excuse student endpoints
 * 
 * @author Daniel Liao
 */
@RestController
class ExcuseStudentRepositoryController {

    private final ExcuseStudentRepository excusedStudentRepository;

    @PersistenceContext
    private EntityManager eManager;

    ExcuseStudentRepositoryController(ExcuseStudentRepository dismissedStudentRepository) {
        this.excusedStudentRepository = dismissedStudentRepository;
    }

    @PostMapping("/attendance/excusal/{course_id}/{section_id}/{date}/{student_id}/excusal")
    ResponseEntity<ExcuseStudent> excuseStudent(
            @PathVariable("course_id") Long courseId,
            @PathVariable("section_id") Long sectionId,
            @PathVariable("date") String date,
            @PathVariable("student_id") String studentId) {

        ExcuseStudent excusedStudent = new ExcuseStudent();
        excusedStudent.setCourseId(courseId);
        excusedStudent.setSectionId(sectionId);
        excusedStudent.setDate(date);
        excusedStudent.setStudentId(studentId);
        ExcuseStudent savedExcusedStudent = excusedStudentRepository.save(excusedStudent);

        return ResponseEntity.ok(savedExcusedStudent);
    }

    @GetMapping("/attendance/excusal/{course_id}/{section_id}/{date}/{student_id}/excusal")
    ResponseEntity<ExcuseStudent> getExcusedStudent(
            @PathVariable("course_id") Long courseId,
            @PathVariable("section_id") Long sectionId,
            @PathVariable("date") String date,
            @PathVariable("student_id") String studentId) {

        ExcuseStudent excusedStudent = excusedStudentRepository
                .findByCourseIdAndSectionIdAndDateAndStudentId(
                        courseId, sectionId, date, studentId);

        if (excusedStudent != null) {
            return ResponseEntity.ok(excusedStudent);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Dismissed student not found for course " + courseId +
                            ", section " + sectionId + ", date " + date + ", student " + studentId);
        }
    }

    @DeleteMapping("/attendance/excusal/{course_id}/{section_id}/{date}/{student_id}/excusal")
    ResponseEntity<String> cancelExcusal(
            @PathVariable("course_id") Long courseId,
            @PathVariable("section_id") Long sectionId,
            @PathVariable("date") String date,
            @PathVariable("student_id") String studentId) {

        excusedStudentRepository.deleteByCourseIdAndSectionIdAndDateAndStudentId(courseId, sectionId, date,
                studentId);
        return ResponseEntity.ok("Excusal cancelled for student ID: " + studentId);
    }

}
