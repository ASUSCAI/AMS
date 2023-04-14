package com.ams.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ams.model.AttendanceLog;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "https://104.237.159.242:3001")
@RequestMapping("/hi")
public class HiController {

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public String hi(@RequestParam String name) {
        return "Hi " + name;
    }

}
