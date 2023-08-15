package com.tectot.filebox.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganisationDTO {

    private Long id;

    @NotEmpty(message = "Name cannot be null")
    private String name;
}
