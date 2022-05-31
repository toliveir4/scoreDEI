package com.example.data;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({ "players" })
@XmlRootElement
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    // private File img;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> players;
    private int wins, draws, defeats, games;

    private String bestScorer;

    public Team() {
    }

    public Team(String name, int games, int wins, int draws, int defeats) {
        this.name = name;
        this.games = games;
        this.wins = wins;
        this.draws = draws;
        this.defeats = defeats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getDefeats() {
        return defeats;
    }

    public void setDefeats(int defeats) {
        this.defeats = defeats;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getBestScorer() {
        return bestScorer;
    }

    public void setBestScorer(String bestScorer) {
        this.bestScorer = bestScorer;
    }

    public String toString() {
        return this.name + " (id = " + this.id + ").";
    }
}