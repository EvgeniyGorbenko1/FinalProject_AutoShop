package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    Optional<Catalog> getCatalogById(Long id);
}
