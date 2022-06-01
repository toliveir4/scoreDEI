package com.example.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

    @ManyToOne(cascade = CascadeType.ALL)
    private Team Home;

    @ManyToOne(cascade = CascadeType.ALL)
    private Team Away;
    
    public Match() {
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date d;
        try {
            d = dt.parse("01-06-2022 20:00:00");
            this.date = d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Match(String score, String date, int status) {
        this.score = score;
        this.status = status;

        try {
            String[] a = score.split("-");
            scoreHome = Integer.parseInt(a[0]);
            scoreAway = Integer.parseInt(a[1]);
        } catch (Exception e) {
        }

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            this.date = formatter.parse(date);
        } catch (Exception e) {
        }
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
        SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return d.format(date);
    }

    public void setDate(String date) {
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date d;
        try {
            d = dt.parse(date);
            this.date = d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Team getHome() {
        return Home;
    }

    public void setHome(Team home) {
        this.Home = home;
    }
    
    public Team getAway() {
        return Away;
    }

    public void setAway(Team away) {
        this.Away = away;
    }

    public String toString() {
        return this.name + "(id = " + this.id + "). Telephone: " + this.score;
    }
}
