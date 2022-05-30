package com.example.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    // 1-Inicio e Fim de Jogo
    // 2-Golo
    // 3-Cartao Amarelo
    // 4-Cartao Vermelho
    // 5-Jogo interrompido
    // 6-Jogo resumido
    private int type;
    private String title;
    private String info;
    private Date time;

    @ManyToOne(cascade = CascadeType.ALL)
    private Match match;

    // redundancia para facilitar a pesquisa
    @ManyToOne(cascade = CascadeType.ALL)
    private Team team;

    @ManyToOne(cascade = CascadeType.ALL)
    private Player player;

    @ManyToOne(cascade = CascadeType.ALL)
    private WebUser userEvent;

    // verificacao
    @ManyToOne(cascade = CascadeType.ALL)
    private WebUser userApprovel;

    private boolean aproved;

    public Event() {
    }

    public Event(String title, String info, String time, int type) {
        this.title = title;
        this.info = info;
        setTime(time);
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTime() {
        SimpleDateFormat d = new SimpleDateFormat("mm:ss");
        return d.format(time);
    }

    public void setTime(String time) {
        SimpleDateFormat dt = new SimpleDateFormat("mm:ss");
        Date d;
        try {
            d = dt.parse(time);
            this.time = d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean isAproved() {
        return aproved;
    }

    public void setAproved(boolean aproved) {
        this.aproved = aproved;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public WebUser getUserEvent() {
        return userEvent;
    }

    public void setUserEvent(WebUser userEvent) {
        this.userEvent = userEvent;
    }

    public WebUser getUserApprovel() {
        return userApprovel;
    }

    public void setUserApprovel(WebUser userApprovel) {
        this.userApprovel = userApprovel;
    }

    public String toString() {
        return this.info + "(id = " + this.id + ")";
    }
}
