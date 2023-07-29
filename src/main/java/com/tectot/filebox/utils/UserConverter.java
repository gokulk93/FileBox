package com.tectot.filebox.utils;

import com.tectot.filebox.dtos.UserDTO;
import com.tectot.filebox.entities.User;
import org.modelmapper.ModelMapper;

public class UserConverter {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
