package com.example.userservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {

    private List<ErrorDetail> error;

    public ErrorResponse(List<ErrorDetail> error) {
        this.error = error;
    }

    public List<ErrorDetail> getError() {
        return error;
    }

    public void setError(List<ErrorDetail> error) {
        this.error = error;
    }

    public static class ErrorDetail {
        private LocalDateTime timestamp;
        private Integer codigo;
        private String detail;

        public ErrorDetail(Integer codigo, String detail) {
            this.timestamp = LocalDateTime.now();
            this.codigo = codigo;
            this.detail = detail;
        }

        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

        public Integer getCodigo() { return codigo; }
        public void setCodigo(Integer codigo) { this.codigo = codigo; }

        public String getDetail() { return detail; }
        public void setDetail(String detail) { this.detail = detail; }
    }
}