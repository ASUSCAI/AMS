package com.ams.restapi;

import com.ams.restapi.authentication.SectionRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {

    @Autowired
    private SectionRepository sectionRepository;

    @GetMapping("/")
    public String home(@RequestParam("courseId") String courseId, HttpServletResponse response) {
        System.out.println("HERE IS THE COURSE ID >>>>>>>>>" + courseId);
        response.addHeader("Course-Id", courseId);
        return "index.html";
//        return sectionRepository.findBySectionId(courseId)
//                .map(section -> {
//
//                })
//                .orElse("error.html");
    }
}
