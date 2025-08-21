package com.demo.employee.controller;

import com.demo.employee.dto.ApiResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractController {

    public <T> ResponseEntity<ApiResponseDTO<T>> handleResponse(T data, HttpStatus httpStatus, String message) {
        HttpStatus status = httpStatus != null ? httpStatus : HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponseDTO.success(data, message, status.value()));
    }

    public <T> ResponseEntity<ApiResponseDTO<T>> handleResponse(T data, String message) {
        return handleResponse(data, null, message);
    }

    public ResponseEntity<ApiResponseDTO<Void>> handleResponse(HttpStatus httpStatus, String message) {
        HttpStatus status = httpStatus != null ? httpStatus : HttpStatus.OK;
        return ResponseEntity.status(status)
                .body(ApiResponseDTO.success(null, message, status.value()));
    }

    public <T> ResponseEntity<ApiResponseDTO<T>> handlePaginatedResponse(Page<?> page, T data, String message) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDTO.successWithPagination(page, data, message, HttpStatus.OK.value()));
    }
}
