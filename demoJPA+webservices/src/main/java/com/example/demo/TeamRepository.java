package com.example.demo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.data.Team;

public interface TeamRepository extends CrudRepository<Team, Integer> {
    @Query("select t from Team t where t.name like %?1")
    List<Team> findByName(String name);

    @Modifying
    @Query("delete Team t where t.name like :name")
    void deleteMatch(String name);

    @Transactional
    @Modifying
    @Query("update Team t set t.games = t.games + 1 where t.name like :name")
    void addGame(String name);

    @Transactional
    @Modifying
    @Query("update Team t set t.wins = t.wins + 1 where t.name like :name")
    void addWin(String name);

    @Transactional
    @Modifying
    @Query("update Team t set t.draws = t.draws + 1 where t.name like :name")
    void addDraw(String name);

    @Transactional
    @Modifying
    @Query("update Team t set t.defeats = t.defeats + 1 where t.name like :name")
    void addDefeat(String name);

    @Query("select t from Team t order by t.games asc")
    List<Team> orderByGamesASC();

    @Query("select t from Team t order by t.games desc")
    List<Team> orderByGamesDESC();

    @Query("select t from Team t order by t.wins asc")
    List<Team> orderByWinsASC();

    @Query("select t from Team t order by t.wins desc")
    List<Team> orderByWinsDESC();

    @Query("select t from Team t order by t.draws asc")
    List<Team> orderByDrawsASC();

    @Query("select t from Team t order by t.draws desc")
    List<Team> orderByDrawsDESC();

    @Query("select t from Team t order by t.defeats asc")
    List<Team> orderByDefeatsASC();

    @Query("select t from Team t order by t.defeats desc")
    List<Team> orderByDefeatsDESC();
} 