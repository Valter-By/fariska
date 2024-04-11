package com.fufa.fariska.service;

import com.fufa.fariska.dto.GameUserDTO;
import com.fufa.fariska.entity.GameUser;
import com.fufa.fariska.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    public final UserRepository repository;
    private final PasswordEncoder encoder;

    public GameUser create(GameUserDTO dto) {
        return repository.save(
                GameUser.builder()
                        .name(dto.getName())
                        .password(encoder.encode(dto.getPassword()))
                        .build()
        );
    }

    public List<GameUser> readAll() {
        return repository.findAll();
    }
    public Optional<GameUser> read(Integer id) {
        return repository.findById(id);
    }

    public GameUser update(GameUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
