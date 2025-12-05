package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.ServiceParts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicePartsRepository extends JpaRepository<ServiceParts, Long> {
}
