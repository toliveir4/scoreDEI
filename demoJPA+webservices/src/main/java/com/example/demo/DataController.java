package com.example.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.example.data.Event;
import com.example.data.Match;
import com.example.data.Player;
import com.example.data.Team;
import com.example.data.WebUser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DataController {
    private final static Logger logger = LoggerFactory.getLogger(DataController.class);

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

    @GetMapping("/")
    public String redirect() {
        securityService.userDetailsService(userService.getAllUsers());
        return "redirect:/home";
    }

    @GetMapping("/admin")
    public String admin() {
        securityService.userDetailsService(userService.getAllUsers());
        return "redirect:/admin/hello";
    }

    @GetMapping("/listPlayers")
    public String listPlayers(Model model) {
        model.addAttribute("players", this.playerService.getAllPlayers());
        model.addAttribute("field", "name");
        model.addAttribute("order", "asc");
        return "listPlayers";
    }

    @GetMapping("orderPlayers")
    public String orderPlayers(Model m,
            @RequestParam(name = "field", required = true) String field,
            @RequestParam(name = "order", required = true) String order) {
        m.addAttribute("field", field);
        m.addAttribute("order", order.equals("asc") ? "desc" : "asc");

        if (order.equals("asc") && field.equals("name"))
            m.addAttribute("players", this.playerService.orderByNameASC());
        else if (order.equals("desc") && field.equals("name"))
            m.addAttribute("players", this.playerService.orderByNameDESC());
        else if (order.equals("asc") && field.equals("goals"))
            m.addAttribute("players", this.playerService.orderByGoalsASC());
        else if (order.equals("desc") && field.equals("goals"))
            m.addAttribute("players", this.playerService.orderByGoalsDESC());
        return "listPlayers";
    }

    @GetMapping("/listTeams")
    public String listTeams(Model m,
            @RequestParam(name = "field", required = false) String field,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {

        logger.info("ENTROU NO ORDER TEAMS");

        if (field == null) {
            m.addAttribute("teams", this.teamService.getAllTeams());
            return "listTeams";
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

        return "listTeams";
    }

    @GetMapping("/login")
    public String login() {
        securityService.userDetailsService(userService.getAllUsers());
        return "login";
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
            securityService.userDetailsService(u.getUsername(), u.getPassword(), u.getAdmin());

        } catch (Exception e) {
            m.addAttribute("error", "Username taken!");
            return signUp(m);
        }

        return "redirect:/login";
    }

    @GetMapping("/createMatch")
    private String createMatch(Model m) {
        m.addAttribute("match", new Match());
        m.addAttribute("allTeams", this.teamService.getAllTeams());
        return "createMatch";
    }

    @PostMapping("/saveMatch")
    private String saveMatch(@ModelAttribute @Valid Match match, Model m) {
        logger.info("SAVE MATCH!");
        Team home = match.getHome();
        Team away = match.getAway();
        if (home != null && away != null && !home.equals(away)) {
            match.setName(home.getName() + " vs " + away.getName());
            this.matchService.addMatch(match);
            return "redirect:/listMatches";
        }
        return "redirect:/createMatch";
    }

    @GetMapping("/listMatches")
    private String listMatches(Model model) {
        model.addAttribute("matches", this.matchService.getAllMatchs());
        return "listMatches";
    }

    @GetMapping("/match")
    private String match(@RequestParam(name = "id", required = true) int id, Model m) {
        Optional<Match> op = this.matchService.getMatch(id);
        if (op.isPresent()) {
            m.addAttribute("events", eventRepository.getEventsFromMatch(op.get()));
            m.addAttribute("match", op.get());
            return "match";
        }
        return "redirect:/listMatches";
    }

    @GetMapping("/createEvent")
    private String createEvent(@RequestParam(name = "id", required = true) int id, Model m) {
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
            return "redirect:/listMatches";
        }
    }

    @GetMapping("/saveEvent")
    private String saveEvent(@ModelAttribute Event event, Model m) {
        try {
            event.setTime(new Date());
            this.eventService.addEvent(event);
            if (event.getType() == 2) {
                // atualizar o numero de golos
            }

        } catch (Exception e) {
            return "redirect:/listMatches";
        }
        return "redirect:/match?id=" + event.getMatch().getId();
    }

}