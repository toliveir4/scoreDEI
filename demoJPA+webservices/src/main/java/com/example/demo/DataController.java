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

    @GetMapping("/loadData")
    public String loadData() throws UnirestException {
        String URL = "https://v3.football.api-sports.io/";
        // Premier League 2021
        String ENDPOINT = "teams?league=39&season=2021";
        String API_KEY = "6dbe15bb3475e04f108b39b89b365672";

        Unirest.setTimeouts(0, 0);
        HttpResponse<JsonNode> response = Unirest.get(URL + ENDPOINT)
                .header("x-rapidapi-key", API_KEY)
                .header("x-rapidapi-host", "v3.football.api-sports.io")
                .asJson();

        return "redirect:/hello";
    }

    @PostMapping("/saveData")
    public String saveData(Model model) {

        Team[] myTeams = {
                new Team("Benfica", 34, 34, 0, 0),
                new Team("Porto", 34, 0, 0, 35),
                new Team("Sporting", 34, 0, 34, 0)
        };

        WebUser[] users = {

                new WebUser("user", "pass"),
                new WebUser("tmatos", "adeus"),
                new WebUser("user1", "pass1"),
                new WebUser("user2", "pass2"),
                new WebUser("user3", "pass3"),
        };

        for (WebUser u : users) {
            try {
                this.userService.addUser(u);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        Player[] myPlayers = {
                new Player("Darwin", "CF", "24-6-1999"),
                new Player("Taarabt", "CM", "24-3-1989"),
                new Player("Cristiano Ronaldo", "CF", "5-2-1985"),
                new Player("Pepe", "CB", "26-2-1983"),
                new Player("Ruben Dias", "CB", "14-5-1997"),
                new Player("Palhinha", "CDM", "9-7-1995")
        };

        myPlayers[0].setTeam(myTeams[1]);
        myPlayers[1].setTeam(myTeams[2]);
        myPlayers[2].setTeam(myTeams[0]);
        myPlayers[3].setTeam(myTeams[2]);
        myPlayers[4].setTeam(myTeams[1]);
        myPlayers[5].setTeam(myTeams[0]);

        for (Team t : myTeams)
            t.setBestScorer(myPlayers[2].getName());

        for (Player p : myPlayers)
            this.playerService.addPlayer(p);

        Match[] match = {
                new Match("user vs a", "2-1", "22-12-2020 15:30", 1),
                new Match("tmatos vs user", "2-3", "22-12-2020 12:30", 1),
        };

        for (Match m : match)
            this.matchService.addMatch(m);

        Event[] event = {
                new Event("title1", "info1", "86:02", 1),
                new Event("title2", "info2", "90:01", 1),
        };

        for (Event e : event)
            this.eventService.addEvent(e);

        return "redirect:/listPlayers";
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

    @GetMapping("/createPlayer")
    public String createPlayer(Model m) {
        m.addAttribute("player", new Player());
        m.addAttribute("allTeams", this.teamService.getAllTeams());
        return "editPlayer";
    }

    @GetMapping("/editPlayer")
    public String editPlayer(@RequestParam(name = "id", required = true) int id, Model m) {
        Optional<Player> op = this.playerService.getPlayer(id);
        if (op.isPresent()) {
            m.addAttribute("player", op.get());
            m.addAttribute("allTeams", this.teamService.getAllTeams());
            return "editPlayer";
        } else {
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
            securityService.userDetailsService(u.getUsername(), u.getPassword());

        } catch (Exception e) {
            m.addAttribute("error", "Username taken!");
            return signUp(m);
        }

        return "redirect:/login";
    }

    @GetMapping("/listUsers")
    public String listUsers(Model model) {
        model.addAttribute("web_user", this.userService.getAllUsers());
        return "listUsers";
    }

    @GetMapping("/listMatches")
    public String listMatches(Model model) {
        model.addAttribute("matches", this.matchService.getAllMatchs());
        return "listMatches";
    }

    @GetMapping("/match")
    public String match(@RequestParam(name = "id", required = true) int id, Model m) {
        Optional<Match> op = this.matchService.getMatch(id);
        if (op.isPresent()) {
            m.addAttribute("events", eventRepository.getEventsFromMatch(op.get()));
            m.addAttribute("match", op.get());
            return "match";
        } else {
            return "redirect:/listMatches";
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
            return "redirect:/listMatches";
        }
    }

    @GetMapping("/saveEvent")
    public String saveEvent(@ModelAttribute Event event, Model m) {
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