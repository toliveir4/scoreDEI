package com.example.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.Date;
import java.util.List;

@Entity
@XmlRootElement
public class Player {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int goals, assists, yellows, reds;
    private String name, position;
    private Date birthday;

    @ManyToOne(cascade = CascadeType.ALL)
    private Team team;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Event> events;

    public Player() {
    }
    
    public Player(String name, String position, String birthday) {
        this.name = name;
        this.position = position;
        SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yyyy");
        Date date;
        try {
            date = dt.parse(birthday);
            this.birthday = date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getBirthday() {
        SimpleDateFormat d = new SimpleDateFormat("dd-mm-yyyy");
        return d.format(birthday);
    }

    public void setBirthday(String birthday) {
        SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yyyy");
        Date date;
        try {
            date = dt.parse(birthday);
            this.birthday = date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getYellows() {
        return yellows;
    }

    public void setYellows(int yellows) {
        this.yellows = yellows;
    }

    public int getReds() {
        return reds;
    }

    public void setReds(int reds) {
        this.reds = reds;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @XmlElementWrapper(name = "teams")
    @XmlElement(name = "team")
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String toString() {
        return this.name + "(id = " + this.id + "). position: " + this.position + ". birthday: " + getBirthday();
    }
}