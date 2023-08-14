package com.tectot.filebox.repositories;

import com.tectot.filebox.entities.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganisationRepo extends JpaRepository<Organisation, Long> {
}
