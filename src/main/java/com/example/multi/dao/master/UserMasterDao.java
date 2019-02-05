package com.example.multi.dao.master;

import com.example.multi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMasterDao extends JpaRepository<User, Integer> {
}
