package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "admin")
public class AdminController {
    @Autowired
    TeamService teamService;

    @Autowired
    PlayerService playerService;

    @Autowired
    WebUserService userService;

    @Autowired
    WebSecurityConfig securityService;

    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/admin")
    public String redirect() {
        securityService.userDetailsService(userService.getAllUsers());
        return "redirect:admin/home";
    }

    @GetMapping("/listPlayers")
    public String listPlayers(Model model) {
        model.addAttribute("players", this.playerService.getAllPlayers());
        return "admin/listPlayers";
    }
}
