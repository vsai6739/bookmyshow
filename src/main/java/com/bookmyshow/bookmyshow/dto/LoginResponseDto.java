package com.bookmyshow.bookmyshow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private ResponseStatus status;
    private Long userId;
}
