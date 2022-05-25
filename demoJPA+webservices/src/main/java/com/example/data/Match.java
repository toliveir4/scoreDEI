package com.example.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
@Entity
@XmlRootElement
public class Match {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String resultado, name;
    private Date date;
    private int status; 
    private int scoreA,scoreB;
    //private List<Event> events;
    
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Team> teams;
  
    public Match() {
    }
    
    public Match(String name, String resultado, int age) {
        this.name = name;
        this.resultado = resultado;
        this.teams = new ArrayList<>();
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getResultado() {
        return resultado;
    }
    
    @XmlElementWrapper(name = "teamessors")
    @XmlElement(name = "team")
    public List<Team> getteams() {
        return teams;
    }
    
    public void setteams(List<Team> teams) {
        this.teams = teams;
    }

    public void addteam(Team team) {
        this.teams.add(team);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResualtado(String resultado) {
        this.resultado = resultado;
    }

    public String toString() {
        return this.name + "(id = " + this.id + "). Telephone: " + this.resultado ;
    }
}
