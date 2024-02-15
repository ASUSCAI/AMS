package com.ams.restapi.courseInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {

    @Autowired
    private CourseInfoRepository courseInfoRepository;

    @GetMapping("/")
    public String home(@RequestParam("courseId") String courseId){
        System.out.println("HERE IS THE COURSE ID >>>>>>>>>" + courseId);
        if(courseInfoRepository.findById(Long.parseLong(courseId)).get() != null){
            return "index.html";
        }
        return "error.html";
    }
}
