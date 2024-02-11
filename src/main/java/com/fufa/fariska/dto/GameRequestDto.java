package com.fufa.fariska.dto;

import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@Data
public class GameRequestDto {

    public GameRequestDto() {

    }

//    Set<Integer> packsId;
    @Max(100)
    private Integer packsId;
}
