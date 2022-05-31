package com.example.demo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.data.Player;
import com.example.data.Team;

public interface PlayerRepository extends CrudRepository<Player, Integer> {
    @Query("select p from Player p where p.name like %?1")
    List<Player> findByNameEndsWith(String chars);

    @Modifying
    @Query("select p from Player p where p.name like :name")
    void deletePlayer(String name);

    @Transactional
    @Modifying
    @Query("update Player p set p.goals = p.goals + 1 where p.name like :name")
    void addGoal(String name);

    @Transactional
    @Modifying
    @Query("update Player p set p.assists = p.assists + 1 where p.name like :name")
    void addAssist(String name);

    @Transactional
    @Modifying
    @Query("update Player p set p.yellows = p.yellows + 1 where p.name like :name")
    void addYellow(String name);

    @Transactional
    @Modifying
    @Query("update Player p set p.reds = p.reds + 1 where p.name like :name")
    void addRed(String name);

    @Transactional
    @Modifying
    @Query("update Player p set p.team = :team where p.name like :name")
    void updateTeam(Team team, String name);

    @Query("select p from Player p order by p.name asc")
    List<Player> orderByNameASC();

    @Query("select p from Player p order by p.name desc")
    List<Player> orderByNameDESC();

    @Query("select p from Player p order by p.goals asc")
    List<Player> orderByGoalsASC();

    @Query("select p from Player p order by p.goals desc")
    List<Player> orderByGoalsDESC();

    @Query("select p from Player p where p.team.name = :t")
    List<Player> selectPlayersByTeam(String t);

}    