package com.example.demo;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import java.util.ArrayList;    
import org.springframework.beans.factory.annotation.Autowired;    
import org.springframework.stereotype.Service;

import com.example.data.Team;

@Service    
public class TeamService   
{    
    @Autowired    
    private TeamRepository profRepository;

    public List<Team> getAllProfessors()  
    {    
        List<Team>userRecords = new ArrayList<>();    
        profRepository.findAll().forEach(userRecords::add);    
        return userRecords;    
    }

    public void addProfessor(Team prof)  
    {
        System.out.println(prof);
        profRepository.save(prof);    
    }

    public Optional<Team> getProfessor(int id) {
        return profRepository.findById(id);
    }

}    