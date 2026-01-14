package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatalogRepository extends JpaRepository<Catalog,Long>
{
    Optional<Catalog> getCatalogById(Long id);
}
