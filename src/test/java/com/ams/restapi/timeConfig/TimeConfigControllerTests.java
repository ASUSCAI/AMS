package com.ams.restapi.timeConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalTime;
import java.util.List;
import java.time.DayOfWeek;
import com.ams.restapi.courseInfo.CourseInfo;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
import org.springframework.security.test.context.support.WithMockUser;

@AutoConfigureMockMvc
@SpringBootTest
public class TimeConfigControllerTests {
    @Autowired private TimeConfigController controller;
    @Autowired private MockMvc mockMvc;

    @Test void contextLoads() throws Exception {
        assertNotNull(controller);
    }

    @Test
    @WithMockUser(roles="INSTRUCTOR") 
    void courseInfoShouldGenerateDefaultTimeConfig() throws Exception {
        LocalTime startIn = LocalTime.of(10, 10);
        LocalTime endIn = LocalTime.of(11, 50);
        CourseInfo testCourseInfo = new CourseInfo(1234L, 1234L, "CSE 110", "COOR170", List.of(DayOfWeek.MONDAY), startIn, endIn);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        MockHttpServletRequestBuilder request = put("/courseInfo/1234").content(mapper.writeValueAsString(testCourseInfo)).contentType("application/json");

        ResultActions response = mockMvc.perform(request);
        response.andExpect(status().isOk()).andDo(print());

        mockMvc.perform(get("/timeConfig/1234"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.beginIn").value("10:05:00"))
                .andExpect(jsonPath("$.endIn").value("10:15:00"))
                .andExpect(jsonPath("$.endLate").value("10:25:00"))
                .andExpect(jsonPath("$.beginOut").value("11:45:00"))
                .andExpect(jsonPath("$.endOut").value("11:55:00"))
                .andDo(print());
    }
}
