package com.example.userservice.dto;

import com.example.userservice.validation.ValidEmail;
import com.example.userservice.validation.ValidPassword;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class UserSignUpRequest {

    @NotBlank(message = "Email is required")
    @ValidEmail
    private String email;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;

    private String name;

    @Valid
    private List<PhoneDto> phones;

    public UserSignUpRequest() {}

    public UserSignUpRequest(String email, String password, String name, List<PhoneDto> phones) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phones = phones;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<PhoneDto> getPhones() { return phones; }
    public void setPhones(List<PhoneDto> phones) { this.phones = phones; }
}