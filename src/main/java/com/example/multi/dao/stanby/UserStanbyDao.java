package com.example.multi.dao.stanby;

import com.example.multi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStanbyDao extends JpaRepository<User, Integer> {
}
