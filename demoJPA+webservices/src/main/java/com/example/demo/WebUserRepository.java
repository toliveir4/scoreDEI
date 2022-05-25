package com.example.demo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.example.data.WebUser;

public interface WebUserRepository extends CrudRepository <WebUser, Integer> {
}    