package com.example.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Player {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name, position;
    private int age;
    @ManyToOne
    private Team team;
    
    public Player() {
    }
    
    public Player(String name, String position, int age) {
        this.name = name;
        this.position = position;
        this.age = age;
        this.team = new Team();
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getPosition() {
        return position;
    }
    
    @XmlElementWrapper(name = "teams")
    @XmlElement(name = "team")
    public Team getTeam() {
        return team;
    }
    
    public void setTeam(Team team) {
        this.team = team;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String toString() {
        return this.name + "(id = " + this.id + "). Position: " + this.position + ". Age: " + this.age;
    }
}
