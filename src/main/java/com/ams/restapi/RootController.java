package com.ams.restapi;

import com.ams.restapi.sectionInfo.SectionInfoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {

    @Autowired
    private SectionInfoRepository sectionInfoRepository;

    @GetMapping("/")
    public String home(@RequestParam("courseId") String courseId, HttpServletResponse response) {
        System.out.println("HERE IS THE COURSE ID >>>>>>>>>" + courseId);
        if (sectionInfoRepository.findById(Long.parseLong(courseId)).get() != null) {
            response.addHeader("Course-Id", courseId);
            return "index.html";
        }
        return "error.html";
    }

    private String extractCourseIdFromReferer(String referer) {
        return "Extracted CourseId";
    }

}
