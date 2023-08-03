package com.melon.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionDTO {

    private String message;
    private int httpStatus;
}
