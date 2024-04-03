
package com.ams.restapi.attendance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DismissedStudentRepository extends JpaRepository<DismissedStudent, Long> {
    boolean existsByStudentId(String studentId);
    void deleteByStudentId(String studentId);
}

