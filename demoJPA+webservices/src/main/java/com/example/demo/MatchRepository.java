package com.example.demo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import com.example.data.Match;

public interface MatchRepository extends CrudRepository <Match, Integer> {
    @Query("select m from Match m where m.name like %?1")
    Optional<Match> findByNameEndsWith(String Name);

    @Modifying
    @Query("delete Match m where m.name like :name")
    void deleteMatch(String name);

    @Transactional
    @Modifying
    @Query("update Match m set m.scoreHome = m.scoreHome + 1 where id = :id")
    void addHomeGoal(int id);

    @Transactional
    @Modifying
    @Query("update Match m set m.scoreAway = m.scoreAway + 1 where id = :id")
    void addAwayGoal(int id);

    @Transactional
    @Modifying
    @Query("update Match m set m.status = 1 where id = :id")
    void updateStatus(int id);


}    