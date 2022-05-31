package com.example.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String score, name;
    private Date date;
    private int status;
    private int scoreHome, scoreAway;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Event> events;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Team> teams;

    public Match() {
    }

    public Match(String name, String score, String date, int status) {
        this.name = name;
        this.score = score;
        this.status = status;

        try {
            String[] a = score.split("-");
            scoreHome = Integer.parseInt(a[0]);
            scoreAway = Integer.parseInt(a[1]);
        } catch (Exception e) {
        }

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm");
            this.date = formatter.parse(date);
        } catch (Exception e) {
        }

        this.teams = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScoreHome() {
        return scoreHome;
    }

    public void setScoreHome(int scoreHome) {
        this.scoreHome = scoreHome;
    }

    public int getScoreAway() {
        return scoreAway;
    }

    public void setScoreAway(int scoreAway) {
        this.scoreAway = scoreAway;
    }

    // TODO verificar se a data e valida (?)
    public String getDate() {
        SimpleDateFormat d = new SimpleDateFormat("dd-mm-yyyy HH:mm");
        return d.format(date);
    }

    public void setDate(String date) {
        SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yyyy HH:mm");
        Date d;
        try {
            d = dt.parse(date);
            this.date = d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @XmlElementWrapper(name = "teams")
    @XmlElement(name = "team")
    public List<Team> getTeams() {
        return teams;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public String toString() {
        return this.name + "(id = " + this.id + "). Telephone: " + this.score;
    }
}
