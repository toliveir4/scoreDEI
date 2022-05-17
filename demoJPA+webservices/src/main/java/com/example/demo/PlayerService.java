package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.data.Team;

@Service
public class PlayerService {
    @Autowired
    private TeamRepository teamRepository;

    public Team getTeam() {
        Team userRecords = new Team();
        teamRepository.findAll();
        return userRecords;
    }

    public void setTeam(Team team) {
        System.out.println(team);
        teamRepository.save(team);
    }

}