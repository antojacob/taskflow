package com.taskflow.backend.model;

import jakarta.persistence.*;
import lombok.*;
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
    @JoinTable(name = "project_members", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> assignedProjects = new ArrayList<>();
}