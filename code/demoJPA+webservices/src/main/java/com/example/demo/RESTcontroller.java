package com.example.demo;

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
    PlayerService playerService;

    @Autowired
    TeamService teamService;

    @GetMapping(value = "team", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Team getTeam()
    {
        return playerService.getTeam();
    }

    @GetMapping(value = "players", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Player> getPlayers()
    {
        return teamService.getAllPlayers();
    }

    @GetMapping(value = "players/{id}", produces = {MediaType.APPLICATION_XML_VALUE})
    public Player getPlayer(@PathVariable("id") int id) {
        Optional<Player> op = teamService.getPlayer(id);
        if (op.isEmpty())
            return null;
        return op.get();
    }

    /*@GetMapping(value = "team/{id}", produces = {MediaType.APPLICATION_XML_VALUE})
    public Team getTeam2(@PathVariable("id") int id) {
        Optional<Team> op = playerService.getTeam2(id);
        if (op.isEmpty())
            return null;
        return op.get();
    }*/

    @PostMapping(value = "professors", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void setTeam(@RequestBody Team t) {
        playerService.setTeam(t);
    }

    @PostMapping(value = "players", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void addPlayer(@RequestBody Player p) {
        teamService.addPlayer(p);
    }

   /* @PutMapping(value = "professors/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void setTeam(@PathVariable("id") int id, @RequestBody Team p) {
        Optional<Team> op = playerService.getTeam2(id);
        if (!op.isEmpty()) {
            Team p1 = op.get();
            p1.setName(p.getName());
        }
    }*/

    @PutMapping(value = "players/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void addPlayer(@PathVariable("id") int id, @RequestBody Player s) {
        System.out.println("PUT called");
        Optional<Player> op = teamService.getPlayer(id);
        if (!op.isEmpty()) {
            Player s1 = op.get();
            s1.setName(s.getName());
            s1.setAge(s.getAge());
            s1.setPosition(s.getPosition());
            teamService.addPlayer(op.get());
        }
    }
}
