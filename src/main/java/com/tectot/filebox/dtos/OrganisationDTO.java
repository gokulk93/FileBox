package com.tectot.filebox.dtos;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OrganisationDTO {

    private Long id;

    @NotEmpty(message = "Name cannot be null")
    private String name;
}
