package com.example.demo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import javax.transaction.Transactional;

import com.example.data.WebUser;

public interface WebUserRepository extends CrudRepository <WebUser, Integer> {
    Optional<WebUser> findByUsernameEndsWith(String username);

    @Transactional
    @Modifying
    @Query("update WebUser u set u.admin=CASE u.admin when true then false else true end where id = ?1")
    void updateAdmin(int id);
}    