//package com.fufa.fariska.controllers;
//
//import com.fufa.fariska.entities.Pack;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//public class PackController {
//    @GetMapping("/packs")
//    public List<Pack> getAllPacks() {
//        return new ArrayList<>();
//    }
//
//    @GetMapping("/packs/{id}")
//    public String getPack(@PathVariable int id) {
//        return "Pack " + id;
//    }
//
//    @PostMapping("/packs")
//    public Pack createPack(@RequestBody Pack pack) {
//        return Pack.builder().build();
//    }
//}
