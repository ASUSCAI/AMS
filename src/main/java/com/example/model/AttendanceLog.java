package com.example.model;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;

// @Entity
// @Table(name = "attendance_logs")
public class AttendanceLog {

    // @Id
    // @GeneratedValue
    private Long id;

    private Long time;
    private String name;
    private Long sid;
    private String type;

    protected AttendanceLog() {}

    public AttendanceLog(Long time, String name, Long sid, String type) {
      this.time = time;
      this.name = name;
      this.sid = sid;
      this.type = type;
    }

    @Override
    public String toString() {
      return String.format(
        "AttendanceLog[time=%d, name='%s', sid=%d, type='%s']",
          time, name, sid, type);
    }

    public Long getTime() {
      return time;
    }

    public String getName() {
      return name;
    }

    public Long getSID() {
      return sid;
    }

    public String getType() {
      return type;
    }

}
