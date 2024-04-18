package com.ams.restapi.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcuseStudentRepository extends JpaRepository<ExcuseStudent, Long> {
    void deleteByCourseIdAndSectionIdAndDateAndStudentId(Long courseId, Long sectionId, String date, String studentId);
    ExcuseStudent findByCourseIdAndSectionIdAndDateAndStudentId(Long courseId, Long sectionId, String date, String studentId);
}
