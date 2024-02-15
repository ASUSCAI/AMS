package com.ams.restapi.authentication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ams.restapi.courseInfo.CourseInfo;
import edu.ksu.canvas.interfaces.CourseReader;
import edu.ksu.canvas.interfaces.EnrollmentReader;
import edu.ksu.canvas.model.Course;
import edu.ksu.canvas.model.Enrollment;
import edu.ksu.canvas.requestOptions.GetEnrollmentOptions;
import edu.ksu.canvas.requestOptions.ListCurrentUserCoursesOptions;
import edu.ksu.canvas.requestOptions.ListUserCoursesOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ams.restapi.courseInfo.CourseInfoRepository;

import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;
import edu.ksu.canvas.oauth.OauthToken;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Component
public class CanvasCourseAndUserRefresher {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CourseInfoRepository courseInfoRepository;

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    UserService userService;

    private static final CanvasApiFactory API = new CanvasApiFactory(System.getenv("AMS_CANVAS_URL"));;
    private static final OauthToken TOKEN = new NonRefreshableOauthToken(System.getenv("AMS_CANVAS_API_TOKEN"));;

    @Scheduled(fixedRate = 360000)
    @Transactional
    public void updateCoursesAndUsers() throws IOException {

        CourseReader courseReader = API.getReader(CourseReader.class, TOKEN);
        EnrollmentReader courseEnrollmentReader = API.getReader(EnrollmentReader.class, TOKEN);
        List<Course> courseList = courseReader.listCurrentUserCourses(new ListCurrentUserCoursesOptions());
        for(Course course : courseList){
            if(course.getStartAt() != null && course.getStartAt().after(new Date(1704783600L))){
                courseInfoRepository.save(
                    new CourseInfo(course.getId(), course.getCourseCode(), "COOR170", null,
                            LocalTime.of(12, 15), LocalTime.of(13,  5)));
                Long courseId = course.getId();
                List<Enrollment> courseEnrollments = courseEnrollmentReader.getCourseEnrollments(new GetEnrollmentOptions(String.valueOf(courseId)));

                for (Enrollment enrollment : courseEnrollments) {
                    String email = enrollment.getUser().getLoginId();
                    String name = enrollment.getUser().getName();

                    User user = userService.findOrCreateUser(email, name);

                    Role.RoleType roleType = getRoleTypeFromCanvasEnrollment(enrollment.getType());
                    Role role = roleRepository.findByRole(roleType)
                            .orElseThrow(() -> new RuntimeException("Role not found"));
                    role = entityManager.merge(role);
                    user.getRoles().add(role);

                    userRepository.save(user);
                }
            }
        }
    }
    
    private Role.RoleType getRoleTypeFromCanvasEnrollment(String canvasRole) {
        switch (canvasRole) {
            case "TeacherEnrollment":
                return Role.RoleType.INSTRUCTOR;
            case "TaEnrollment":
                return Role.RoleType.TA;
            default:
                return Role.RoleType.DEFAULT;
        }
    }

    /*public List<CourseInfo> parseCSV(String filePath) {
        List<CourseInfo> sections = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                CourseInfo section = new CourseInfo(Long.parseLong(values[0]), Long.parseLong(values[1]), "CSE110", "COOR170",List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                        LocalTime.of(12, 15), LocalTime.of(13,  5));
                sections.add(section);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sections;
    }*/
}
