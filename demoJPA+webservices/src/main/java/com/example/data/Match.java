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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String resultado, name;
    private Date date;
    private int status;
    private int scoreHome, scoreAway;
    // private List<Event> events;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Team> teams;

    public Match() {
    }

    public Match(String name, String resultado, String date) {
        this.name = name;
        this.resultado = resultado;
       try{
        String [] a=resultado.split("-");
        scoreHome=Integer.parseInt(a[0]);
        scoreAway=Integer.parseInt(a[1]);
       }
       catch(Exception e){

       }
        try{
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm");
        this.date = formatter.parse(date);}
        catch(Exception e){

        }
        this.teams = new ArrayList<>();
    }

    @XmlElementWrapper(name = "teams")
    @XmlElement(name = "team")
    public List<Team> getteams() {
        return teams;
    }

    public void addteam(Team team) {
        this.teams.add(team);
    }

    public String toString() {
        return this.name + "(id = " + this.id + "). Telephone: " + this.resultado;
    }
}
