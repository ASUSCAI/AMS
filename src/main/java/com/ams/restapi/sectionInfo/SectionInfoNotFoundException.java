package com.ams.restapi.sectionInfo;

public class SectionInfoNotFoundException extends RuntimeException {
    SectionInfoNotFoundException(Long courseId) {
        super("Could not find courseInfo log " + courseId);
    }
}