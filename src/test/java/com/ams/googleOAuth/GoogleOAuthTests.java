package com.ams.googleOAuth;

import com.ams.restapi.authentication.CanvasCourseAndUserRefresher;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GoogleOAuthTests {
    @Test 
    void TestSectionCode() throws IOException{
        CanvasCourseAndUserRefresher test = new CanvasCourseAndUserRefresher();
        test.updateCoursesAndUsers();
    }
}
