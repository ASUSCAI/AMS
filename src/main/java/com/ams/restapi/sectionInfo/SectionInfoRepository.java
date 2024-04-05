package com.ams.restapi.sectionInfo;

import com.ams.restapi.timeConfig.TimeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

public interface SectionInfoRepository extends JpaRepository<SectionInfo, Long> {
    @Query("SELECT c.defaultTimeConfig FROM SectionInfo c join c.daysOfWeek d "
            + "inner join c.defaultTimeConfig conf WHERE "
            + "(:defaultReaderId = c.defaultReaderId) and "
            + "(:dayOfWeek = d) and "
            + "(:time between conf.beginIn and conf.endOut)")
    Optional<TimeConfig> resolve(
            @Param("defaultReaderId") String defaultReaderId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("time") LocalTime time);
}