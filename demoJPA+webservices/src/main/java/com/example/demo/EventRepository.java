package com.example.demo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.data.Player;
import com.example.data.Team;
import com.example.data.Event;
import com.example.data.Match;

public interface EventRepository extends CrudRepository <Event, Integer> {
    @Query("select e from Event e where e.title like %?1")
    List<Event> findByTitle(String Name);
    
    @Query("select e from Event e where e.match = ?1 Order by e.time")
    List<Event> getEventsFromMatch(Match i);

    @Query("select e from Event e where e.team = ?1")
    List<Event> getTeamEvents(Team i);

    @Query("select e from Event e where e.player = ?1")
    List<Event> getPlayerEvents(Player i);


    @Query("select e from Event e where e.team = ?1 and e.match = ?2")
    List<Event> getTeamEventsFromAMatch(Team i);

    @Query("select e from Event e where e.player = ?1 and e.match = ?2")
    List<Event> getPlayerEventsFromAMatch(Player i,Match m );

    @Query("Delete from Event e where e.id = ?1")
    List<Event> deleteEventById(int id);

}    