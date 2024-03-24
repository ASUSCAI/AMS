package com.ams.restapi.timeConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.ams.restapi.courseInfo.CourseInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TimeConfigControllerTests {
    @Autowired private TimeConfigController controller;
    @Autowired private MockMvc mockMvc; 

    @Test void contextLoads() throws Exception {
        assertNotNull(controller);
    }

    private String unwrapQuotes(String str) {
        if (str.indexOf("\"") == 0 && str.lastIndexOf("\"") == str.length()-1)
            return str.substring(1, str.length()-1);
        else
            throw new IllegalArgumentException("Not wrapped in double quotes");
    }

    @Test void courseInfoShouldCreateTimeConfig() throws Exception {
        // utility
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // setting up test
        LocalTime startTime = LocalTime.of(8, 30);
        LocalTime endTime = LocalTime.of(9, 45);
        CourseInfo testCourseInfo = new CourseInfo(1234L, 1234L, "CSE 110", "COOR170",
            List.of(DayOfWeek.MONDAY),
            startTime, endTime);
        
        // defining expectations
        LocalTime expectedBeginIn = startTime.minusMinutes(CourseInfo.DEFAULT_TOLERANCE);
        LocalTime expectedEndIn = startTime.plusMinutes(CourseInfo.DEFAULT_TOLERANCE);
        LocalTime expectedEndLate = startTime.plusMinutes(CourseInfo.DEFAULT_LATE_TOLERANCE);
        LocalTime expectedBeginOut = endTime.minusMinutes(CourseInfo.DEFAULT_TOLERANCE);
        LocalTime expectedEndOut = endTime.plusMinutes(CourseInfo.DEFAULT_TOLERANCE);

        // sending requests and checked for expected

        // OOP WAY OF SENDING MOCK MVC REQUEST AND CHECKING EXPECTED        
        // MockHttpServletRequestBuilder request = put("/courseInfo/1234")
        // .content(mapper.writeValueAsString(testCourseInfo))
        // .contentType("application/json");
        
        // ResultActions response = mockMvc.perform(request);
        // response.andExpect(status().isOk()).andDo(print());
        
        // FUNCTIONAL WAY OF SENDING MOCK MVC REQUEST AND CHECKING EXPECTED

        mockMvc.perform(put("/courseInfo/1234")
            .contentType("application/json")
            // .content(mapper.writeValueAsString(testCourseInfo)))
            .content(mapper.writeValueAsString(testCourseInfo)))
            .andExpect(status().isOk()).andDo(print());

        mockMvc.perform(get("/timeConfig/1234"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.beginIn").value(unwrapQuotes(mapper.writeValueAsString(expectedBeginIn))))
            .andExpect(jsonPath("$.endIn").value(unwrapQuotes(mapper.writeValueAsString(expectedEndIn))))
            .andExpect(jsonPath("$.endLate").value(unwrapQuotes(mapper.writeValueAsString(expectedEndLate))))
            .andExpect(jsonPath("$.beginOut").value(unwrapQuotes(mapper.writeValueAsString(expectedBeginOut))))
            .andExpect(jsonPath("$.endOut").value(unwrapQuotes(mapper.writeValueAsString(expectedEndOut))))
            .andDo(print());
    }
}
