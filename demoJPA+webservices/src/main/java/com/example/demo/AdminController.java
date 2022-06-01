package com.example.demo;

import org.json.JSONArray;
import org.json.JSONObject;
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

    @GetMapping("/loadData")
    public String loadData() throws UnirestException {
        String URL = "https://v3.football.api-sports.io/";
        // Premier League 2021
        String ENDPOINT = "players?league=39&season=2021";
        String API_KEY = "6dbe15bb3475e04f108b39b89b365672";

        HttpResponse<JsonNode> responsePlayers = Unirest.get(URL + ENDPOINT)
                .header("x-rapidapi-key", API_KEY)
                .header("x-rapidapi-host", "v3.football.api-sports.io")
                .asJson();

        JSONArray playersBody = responsePlayers.getBody().getObject().getJSONArray("response");

        ArrayList<JSONObject> arrays = new ArrayList<JSONObject>();
        for (int i = 0; i < playersBody.length(); i++)
            arrays.add(playersBody.getJSONObject(i));
        
        for (int p = 0; p < arrays.size(); p++) {
            String name = arrays.get(p).getJSONObject("player").getString("name");
            String birth = arrays.get(p).getJSONObject("player").getJSONObject("birth").optString("date", "2001-10-14");

            JSONArray pArr = arrays.get(p).optJSONArray("statistics");
            ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
            arr.add(pArr.getJSONObject(0));

            
            Team tt = new Team(arr.get(0).getJSONObject("team").getString("name"), 
                    arr.get(0).getJSONObject("team").getString("logo"));
            
            List<Team> check = this.teamService.findByName(tt.getName());

            if (check.isEmpty())
                this.teamService.addTeam(tt);
            else tt = check.get(0);

            String position = arr.get(0).getJSONObject("games").getString("position");
            
            Player newP = new Player(name, position, birth);
            this.playerService.addPlayer(newP);
            newP.setTeam(tt);
        }

        // TODO isto vai saltar fora, apenas está a servir como ajuda
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
                e.printStackTrace();
            }
        }

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

        return "redirect:/admin/listPlayers";
    }
  
    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("players", this.playerService.getAllPlayers());
        model.addAttribute("field", "name");
        model.addAttribute("order", "asc");
        return "admin/hello";
    }
    
    @GetMapping("/listPlayers")
    //@Secured("ADMIN")
    public String listPlayers(Model model) {
        model.addAttribute("players", this.playerService.getAllPlayers());
        model.addAttribute("field", "name");
        model.addAttribute("order", "asc");
        return "admin/listPlayers";
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

   

    @GetMapping("/createUser")
    private String signUp(Model m) {
        m.addAttribute("web_user", new WebUser());
        return "admin/createUser";
    }

    @PostMapping("/saveUser")
    private String saveUser(@ModelAttribute @Valid WebUser u, Model m) {
        try {
            userService.addUser(u);
            securityService.userDetailsService(u.getUsername(), u.getPassword(),u.getAdmin());

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
            return "redirect:admin/listMatches";
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
