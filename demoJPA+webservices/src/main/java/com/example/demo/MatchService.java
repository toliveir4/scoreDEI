package com.example.demo;    

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.data.Match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service    
public class MatchService   
{    
    @Autowired    
    private MatchRepository MatchRepository;

    public List<Match> getAllMatchs()  
    {    
        List<Match>MatchRecords = new ArrayList<>();    
        MatchRepository.findAll().forEach(MatchRecords::add);    
        return MatchRecords;    
    }

    public void addMatch(Match match) {
        MatchRepository.save(match);
    }


    public Optional<Match> getMatch(int id) {
        return MatchRepository.findById(id);
    }
    public void updateStatus(int id,int status){
        MatchRepository.updateStatus(id,status);
    }
    public void removeMatch(int id){
        MatchRepository.deleteMatch(id);;
    }
}   