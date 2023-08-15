package com.tectot.filebox.controllers;

import com.tectot.filebox.dtos.OrganisationDTO;
import com.tectot.filebox.services.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/organisations")
public class OrganisationController {


    @Autowired
    private OrganisationService organisationService;


    @GetMapping
    public String listOrganisations(Model model) {
        List<OrganisationDTO> organisations = organisationService.findAllOrgs();
        model.addAttribute("organisations", organisations);
        return "organisation/list";
    }

    @GetMapping("/add")
    public String addOrganisationForm(Model model) {
        model.addAttribute("organisation", new OrganisationDTO());
        return "organisation/form";
    }

    @PostMapping("/add")
    public String addOrganisation(@ModelAttribute OrganisationDTO organisationDTO) {
        organisationService.createOrg(organisationDTO);
        return "redirect:/organisations";
    }

    @GetMapping("/edit/{id}")
    public String editOrganisationForm(@PathVariable Long id, Model model) {
        Optional<OrganisationDTO> organisationDTO = Optional.ofNullable(organisationService.findOrgById(id));
        organisationDTO.ifPresent(o -> model.addAttribute("organisation", o));
        return "organisation/form";
    }

    @PostMapping("/edit/{id}")
    public String editOrganisation(@PathVariable Long id, @ModelAttribute OrganisationDTO organisationDTO) {
        organisationService.updateOrg(organisationDTO);
        return "redirect:/organisations";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrganisation(@PathVariable Long id) {
        organisationService.deleteOrgById(id);
        return "redirect:/organisations";
    }
}
