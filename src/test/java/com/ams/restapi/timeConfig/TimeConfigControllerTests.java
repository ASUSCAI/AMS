package com.ams.restapi.timeConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalTime;
import java.util.List;
import java.time.DayOfWeek;
import com.ams.restapi.courseInfo.CourseInfo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;

public class TimeConfigControllerTests {
    @Autowired private TimeConfigController controller;
    @Autowired private MockMvc mockMvc;

    @Test void contextLoads() throws Exception {
        assertNotNull(controller);
    }

    // @BeforeEach
    // void setUp() {
    //     mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    // }

    @Test void courseInfoShouldGenerateDefaultTimeConfig() throws Exception {
        LocalTime startTime = LocalTime.of(8, 30);
        LocalTime endTime = LocalTime.of(9, 45);
        CourseInfo testCourseInfo = new CourseInfo(1234L, 1234L, "CSE 110", "COOR170", List.of(DayOfWeek.MONDAY), startTime, endTime);
        TimeConfig defaultTimeConfig = CourseInfo.getDefaultTimeConfig(testCourseInfo, startTime, endTime);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        MockHttpServletRequestBuilder request = put("/courseInfo/1234").content(mapper.writeValueAsString(defaultTimeConfig)).contentType("applications/json");

        ResultActions response = mockMvc.perform(request);
        response.andExpect(status().isOk()).andDo(print());

        mockMvc.perform(get("/timeConfig/1234"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.beginIn").value("9:25:00"))
                .andExpect(jsonPath("$.endIn").value("9:25:00"))
                .andExpect(jsonPath("$.endLate").value("9:25:00"))
                .andExpect(jsonPath("$.beginOut").value("9:25:00"))
                .andExpect(jsonPath("$.endOut").value("9:25:00"))
                .andDo(print());
    }
}
