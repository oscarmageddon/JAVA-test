package com.example.userservice.dto;

import javax.validation.constraints.NotNull;

public class PhoneDto {

    @NotNull(message = "Phone number is required")
    private Long number;

    @NotNull(message = "City code is required")
    private Integer citycode;

    @NotNull(message = "Country code is required")
    private String contrycode;

    public PhoneDto() {}

    public PhoneDto(Long number, Integer citycode, String contrycode) {
        this.number = number;
        this.citycode = citycode;
        this.contrycode = contrycode;
    }

    public Long getNumber() { return number; }
    public void setNumber(Long number) { this.number = number; }

    public Integer getCitycode() { return citycode; }
    public void setCitycode(Integer citycode) { this.citycode = citycode; }

    public String getContrycode() { return contrycode; }
    public void setContrycode(String contrycode) { this.contrycode = contrycode; }
}