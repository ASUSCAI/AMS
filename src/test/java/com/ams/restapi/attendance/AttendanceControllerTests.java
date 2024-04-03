package com.ams.restapi.attendance;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AttendanceControllerTests {
    @Autowired private AttendanceController controller;
    @Autowired private MockMvc mockMvc; 

    @Test void contextLoads() throws Exception {
        assertNotNull(controller);
    }

    @Test
    void shouldReturnCorrectPageSize() throws Exception {
        mockMvc.perform(get("/attendance")
                .param("room", "COOR170")
                .param("date", "2024-01-11")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "time")
                .param("sortType", "asc")
                .param("types", "LEFT"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[*].type").value(everyItem(equalTo("LEFT"))))
                .andDo(print());
    }
}
