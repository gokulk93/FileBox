package com.tectot.filebox.services;

import com.tectot.filebox.dtos.OrganisationDTO;
import com.tectot.filebox.entities.Organisation;
import com.tectot.filebox.entities.User;
import com.tectot.filebox.repositories.OrganisationRepo;
import com.tectot.filebox.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tectot.filebox.utils.ObjectConverter.convertToOrganisation;
import static com.tectot.filebox.utils.ObjectConverter.convertToOrganisationDTO;

@Service
public class OrganisationService {

    @Autowired
    private OrganisationRepo organisationRepo;

    @Autowired
    private UserRepo userRepo;

    public List<OrganisationDTO> findAllOrgs() {
        List<Organisation> organisations = organisationRepo.findAll();
        return organisations.stream().map(org -> convertToOrganisationDTO(org)).collect(Collectors.toList());
    }

    public OrganisationDTO findOrgById(Long id){
        Optional<Organisation> organisation = organisationRepo.findById(id);
        if (organisation.isPresent()) {
            return convertToOrganisationDTO(organisation.get());
        }
        return null;
    }

    @Transactional
    public void createOrg(OrganisationDTO orgDTO){
        Organisation org = convertToOrganisation(orgDTO);
        organisationRepo.save(org);
    }

    @Transactional
    public void updateOrg(OrganisationDTO orgDTO){
        Organisation org = convertToOrganisation(orgDTO);
        organisationRepo.save(org);
    }


    @Transactional
    public void deleteOrgById(Long id){
        organisationRepo.deleteById(id);
    }
}
