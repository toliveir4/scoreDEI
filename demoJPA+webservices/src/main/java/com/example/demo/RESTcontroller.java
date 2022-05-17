package com.example.demo;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import com.example.data.Team;
import com.example.data.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("rest")
public class RESTcontroller {
    @Autowired
    TeamService profService;

    @Autowired
    PlayerService playerService;

    @GetMapping(value = "professors", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Team> getProfessors()
    {
        return profService.getAllTeams();
    }

    @GetMapping(value = "students", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Player> getPlayers()
    {
        return playerService.getAllPlayers();
    }

    @GetMapping(value = "students/{id}", produces = {MediaType.APPLICATION_XML_VALUE})
    public Player getPlayer(@PathVariable("id") int id) {
        Optional<Player> op = playerService.getPlayer(id);
        if (op.isEmpty())
            return null;
        return op.get();
    }

    @GetMapping(value = "professors/{id}", produces = {MediaType.APPLICATION_XML_VALUE})
    public Team getProfessor(@PathVariable("id") int id) {
        Optional<Team> op = profService.getProfessor(id);
        if (op.isEmpty())
            return null;
        return op.get();
    }

    @PostMapping(value = "professors", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void addTeam(@RequestBody Team t) {
        profService.addTeam(t);
    }

    @PostMapping(value = "students", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void addPlayer(@RequestBody Player s) {
        playerService.addPlayer(s);
    }

    @PutMapping(value = "professors/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void addTeam(@PathVariable("id") int id, @RequestBody Team t) {
        Optional<Team> op = profService.getProfessor(id);
        if (!op.isEmpty()) {
            Team t1 = op.get();
            t1.setName(t.getName());
            profService.addTeam(t1);
        }
    }

    @PutMapping(value = "students/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void addPlayer(@PathVariable("id") int id, @RequestBody Player s) {
        System.out.println("PUT called");
        Optional<Player> op = playerService.getPlayer(id);
        if (!op.isEmpty()) {
            Player s1 = op.get();
            s1.setName(s.getName());
            s1.setBirthday(s.getBirthday());
            s1.setPosition(s.getPosition());
            playerService.addPlayer(op.get());
        }
    }
}
