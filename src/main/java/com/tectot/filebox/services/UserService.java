package com.tectot.filebox.services;

import com.tectot.filebox.dtos.UserDTO;
import com.tectot.filebox.entities.User;
import com.tectot.filebox.repositories.UserRepo;
import com.tectot.filebox.utils.ObjectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserRepo userRepo;

    @Autowired
    private ObjectConverter objectConverter;

    @Autowired
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepo userRepo) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepo = userRepo;
    }

    public boolean createUser(UserDTO userDTO){
        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        User user = objectConverter.convertToUser(userDTO);
        userRepo.save(user);
        return true;
    }

}
