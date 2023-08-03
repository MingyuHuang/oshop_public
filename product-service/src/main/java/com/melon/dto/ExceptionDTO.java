package com.melon.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import javax.persistence.criteria.CriteriaBuilder;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ExceptionDTO {

    private String message;
    private int httpStatus;
}
