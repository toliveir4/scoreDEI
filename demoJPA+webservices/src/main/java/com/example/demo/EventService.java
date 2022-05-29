package com.example.demo;    

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.data.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service    
public class EventService   
{    
    @Autowired    
    private EventRepository eventRepository;

    public List<Event> getAllUsers()  
    {    
        List<Event>userRecords = new ArrayList<>();    
        eventRepository.findAll().forEach(userRecords::add);    
        return userRecords;    
    }

    public void addUser(Event User)  
    {    
        eventRepository.save(User);    
    }

    public Optional<Event> getUser(int id) {
        return eventRepository.findById(id);
    }

}    