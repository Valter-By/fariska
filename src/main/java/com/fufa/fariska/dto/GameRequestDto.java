package com.fufa.fariska.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@Data
public class GameRequestDto {

    Set<Integer> packsId;
}
