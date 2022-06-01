package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import javax.validation.Valid;

import com.example.data.Player;
import com.example.data.Team;

import com.example.data.Match;
import com.example.data.WebUser;
import com.example.data.Event;
import java.util.Date;

import com.example.formdata.FormData;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {
    @Autowired
    TeamService teamService;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MatchService matchService;

    @Autowired
    PlayerService playerService;

    @Autowired
    WebUserService userService;

    @Autowired
    EventService eventService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    WebSecurityConfig securityService;


    //private final static Logger logger = LoggerFactory.getLogger(AdminController.class);
  
    @GetMapping("home")
    public String home(Model model) {
        model.addAttribute("players", this.playerService.getAllPlayers());
        model.addAttribute("field", "name");
        model.addAttribute("order", "asc");
        return "admin/home";
    }
    
    @GetMapping("/listPlayers")
    //@Secured("ADMIN")
    public String listPlayers(Model model) {
        model.addAttribute("players", this.playerService.getAllPlayers());
        model.addAttribute("field", "name");
        model.addAttribute("order", "asc");
        return "admin/listPlayers";
    }

    @GetMapping("/login")
    public String adminlogin() {
        securityService.userDetailsService(userService.getAllUsers());
        return "admin/login";
    }
    @GetMapping("/listTeams")
    public String listTeams(Model m,
    @RequestParam(name = "field", required = false) String field,
    @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {

        if (field == null) {
            m.addAttribute("teams", this.teamService.getAllTeams());
            return "admin/listTeams";
        }

        order = order.equals("asc") ? "desc" : "asc";
        m.addAttribute("field", field);
        m.addAttribute("order", order);

        if (order.equals("asc") && field.equals("games"))
            m.addAttribute("teams", this.teamService.orderByGamesASC());
        else if (order.equals("desc") && field.equals("games"))
            m.addAttribute("teams", this.teamService.orderByGamesDESC());
        else if (order.equals("asc") && field.equals("wins"))
            m.addAttribute("teams", this.teamService.orderByWinsASC());
        else if (order.equals("desc") && field.equals("wins"))
            m.addAttribute("teams", this.teamService.orderByWinsDESC());
        else if (order.equals("asc") && field.equals("draws"))
            m.addAttribute("teams", this.teamService.orderByDrawsASC());
        else if (order.equals("desc") && field.equals("draws"))
            m.addAttribute("teams", this.teamService.orderByDrawsDESC());
        else if (order.equals("asc") && field.equals("defeats"))
            m.addAttribute("teams", this.teamService.orderByDefeatsASC());
        else if (order.equals("desc") && field.equals("defeats"))
            m.addAttribute("teams", this.teamService.orderByDefeatsDESC());

        return "admin/listTeams";
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
    public String editTeam(@RequestParam(name = "id", required = true) int id, Model m) {
        return getEditTeamForm(id, "editTeam", m);
    }

    @PostMapping("/saveTeam")
    public String saveTeam(@ModelAttribute Team t) {
        this.teamService.addTeam(t);
        return "redirect:/listTeams";
    }

   

    @GetMapping("/signup")
    private String signUp(Model m) {
        m.addAttribute("web_user", new WebUser());
        return "signup";
    }

    @PostMapping("/saveUser")
    private String saveUser(@ModelAttribute @Valid WebUser u, Model m) {
        try {
            userService.addUser(u);
            securityService.userDetailsService(u.getUsername(), u.getPassword());

        } catch (Exception e) {
            m.addAttribute("error", "Username taken!");
            return signUp(m);
        }

        return "redirect:/listUsers";
    }

    @GetMapping("/listUsers")
    public String listUsers(Model model) {
        model.addAttribute("web_user", this.userService.getAllUsers());
        return "admin/listUsers";
    }

    @GetMapping("/listMatches")
    public String listMatches(Model model) {
        model.addAttribute("matches", this.matchService.getAllMatchs());
        return "admin/listMatches";
    }

    @GetMapping("/match")
    public String match(@RequestParam(name = "id", required = true) int id, Model m) {
        Optional<Match> op = this.matchService.getMatch(id);
        if (op.isPresent()) {
            m.addAttribute("events", eventRepository.getEventsFromMatch(op.get()));
            m.addAttribute("match", op.get());
            return "match";
        } else {
            return "redirect:admin//listMatches";
        }
    }

    @GetMapping("/createEvent")
    public String createEvent(@RequestParam(name = "id", required = true) int id, Model m) {
        Optional<Match> op = this.matchService.getMatch(id);
        if (op.isPresent()) {

            Match match = op.get();
            Event event = new Event(match.getName(), "info1", "86:02", 1);
            event.setMatch(match);
            m.addAttribute("event", event);
            m.addAttribute("allTeams", this.teamService.getAllTeams());
            m.addAttribute("allPlayers", this.playerService.getAllPlayers());
            // saveEvent
            // this.eventService.addEvent(event);
            return "createEvent";
        } else {
            return "redirect:admin/listMatches";
        }
    }

    @GetMapping("/saveEvent")
    public String saveEvent(@ModelAttribute Event event, Model m) {
        try {
            event.setTime(new Date());
            this.eventService.addEvent(event);
            switch(event.getType()){
                case 1:{

                    //inicio do jogo
                    break;
                }
                case 2:{
                     // 
                    break;}
                case 3:{
                     // 
                    break;}
                case 4:{
                     // 
                    break;}
                case 5:{
                     // 
                    break;}
                case 6:{
                     // 
                    break;}
            }
            

        } catch (Exception e) {
            return "redirect:admin/listMatches";
        }
        return "redirect:admin/match?id=" + event.getMatch().getId();
    }


}
