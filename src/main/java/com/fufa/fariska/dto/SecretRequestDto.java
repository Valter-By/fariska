package com.fufa.fariska.dto;

import com.fufa.fariska.entities.Player;
import com.fufa.fariska.entities.User;
import com.fufa.fariska.entities.enums.GameStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;

@SuperBuilder
@Data
public class SecretRequestDto {
    private String secret;
    private int cardHandNumber;
}