package com.example.demo;    

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.data.WebUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service    
public class WebUserService   
{    
    @Autowired    
    private WebUserRepository userRepository;

    public List<WebUser> getAllUsers()  
    {    
        List<WebUser>userRecords = new ArrayList<>();    
        userRepository.findAll().forEach(userRecords::add);    
        return userRecords;    
    }

    public void addUser(WebUser User)  
    {    
        userRepository.save(User);    
    }

    public Optional<WebUser> getUser(int id) {
        return userRepository.findById(id);
    }

}    