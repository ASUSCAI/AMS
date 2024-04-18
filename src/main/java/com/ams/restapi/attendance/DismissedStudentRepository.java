package com.ams.restapi.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DismissedStudentRepository extends JpaRepository<DismissedStudent, Long> {
    void deleteByCourseIdAndSectionIdAndDateAndStudentId(Long courseId, Long sectionId, String date, String studentId);
    DismissedStudent findByCourseIdAndSectionIdAndDateAndStudentId(Long courseId, Long sectionId, String date, String studentId);
}
