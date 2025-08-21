package com.demo.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseDTO<T> {
    private String status;
    private int code;
    private String message;
    private T data;
    private Map<String, Object> meta;
    private Instant timestamp = Instant.now();

    public static <T> ApiResponseDTO<T> success(T data, String message, int code) {
        return ApiResponseDTO.<T>builder()
                .status("success")
                .code(code)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponseDTO<T> successWithPagination(Page<?> page, T data, String message, int code) {
        Map<String, Object> paginationMeta = new HashMap<>();
        paginationMeta.put("currentPage", page.getNumber());
        paginationMeta.put("pageSize", page.getSize());
        paginationMeta.put("totalPages", page.getTotalPages());
        paginationMeta.put("totalElements", page.getTotalElements());
        paginationMeta.put("isLast", page.isLast());
        paginationMeta.put("isFirst", page.isFirst());

        return ApiResponseDTO.<T>builder()
                .status("success")
                .code(code)
                .message(message)
                .data(data)
                .meta(paginationMeta)
                .timestamp(Instant.now())
                .build();
    }

}