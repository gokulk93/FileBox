package com.tectot.filebox.utils;

import com.tectot.filebox.dtos.OrganisationDTO;
import com.tectot.filebox.dtos.UserDTO;
import com.tectot.filebox.entities.Organisation;
import com.tectot.filebox.entities.User;
import com.tectot.filebox.services.OrganisationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ObjectConverter {

    @Autowired
    private OrganisationService organisationService;

    private static final ModelMapper modelMapper = new ModelMapper();

    public User convertToUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);

        OrganisationDTO organisationDTO = organisationService.findOrgById(userDTO.getOrganisationId());

        user.setOrganisation(convertToOrganisation(organisationDTO));
        return user;
    }

    public static Organisation convertToOrganisation(OrganisationDTO orgDTO) {
        return modelMapper.map(orgDTO, Organisation.class);
    }

    public static OrganisationDTO convertToOrganisationDTO(Organisation org) {
        return modelMapper.map(org, OrganisationDTO.class);
    }
}
