package com.fufa.fariska.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class SecretRequestDto {

    private int round;
    private String secret;
    private int cardHandNumber;
}