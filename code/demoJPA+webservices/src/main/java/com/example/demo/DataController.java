package com.example.demo;

import java.util.List;
import java.util.Optional;

import com.example.data.Team;
import com.example.data.Player;
import com.example.formdata.FormData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class DataController {
    @Autowired
    PlayerService playerService;

    @Autowired
    TeamService teamService;

    @GetMapping("/")
    public String redirect() {
        return "redirect:/listTeamPlayers";
    }

    @GetMapping("/createData")
    public String createData() {
        return "createData";
    }

	@PostMapping("/saveData")
	public String saveData(Model model) {
        Team[] myTeams = { 
            new Team("Benfica"), 
            new Team("Porto"), 
            new Team("Sporting")
        };
        Player[] myPlayers = { 
            new Player("Darwin", "LW", 23),
            new Player("Taarabt", "CM", 32),
            new Player("Cristiano Ronaldo", "ST", 37),
            new Player("Pepe", "CB", 39),
            new Player("Ruben Dias", "CB", 24),
            new Player("Palhinha", "MC", 26)
        };

        myPlayers[0].setTeam(myTeams[0]);
        myPlayers[0].setTeam(myTeams[1]);
        myPlayers[1].setTeam(myTeams[1]);
        myPlayers[1].setTeam(myTeams[2]);
        myPlayers[2].setTeam(myTeams[0]);
        myPlayers[3].setTeam(myTeams[2]);
        myPlayers[4].setTeam(myTeams[1]);
        myPlayers[5].setTeam(myTeams[0]);
        myPlayers[5].setTeam(myTeams[1]);
        myPlayers[5].setTeam(myTeams[2]);

        for (Player s : myPlayers)
            this.teamService.addPlayer(s);
    
		return "redirect:/listTeamPlayers";
	}

    @GetMapping("/listTeamPlayers")
    public String listTeamPlayers(Model model) {
        model.addAttribute("players", this.teamService.getAllPlayers());
        return "listTeamPlayers";
    }

    @GetMapping("/createPlayer")
    public String createPlayer(Model m) {
        m.addAttribute("player", new Player());
        m.addAttribute("allPlayers", this.playerService.getTeam());
        return "editPlayer";
    }

    @GetMapping("/editPlayer")
    public String editPlayer(@RequestParam(name="id", required=true) int id, Model m) {
        Optional<Player> op = this.teamService.getPlayer(id);
        if (op.isPresent()) {
            m.addAttribute("player", op.get());
            m.addAttribute("allPlayers", this.playerService.getTeam());
            return "editPlayer";
        }
        else {
            return "redirect:/listTeamPlayers";
        }
    }    

    @PostMapping("/savePlayer")
    public String saveplayer(@ModelAttribute Player p) {
        this.teamService.addPlayer(p);
        return "redirect:/listTeamPlayers";
    }

    @GetMapping("/queryPlayers")
    public String queryPlayer1(Model m) {
        m.addAttribute("person", new FormData());
        return "queryPlayers";
    }

    /* Note the invocation of a service method that is served by a query in jpql */
    @GetMapping("/queryResults")
    public String queryResult1(@ModelAttribute FormData data, Model m) {
        List<Player> ls = this.teamService.findByNameEndsWith(data.getName());
        m.addAttribute("players", ls);
        return "listTeamPlayers";
    }

    /*@GetMapping("/listTeams")
    public String listProfs(Model model) {
        model.addAttribute("team", this.playerService.getTeam());
        return "listTeams";
    }*/

    @GetMapping("/createTeam")
    public String createTeam(Model m) {
        m.addAttribute("team", new Team());
        return "editTeam";
    }

    /*private String getEditTeamForm(String formName, Model m) {
        Team op = this.playerService.getTeam();
        if (op != NULL) {
            m.addAttribute("team", op);
            return formName;
        }
        return "redirect:/listTeams";
    }

    @GetMapping("/editTeam")
    public String editTeam(@RequestParam(name="id", required=true) int id, Model m) {
        return getEditTeamForm("editTeam", m);
    }*/ 

    @PostMapping("/saveTeam")
    public String saveTeam(@ModelAttribute Team team) {
        this.playerService.setTeam(team);
        return "redirect:/listTeams";
    }

}