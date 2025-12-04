package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.SpareParts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SparePartsRepository extends JpaRepository<SpareParts, Long> {
}
