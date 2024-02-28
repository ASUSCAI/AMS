package com.ams.restapi.timeConfig;

import com.ams.restapi.sectionInfo.SectionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface DateSpecificTimeRepository
        extends JpaRepository<DateSpecificTimeConfig, Long> {
    Optional<DateSpecificTimeConfig> findByCourseAndDate(SectionInfo course, LocalDate date);

    @Query("SELECT c.config FROM DateSpecificTimeConfig c "
    + "inner join c.course cour "
    + "inner join c.config conf WHERE "
    + "(:room = cour.room) and "
    + "(:date = c.date) and "
    + "(:time between conf.beginIn and conf.endOut)")
    Optional<TimeConfig> resolve(
        @Param("room") String room,
        @Param("date") LocalDate date,
        @Param("time") LocalTime time);
}