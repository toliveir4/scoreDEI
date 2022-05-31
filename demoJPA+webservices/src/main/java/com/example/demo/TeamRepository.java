package com.example.demo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.data.Team;

public interface TeamRepository extends CrudRepository<Team, Integer> {
    @Query("select t from Team t where t.name like %?1")
    List<Team> findByNameEndsWith(String name);

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
} 