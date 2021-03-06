package com.example.demo;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.data.Team;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    public List<String> getOptions() {
        List<String> options = new ArrayList<String>();
        options.add("Most Games");
        options.add("Least Games");
        options.add("Most Wins");
        options.add("Least Wins");
        options.add("Most Draws");
        options.add("Least Draws");
        options.add("Most Defeats");
        options.add("Least Defeats");
        
        return options;
    }

    public List<Team> getAllTeams() {
        List<Team> userRecords = new ArrayList<>();
        teamRepository.findAll().forEach(userRecords::add);
        return userRecords;
    }

    public void addTeam(Team t) {
        System.out.println(t);
        teamRepository.save(t);
    }

    public Optional<Team> getTeam(int id) {
        return teamRepository.findById(id);
    }

    Team findByName(String name) {
        return teamRepository.findByName(name);
    }

    List<Team> orderByGamesASC() {
        return teamRepository.orderByGamesASC();
    }

    List<Team> orderByGamesDESC() {
        return teamRepository.orderByGamesDESC();
    }

    List<Team> orderByWinsASC() {
        return teamRepository.orderByWinsASC();
    }

    List<Team> orderByWinsDESC() {
        return teamRepository.orderByWinsDESC();
    }

    List<Team> orderByDrawsASC() {
        return teamRepository.orderByDrawsASC();
    }

    List<Team> orderByDrawsDESC() {
        return teamRepository.orderByDrawsDESC();
    }

    List<Team> orderByDefeatsASC() {
        return teamRepository.orderByDefeatsASC();
    }

    List<Team> orderByDefeatsDESC() {
        return teamRepository.orderByDefeatsDESC();
    }
}