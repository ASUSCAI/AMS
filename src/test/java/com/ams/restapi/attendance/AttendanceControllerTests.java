package com.ams.restapi.attendance;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
    @MockBean private DismissedStudentRepository dismissedStudentRepository;

    @Test void contextLoads() throws Exception {
        assertNotNull(controller);
    }

    @Test

    @WithMockUser(roles="INSTRUCTOR")
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

    @Test
    @WithMockUser(roles="INSTRUCTOR")
    void deleteRecord() throws Exception {
        mockMvc.perform(get("/attendance/25"))
            .andExpect(status().isOk())
            .andDo(print());
        mockMvc.perform(delete("/attendance/25")
            .with(csrf()))
            .andDo(print());
        mockMvc.perform(get("/attendance/25"))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    @WithMockUser(roles="INSTRUCTOR")
     void shouldUpdateAttendenceRecord() throws Exception {
        mockMvc.perform(put("/attendance/33")
                .with(csrf())
                .content("{\n" + //
                            "\t\"room\": \"COOR170\",\n" + //
                            "\t\"date\": \"2024-01-11\",\n" + //
                            "\t\"time\": \"13:00\",\n" + //
                            "\t\"sid\": \"1221000004\",\n" + //
                            "\t\"type\": \"LEFT\"\n" + //
                            "}").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$.id").value("33"))
                .andExpect(jsonPath("$.room").value("COOR170"))
                .andExpect(jsonPath("$.date").value("2024-01-11"))
                .andExpect(jsonPath("$.time").value("13:00"))
                .andExpect(jsonPath("$.sid").value("1221000004"))
                .andExpect(jsonPath("$.type").value("LEFT"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles="INSTRUCTOR")
    void shouldGetAttendanceRecordbyID() throws Exception {
        mockMvc.perform(get("/attendance/33"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("33"))
                .andExpect(jsonPath("$.room").value("COOR170"))
                .andExpect(jsonPath("$.date").value("2024-01-11"))
                .andExpect(jsonPath("$.time").value("13:00"))
                .andExpect(jsonPath("$.sid").value("1221000004"))
                .andExpect(jsonPath("$.type").value("LEFT"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles="INSTRUCTOR")
    void dismissStudent() throws Exception {
        String studentId = "123456789";
        Long attendanceRecordId = 1L;

        mockMvc.perform(post("/attendance/{id}/dismiss", attendanceRecordId)
                .param("studentId", studentId)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.studentId").value(studentId))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles="INSTRUCTOR")
    void isStudentDismissed() throws Exception {
        String studentId = "123456789";

        when(dismissedStudentRepository.existsByStudentId(studentId)).thenReturn(true);

        mockMvc.perform(get("/attendance/{id}/dismiss", 1L)
                .param("studentId", studentId)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").value(true))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles="INSTRUCTOR")
    void cancelDismissal() throws Exception {
        String studentId = "123456789";

        mockMvc.perform(delete("/attendance/{id}/dismiss", 1L)
                .param("studentId", studentId)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("Dismissal cancelled for student ID: " + studentId))
                .andDo(print());
    }
}
