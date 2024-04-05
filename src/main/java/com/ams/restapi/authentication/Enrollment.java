package com.ams.restapi.authentication;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Enrollment implements GrantedAuthority {
    private @Id
    @GeneratedValue Long id;

    public static enum RoleType {
        INSTRUCTOR, TA, ADMIN, DEFAULT, ESP
    }

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Enrollment() {
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return "ROLE_" + this.role.name();
    }

    public Long getId() {
        return id;
    }

    public RoleType getRole() {
        return role;
    }

    public void setId(Long roleId) {
        this.id = roleId;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
