package com.fufa.fariska.utils;

import com.fufa.fariska.dto.PlayerDto;
import com.fufa.fariska.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MakeListDto {

    public static List<PlayerDto> makeListPlayerDto(List<Player> players) {
        List<PlayerDto> playerDtos = new ArrayList<>(9);
        for (Player player : players) {
            playerDtos.add(player.makeDto());
        }
        return playerDtos;
    }
}
