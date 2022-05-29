package com.example.demo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import com.example.data.WebUser;

public interface WebUserRepository extends CrudRepository <WebUser, Integer> {
    Optional<WebUser> findByNameEndsWith(String Name);
}    