package com.example.data;

<<<<<<< HEAD
import lombok.Getter;
import lombok.Setter;

=======
>>>>>>> 4c1ce3040022223ced12d93d7364bd7230607448
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
<<<<<<< HEAD
    private int scoreHome, scoreAway;
    // private List<Event> events;
=======
    private int scoreA, scoreB;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Event> events;
>>>>>>> 4c1ce3040022223ced12d93d7364bd7230607448

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Team> teams;

    public Match() {
    }

<<<<<<< HEAD
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
=======
    public Match(String name, String resultado, String date, int status, String score) {
        this.name = name;
        this.resultado = resultado;

        SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yyyy");
        Date d;
        try {
            d = dt.parse(date);
            this.date = d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        this.status = status;

        String[] s = score.split("-");
        this.scoreA = Integer.parseInt(s[0]);
        this.scoreB = Integer.parseInt(s[1]);
>>>>>>> 4c1ce3040022223ced12d93d7364bd7230607448
        this.teams = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
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

    public int getScoreA() {
        return scoreA;
    }

    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }

    // TODO verificar se a data e valida (?)
    public String getDate() {
        SimpleDateFormat d = new SimpleDateFormat("dd-mm-yyyy");
        return d.format(date);
    }

    public void setDate(String date) {
        SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yyyy");
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
        return this.name + "(id = " + this.id + "). Telephone: " + this.resultado;
    }
}
