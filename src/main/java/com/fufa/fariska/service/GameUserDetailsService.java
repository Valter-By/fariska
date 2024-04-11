package com.fufa.fariska.service;

import com.fufa.fariska.config.GameUserDetails;
import com.fufa.fariska.dto.GameUserDTO;
import com.fufa.fariska.entity.GameUser;
import com.fufa.fariska.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameUserDetailsService implements UserDetailsService {

//    @Autowired
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<GameUser> user = repository.findByName(username);
        return user.map(GameUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }
}
