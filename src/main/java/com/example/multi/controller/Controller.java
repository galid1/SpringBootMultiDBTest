package com.example.multi.controller;

import com.example.multi.dao.master.UserMasterDao;
import com.example.multi.dao.stanby.UserStanbyDao;
import com.example.multi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private UserMasterDao masterDao;

    @GetMapping("/master")
    @ResponseBody
    public String getMasterUser(){
        String data = "";

        List<User> users = masterDao.findAll();
        for(User user : users) {
            data += user.getName();
            data += "\n";
        }

        return data;
    }

    @Autowired
    private UserStanbyDao stanbyDao;

    @GetMapping("/stanby")
    @ResponseBody
    public String getStanbyUser(){
        String data = "";

        List<User> users = stanbyDao.findAll();
        for(User user : users) {
            data += user.getName();
            data += "\n";
        }

        return data;
    }

}
