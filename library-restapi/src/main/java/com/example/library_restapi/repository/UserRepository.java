package com.example.library_restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.library_restapi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
