package com.fufa.fariska.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public class SecretRequestDto {

    private Integer round;
    private String secret;
    private Integer cardHandNumber;
}