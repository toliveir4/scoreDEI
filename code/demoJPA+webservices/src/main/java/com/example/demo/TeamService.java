package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.data.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    @Autowired
    private PlayerRepository studentRepository;

    public List<Player> getAllPlayers() {
        List<Player> userRecords = new ArrayList<>();
        studentRepository.findAll().forEach(userRecords::add);
        return userRecords;
    }

    public void addPlayer(Player student) {
        studentRepository.save(student);
    }

    public Optional<Player> getPlayer(int id) {
        return studentRepository.findById(id);
    }

    public List<Player> findByNameEndsWith(String chars) {
        return studentRepository.findByNameEndsWith(chars);
    }

}