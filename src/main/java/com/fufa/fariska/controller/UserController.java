package com.fufa.fariska.controller;


import com.fufa.fariska.dto.GameUserDTO;
import com.fufa.fariska.entity.GameUser;
import com.fufa.fariska.service.GameUserDetailsService;
import com.fufa.fariska.service.UserService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/new")
    public ResponseEntity<GameUser> create(@RequestBody GameUserDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GameUser>> readAll() {
        return new ResponseEntity<>(service.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<GameUser>> read(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(service.read(id).isPresent() ? service.read(id) : null, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<GameUser> update(@RequestBody GameUser user) {
        return new ResponseEntity<>(service.update(user), HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    public HttpStatus delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return HttpStatus.OK;
    }
}
