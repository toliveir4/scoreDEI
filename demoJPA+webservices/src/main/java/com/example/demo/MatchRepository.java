package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import com.example.data.Match;

public interface MatchRepository extends CrudRepository <Match, Integer> {
    Optional<Match> findByNameEndsWith(String Name);
}    