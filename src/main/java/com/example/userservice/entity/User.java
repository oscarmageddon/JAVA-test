package com.example.userservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false)
    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Phone> phones;

    public User() {
        this.created = LocalDateTime.now();
        this.lastLogin = LocalDateTime.now();
        this.isActive = true;
    }

    public User(String email, String password, String name, List<Phone> phones) {
        this();
        this.email = email;
        this.password = password;
        this.name = name;
        this.phones = phones;
        if (phones != null) {
            phones.forEach(phone -> phone.setUser(this));
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreated() { return created; }
    public void setCreated(LocalDateTime created) { this.created = created; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public List<Phone> getPhones() { return phones; }
    public void setPhones(List<Phone> phones) { 
        this.phones = phones;
        if (phones != null) {
            phones.forEach(phone -> phone.setUser(this));
        }
    }
}