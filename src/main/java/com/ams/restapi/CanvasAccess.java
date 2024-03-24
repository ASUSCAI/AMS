package com.ams.restapi;

import org.springframework.stereotype.Component;

import com.ams.restapi.attendance.AttendanceRepository;

import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.interfaces.CanvasReader;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;
import edu.ksu.canvas.oauth.OauthToken;

@Component
public class CanvasAccess {

    private final CanvasApiFactory apiFactory;
    private final OauthToken oauthToken;

    public CanvasAccess() {
        apiFactory = new CanvasApiFactory(System.getenv("AMS_CANVAS_URL"));
        oauthToken = new NonRefreshableOauthToken(System.getenv("AMS_CANVAS_API_TOKEN"));
        System.out.println("USING TOKEN: " + oauthToken.getAccessToken());
    }

    public <T extends CanvasReader> T getReader(Class<T> type) {
        return apiFactory.getReader(type, oauthToken);
    }
}
