package com.ams.restapi.authentication;

import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.interfaces.SectionReader;
import edu.ksu.canvas.interfaces.UserReader;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;
import edu.ksu.canvas.oauth.OauthToken;
import edu.ksu.canvas.requestOptions.GetUsersInCourseOptions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private SectionRepository sectionRepository;

    private static final CanvasApiFactory API = new CanvasApiFactory(System.getenv("AMS_CANVAS_URL"));
    private static final OauthToken TOKEN = new NonRefreshableOauthToken(System.getenv("AMS_CANVAS_API_TOKEN"));

    @Transactional
    public User findOrCreateUser(String email, String name, String courseId) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserReader userReader = API.getReader(UserReader.class, TOKEN);
                    GetUsersInCourseOptions options = new GetUsersInCourseOptions(courseId);
                    options.searchTerm(email);
                    options.include(List.of(GetUsersInCourseOptions.Include.ENROLLMENTS));

                    try {
                        for (edu.ksu.canvas.model.User u : userReader.getUsersInCourse(options)) {
                            if (u.getEmail().equals(email)) {
                                User newUser = new User();
                                newUser.setEmail(email);
                                newUser.setName(name);
                                newUser = userRepository.saveAndFlush(newUser); // Save the User first

                                SectionReader sectionReader = API.getReader(SectionReader.class, TOKEN);
                                for (edu.ksu.canvas.model.Enrollment e : u.getEnrollments()) {
                                    Enrollment newEnrollment = new Enrollment();
                                    newEnrollment.setRole(getRoleTypeFromCanvasEnrollment(e.getType()));
                                    // Associate the Enrollment with the Section and User
                                    Optional<edu.ksu.canvas.model.Section> s = sectionReader.getSingleSection(e.getCourseSectionId());
                                    if (s.isPresent()) {
                                        Section newSection = sectionRepository.findBySectionId(String.valueOf(s.get().getId()))
                                                .orElseGet(() -> {
                                                    Section tempSection = new Section();
                                                    tempSection.setName(s.get().getName());
                                                    tempSection.setSectionId(String.valueOf(s.get().getId()));
                                                    // Ensure the Section is saved before being associated
                                                    return sectionRepository.save(tempSection);
                                                });
                                        newEnrollment.setSection(newSection);
                                        // Use the utility method to add the Enrollment to the User
                                        newUser.addEnrollment(newEnrollment);
                                        // Now, the Enrollment is correctly associated with the User, and you can save it
                                        enrollmentRepository.save(newEnrollment);
                                    }
                                }
                                return newUser; // The User is already saved, just return the instance
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                });
    }


    private Enrollment.RoleType getRoleTypeFromCanvasEnrollment(String canvasRole) {
        return switch (canvasRole) {
            case "TeacherEnrollment" -> Enrollment.RoleType.INSTRUCTOR;
            case "TaEnrollment" -> Enrollment.RoleType.TA;
            default -> null;
        };
    }
}

