package com.example.demo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.example.data.Event;

public interface EventRepository extends CrudRepository <Event, Integer> {
}    