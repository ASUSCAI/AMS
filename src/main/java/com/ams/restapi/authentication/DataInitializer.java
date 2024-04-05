package com.ams.restapi.authentication;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private AdminEmailService adminEmailService;

    @PostConstruct
    public void initAdminUser() {
        adminEmailService.getAdminEmails().forEach(adminEmail -> {
            User adminUser = userRepository.findByEmail(adminEmail).orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(adminEmail);
                return userRepository.saveAndFlush(newUser); // Ensure user is saved and flushed
            });

            if (enrollmentRepository.findByUser(adminUser).isEmpty()) {
                Enrollment adminEnrollment = new Enrollment();
                adminEnrollment.setRole(Enrollment.RoleType.ADMIN);
                adminEnrollment.setUser(adminUser); // User is already persisted
                // Set other properties of adminEnrollment
                enrollmentRepository.save(adminEnrollment); // Now it's safe to save Enrollment
            }
        });

    }

//    private void ensureRolesExist() {
//        for (Enrollment.RoleType roleType : Enrollment.RoleType.values()) {
//            List<Enrollment> enrollments = enrollmentRepository.findByRole(roleType);
//            if (enrollments.isEmpty()) {
//                Enrollment newEnrollment = new Enrollment();
//                newEnrollment.setRole(roleType);
//                // You might need to set a default or a null section here, or adjust your model to handle this case
//                enrollmentRepository.save(newEnrollment);
//            }
//        }
//    }
}
