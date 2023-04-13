package com.example.controller;

import java.util.HashMap;

import com.example.model.AttendanceLog;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "https://104.237.159.242:3001")
@RequestMapping("/attendance")
public class HelloWorldController {

    private static final HashMap<String, AttendanceLog[]> LOGS = new HashMap<>();
    static {
      LOGS.put("Wed Apr 05 2023", new AttendanceLog[] {
        new AttendanceLog(0L, "Chloe", 1111L, "Arrived"),
        new AttendanceLog(1L, "Jack", 2222L, "Arrived Late"),
        new AttendanceLog(2L, "Kim", 3333L, "Left Early"),
        new AttendanceLog(3L, "David", 4444L, "Left")
      });
      LOGS.put("Thu Apr 06 2023", new AttendanceLog[] {
        new AttendanceLog(4L, "Callean", 5555L, "Arrived"),
        new AttendanceLog(5L, "Alicia", 6666L, "Arrived Late"),
        new AttendanceLog(6L, "Janela", 7777L, "Left Early"),
        new AttendanceLog(7L, "Francklyn", 8888L, "Left")
      });
      LOGS.put("Fri Apr 07 2023", new AttendanceLog[] {
        new AttendanceLog(8L, "Bendix", 9999L, "Arrived"),
        new AttendanceLog(9L, "Leone", 1010L, "Arrived Late"),
        new AttendanceLog(10L, "Phillis", 1111L, "Left Early"),
        new AttendanceLog(11L, "Brook", 1212L, "Left")
      });
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public AttendanceLog[] attendance(@RequestParam String date) {
        return LOGS.get(date);
    }

}
