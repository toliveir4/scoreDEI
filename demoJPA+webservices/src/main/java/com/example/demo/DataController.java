package com.example.demo;

import java.util.List;
import java.util.Optional;

import com.example.data.Team;
import com.example.data.Player;
import com.example.data.WebUser;
import com.example.formdata.FormData;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.WebUserService;


@Controller
public class DataController {

    @Autowired
    TeamService teamService;

    @Autowired
    PlayerService playerService;

    @Autowired
    WebUserService userService;

    @GetMapping("/")
    public String redirect() {
        return "redirect:/home";
    }

    @GetMapping("/createData")
    public String createData() {
        return "createData";
    }

    @PostMapping("/saveData")
	public String saveData(Model model) {
        Team[] myTeams = {
                new Team("Benfica", 34, 34, 0, 0),
                new Team("Porto", 34, 0, 0, 35),
                new Team("Sporting", 34, 0, 34, 0)
        };

        WebUser[] users = {
           
            new WebUser("user","pass"),
            new WebUser("tmatos","adeus"),
            new WebUser("user1","pass1"),
            new WebUser("user2","pass2"),
            new WebUser("user3","pass3"),
        };

        for (WebUser u : users)
            this.userService.addUser(u);



        
        Player[] myPlayers = {
                new Player("Darwin","CF", "24-6-1999"),
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
        m.addAttribute("player", new FormData());
        return "queryPlayers";
    }

    /* Note the invocation of a service method that is served by a query in jpql */
    @GetMapping("/queryResults")
    public String queryResult1(@ModelAttribute FormData data, Model m) {
        List<Player> ls = this.playerService.findByNameEndsWith(data.getName());
        m.addAttribute("players", ls);
        return "listPlayers";
    }

    @GetMapping("/listTeams")
    public String listTeams(Model model) {
        model.addAttribute("teams", this.teamService.getAllTeams());
        return "listTeams";
    }

    @GetMapping("/createTeam")
    public String createTeam(Model m) {
        m.addAttribute("team", new Team());
        return "editTeam";
    }

    private String getEditTeamForm(int id, String formName, Model m) {
        Optional<Team> op = this.teamService.getTeam(id);
        if (op.isPresent()) {
            m.addAttribute("team", op.get());
            return formName;
        }
        return "redirect:/listTeams";
    }

    @GetMapping("/editTeam")
    public String editTeam(@RequestParam(name="id", required=true) int id, Model m) {
        return getEditTeamForm(id, "editTeam", m);
    }    

    @PostMapping("/saveTeam")
    public String saveTeam(@ModelAttribute Team t) {
        this.teamService.addTeam(t);
        return "redirect:/listTeams";
    }
    
}