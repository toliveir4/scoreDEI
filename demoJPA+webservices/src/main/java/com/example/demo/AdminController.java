package com.example.demo;

import org.apache.catalina.User;
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
    MatchRepository matchRepository;

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


    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/admin/loadData")
    public String loadData() throws UnirestException {
        logger.info("LOAD DATA FROM THE API!");

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
            
            Team check = this.teamService.findByName(tt.getName());

            if (check == null)
                this.teamService.addTeam(tt);
            else tt = check;

            String position = arr.get(0).getJSONObject("games").getString("position");
            
            Player newP = new Player(name, position, birth);
            this.playerService.addPlayer(newP);
            newP.setTeam(tt);
        }

        Team a = new Team("Liverpool", "a.png");
        Team b = new Team("Arsenal", "b.png");
        this.teamService.addTeam(a);
        this.teamService.addTeam(b);

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
                new Match("2-1", "22-12-2020 15:30:00", 0),
                new Match("2-3", "22-12-2020 12:30:00", 0),
        };
        match[0].setName("Liverpool vs Arsenal");
        match[1].setName("Everton vs Leicester");
        match[0].setHome(a);
        match[0].setAway(b);

        for (Match m : match)
            this.matchService.addMatch(m); 

        Event[] event = {
                new Event("title1", "info1", "86:02", 1),
                new Event("title2", "info2", "90:01", 1),
        };
        Player[] p = {
            new Player("Salah", "CF", "22-12-1992"),
            new Player("Diogo Jota", "CF", "22-12-1992"),
         };
         p[0].setTeam(a);
        for (Player e : p)
        this.playerService.addPlayer(e);

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
        return "admin/editTeam";
    }

    private String getEditTeamForm(int id, String formName, Model m) {
        Optional<Team> op = this.teamService.getTeam(id);
        if (op.isPresent()) {
            m.addAttribute("team", op.get());
            return formName;
        }
        return "redirect:/admin/listTeams";
    }

    @GetMapping("/editTeam")
    public String editTeam(@RequestParam(name = "id", required = true) int id, Model m) {
        return getEditTeamForm(id, "admin/editTeam", m);
    }

    @PostMapping("/saveTeam")
    public String saveTeam(@ModelAttribute Team t) {
        this.teamService.addTeam(t);
        return "redirect:/admin/listTeams";
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

        return "redirect:/admin/listUsers";
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

    @GetMapping("/admin/createMatch")
    private String createMatch(Model m) {
        m.addAttribute("match", new Match());
        m.addAttribute("allTeams", this.teamService.getAllTeams());
        return "createMatch";
    }

    @PostMapping("/admin/saveMatch")
    private String saveMatch(@ModelAttribute @Valid Match match, Model m) {
        logger.info("SAVE MATCH!");
        Team home = match.getHome();
        Team away = match.getAway();
        if (home != null && away != null && !home.equals(away)) {
            match.setName(home.getName() + " vs " + away.getName());
            this.matchService.addMatch(match);
            return "redirect:/admin/listMatches";
        }
        return "redirect:/admin/createMatch";
    }

    @GetMapping("/match")
    public String match(@RequestParam(name = "id", required = true) int id, Model m) {
        Optional<Match> op = this.matchService.getMatch(id);
        if (op.isPresent()) {
            m.addAttribute("events", eventRepository.getEventsFromMatch(op.get()));
            m.addAttribute("match", op.get());
            return "admin/match";
        } else {
            return "redirect:admin/listMatches";
        }
    }
    @GetMapping("/deleteMatch")
    //@Secured("ADMIN")
    public String deleteMatch(@RequestParam(name = "id", required = true) int id, Model ml) {
        Optional<Match> op = this.matchService.getMatch(id);
        if (op.isPresent()) {
            this.matchService.removeMatch(id);
        }
        return "redirect:/admin/listMatches";
    }
    @PostMapping("/changeRole")
    //@Secured("ADMIN")
    public String changeRole(@RequestParam(name = "id", required = true) int id, Model ml) {
        Optional<WebUser> op = this.userService.getUser(id);
        if (op.isPresent()) {
            this.userService.updateAdmin(id);
        }
        return "redirect:/admin/listUsers";
    }

    @GetMapping("/createEvent")
    public String createEvent(@RequestParam(name = "id", required = true) int id, Model m) {
        Optional<Match> op = this.matchService.getMatch(id);
        if (op.isPresent()) {

            Match match = op.get();
            Event event = new Event(match.getName(), "info1", "86:02", 1);
            event.setMatch(match); 
            m.addAttribute("event", event);
            List<Team> a = new ArrayList<Team>();
            a.add(this.teamService.getTeam(match.getHome().getId()).get());
            a.add(this.teamService.getTeam(match.getAway().getId()).get());
            m.addAttribute("allTeams", a);
            List<Player> p = new ArrayList<Player>();
            p.addAll(this.playerService.selectPlayersByTeam(match.getAway().getId()));
            p.addAll(this.playerService.selectPlayersByTeam(match.getHome().getId()));
            m.addAttribute("allPlayers", p);
           
            // saveEvent
            // this.eventService.addEvent(event);
            return "/admin/createEvent";
        } else {
            return "redirect:/admin/listMatches";
        }
    }

    @GetMapping("/saveEvent")
    private String saveEvent(@ModelAttribute Event event, Model m) {
        try {
            event.setTime(new Date());
         
            switch(event.getType()){
                case 1:{
                    //inicio do jogo
                    //System.out.println("aaaaa-a--aaa");
                   if(this.matchService.getMatch(event.getMatch().getId()).get().getStatus()!=1)
                         this.matchService.updateStatus(event.getMatch().getId(), 1);
                    else         
                        this.matchService.updateStatus(event.getMatch().getId(), 2);
                    



                    Match match=this.matchService.getMatch(event.getMatch().getId()).get();
                    if(match.getStatus()==1)
                    {
                        
                        if(match.getScoreAway()<match.getScoreHome())
                        {
                            this.teamRepository.addDefeat(match.getAway().getName());
                            this.teamRepository.addWin(match.getHome().getName());
                        }
                        if(event.getMatch().getScoreAway()>event.getMatch().getScoreHome())
                        {
                            this.teamRepository.addDefeat(match.getHome().getName());
                            this.teamRepository.addWin(match.getAway().getName());
                        }
                        if(event.getMatch().getScoreAway()==event.getMatch().getScoreHome())
                        {
                            this.teamRepository.addDraw(match.getAway().getName());
                            this.teamRepository.addDraw(match.getHome().getName());
                        }
                    }

                    if(event.getMatch().getStatus()==1)
                    {
                        this.teamRepository.addGame(event.getMatch().getAway().getName());
                        this.teamRepository.addGame(event.getMatch().getHome().getName());
                    }
                    break;
                }
                case 2:{
                     // 
                     
                        //adicionar golos ao match 
                        if(event.getMatch().getStatus()==1){
                            this.playerService.addGoal(event.getPlayer().getName());
                        if(event.getMatch().getHome().getId()==event.getTeam().getId()){
                             this.matchRepository.addHomeGoal(event.getMatch().getId());
                             this.matchRepository.updateScore(((event.getMatch().getScoreHome()+1)+"-"+event.getMatch().getScoreAway()),event.getMatch().getId());
                            }
                             
                        else{
                            
                            this.matchRepository.addAwayGoal(event.getMatch().getId());
                            this.matchRepository.updateScore((event.getMatch().getScoreHome()+"-"+(event.getMatch().getScoreAway()+1)),event.getMatch().getId());
                         
                        }
                    }
                        else {
                            this.matchRepository.deleteById(event.getMatch().getId());
                        }
                    
                      
                    break;}
                case 3:{
                     // 
                        this.playerService.addYellowCard(event.getPlayer().getName());
                    break;}
                case 4:{
                        this.playerService.addRedCard(event.getPlayer().getName());
                    break;}
                case 5:{
                        this.matchService.updateStatus(event.getMatch().getId(), 5);
                    break;}
                case 6:{
                       this.matchService.updateStatus(event.getMatch().getId(), 6);
                    break;}
                    
            }
            this.eventService.addEvent(event);
        } catch (Exception e) {
            return "redirect:/admin/listMatches";
        }
        return "redirect:admin/match?id=" + event.getMatch().getId();
    }
}
