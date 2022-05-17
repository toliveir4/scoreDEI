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
    TeamService teamService;

    @Autowired
    PlayerService playerService;

    @GetMapping("/")
    public String redirect() {
        return "redirect:/listPlayers";
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
                new Player("Darwin", "CF", "24-6-1999"),
                new Player("Taarabt", "CM", "24-3-1989"),
                new Player("Cristiano Ronaldo", "CF", "5-2-1985"),
                new Player("Pepe", "CB", "26-2-1983"),
                new Player("Ruben Dias", "CB", "14-5-1997"),
                new Player("Palhinha", "CDM", "9-7-1995")
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

        for (Player p : myPlayers)
            this.playerService.addPlayer(p);
    
		return "redirect:/listPlayers";
	}

    @GetMapping("/listPlayers")
    public String listPlayers(Model model) {
        model.addAttribute("players", this.playerService.getAllPlayers());
        return "listPlayers";
    }

    @GetMapping("/createPlayer")
    public String createPlayer(Model m) {
        m.addAttribute("player", new Player());
        m.addAttribute("allProfessors", this.teamService.getAllTeams());
        return "editPlayer";
    }

    @GetMapping("/editPlayer")
    public String editPlayer(@RequestParam(name="id", required=true) int id, Model m) {
        Optional<Player> op = this.playerService.getPlayer(id);
        if (op.isPresent()) {
            m.addAttribute("player", op.get());
            m.addAttribute("allTeams", this.teamService.getAllTeams());
            return "editPlayer";
        }
        else {
            return "redirect:/listPlayers";
        }
    }    

    @PostMapping("/savePlayer")
    public String savePlayer(@ModelAttribute Player st) {
        this.playerService.addPlayer(st);
        return "redirect:/listPlayers";
    }

    @GetMapping("/queryPlayers")
    public String queryStudent1(Model m) {
        m.addAttribute("person", new FormData());
        return "queryPlayers";
    }

    /* Note the invocation of a service method that is served by a query in jpql */
    @GetMapping("/queryResults")
    public String queryResult1(@ModelAttribute FormData data, Model m) {
        List<Player> ls = this.playerService.findByNameEndsWith(data.getName());
        m.addAttribute("players", ls);
        return "listPlayers";
    }

    @GetMapping("/listProfessors")
    public String listProfs(Model model) {
        model.addAttribute("professors", this.teamService.getAllTeams());
        return "listProfessors";
    }

    @GetMapping("/createProfessor")
    public String createProfessor(Model m) {
        m.addAttribute("professor", new Team());
        return "editProfessor";
    }

    private String getEditProfessorForm(int id, String formName, Model m) {
        Optional<Team> op = this.teamService.getProfessor(id);
        if (op.isPresent()) {
            m.addAttribute("professor", op.get());
            return formName;
        }
        return "redirect:/listProfessors";
    }

    @GetMapping("/editProfessor")
    public String editProfessor(@RequestParam(name="id", required=true) int id, Model m) {
        return getEditProfessorForm(id, "editProfessor", m);
    }    

    @PostMapping("/saveProfessor")
    public String saveProfessor(@ModelAttribute Team t) {
        this.teamService.addTeam(t);
        return "redirect:/listProfessors";
    }

}