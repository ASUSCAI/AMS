package com.ams.restapi.autograder;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AutograderService {

    private final Map<String, Boolean> taskStatusMap = new ConcurrentHashMap<>();
    
    public boolean taskExists(String key) {
        return taskStatusMap.containsKey(key);
    }

    public boolean taskRunning(String key) {
        return taskExists(key) && taskStatusMap.get(key);
    }

    @Async
    public CompletableFuture<Void> executeTaskAsync(String sectionId, String date) {
        String key = sectionId + "-" + date;
        taskStatusMap.put(key, true);
        try {
            Thread.sleep(15000); // Simulating a task that takes 15 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        taskStatusMap.put(key, false);
        return CompletableFuture.completedFuture(null);
    }
}
