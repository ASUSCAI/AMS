package com.ams.restapi.authentication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByRole(Enrollment.RoleType roleType);

    List<Enrollment> findByUser(User user);

    Set<Enrollment> findBySectionContains(Section section);

    Set<Enrollment> findByUserAndRoleContains(User users, Enrollment.RoleType role);
}
