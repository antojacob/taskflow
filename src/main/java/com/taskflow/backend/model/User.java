package com.taskflow.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> ownedProjects = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "project_members", 
               joinColumns = @JoinColumn(name = "user_id"), 
               inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> assignedProjects = new ArrayList<>();

    // === MANUAL GETTERS ===
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public List<Project> getOwnedProjects() {
        return ownedProjects;
    }

    public List<Project> getAssignedProjects() {
        return assignedProjects;
    }

    // === MANUAL SETTERS ===
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName; 
    }

    public void setOwnedProjects(List<Project> ownedProjects) {
        this.ownedProjects = ownedProjects;
    }

    public void setAssignedProjects(List<Project> assignedProjects) {
        this.assignedProjects = assignedProjects;
    }
}