package com.example.demo;    

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.data.WebUser;
import com.example.data.WebUserDTO;

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

    public void addUser(WebUser User) throws Exception {
        if (userRepository.findByUsernameEndsWith(User.getUsername()).isPresent())
            throw new Exception("Username already exists!");

        userRepository.save(User);
    }

    public void addUser(WebUserDTO User) throws Exception {    
        if (userRepository.findByUsernameEndsWith(User.getUsername()).isPresent())
            throw new Exception("Username already exists!");

        WebUser u =  new WebUser();
        u.setUsername(User.getUsername());
        u.setPassword(User.getPassword());
        u.setEmail(User.getEmail());
        u.setRoles("USER" + (User.isAdmin() ? "ADMIN" : ""));
        userRepository.save(u);    
    }

    public Optional<WebUser> getUser(int id) {
        return userRepository.findById(id);
    }

}    