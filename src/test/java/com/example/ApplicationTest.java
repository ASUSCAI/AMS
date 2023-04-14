package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ams.model.AttendanceLog;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetMessage() {
        ResponseEntity<AttendanceLog> response = restTemplate.getForEntity("/attendance", AttendanceLog.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Message message = response.getBody();
        // assertThat(message.getMessage()).isEqualTo("Hello, World!");
    }

}
