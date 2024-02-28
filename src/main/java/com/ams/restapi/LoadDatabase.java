package com.ams.restapi;

import com.ams.restapi.attendance.AttendanceRecord;
import com.ams.restapi.attendance.AttendanceRecord.AttendanceType;
import com.ams.restapi.attendance.AttendanceRepository;
import com.ams.restapi.sectionInfo.SectionInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
class LoadDatabase {
    
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    private static final boolean BEAN = true;

    @Bean
    CommandLineRunner initDatabase(AttendanceRepository attendance, SectionInfoRepository courseInfo) {
        if (BEAN) {
            return args -> {
                log.info("BEAN MODE ACTIVATED");
                BufferedReader reader = new BufferedReader(new FileReader("./mock.csv"));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split("\\s*,\\s*");
                    log.debug("Preloading " + attendance.save(
                            new AttendanceRecord(tokens[0], LocalDate.parse(tokens[1]),
                                    LocalTime.parse(tokens[2]), tokens[3], AttendanceType.valueOf(tokens[4]))));
                }
                reader.close();

                /*courseInfo.save(
                    new CourseInfo(
                        85L, 77L, "CSE110", "COOR170",
                        List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                        LocalTime.of(12, 15), LocalTime.of(13,  5))
                );

                System.out.println(courseInfo.findById(85L).get().getDefaultTimeConfig());*/

                // times.save(new TimeConfig(
                //     1234L,
                //     LocalTime.of(12, 10),
                //     LocalTime.of(12, 20),
                //     LocalTime.of(12, 30),
                //     LocalTime.of(13, 0),
                //     LocalTime.of(13, 20)));
            };
        }
        return args -> {log.info("BEAN MODE DEACTIVATED");};
    }

}
