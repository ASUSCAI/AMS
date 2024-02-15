package com.ams.googleOAuth;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.ams.restapi.authentication.CanvasCourseAndUserRefresher;

public class GoogleOAuthTests {
    @Test 
    void TestSectionCode() throws IOException{
        CanvasCourseAndUserRefresher test = new CanvasCourseAndUserRefresher();
        test.updateCoursesAndUsers();
    }
}
