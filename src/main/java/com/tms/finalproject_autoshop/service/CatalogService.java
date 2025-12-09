package com.tms.finalproject_autoshop.service;

import com.tms.finalproject_autoshop.model.Catalog;
import com.tms.finalproject_autoshop.model.SpareParts;
import com.tms.finalproject_autoshop.repository.CatalogRepository;
import com.tms.finalproject_autoshop.repository.SparePartsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogService {
    private final CatalogRepository catalogRepository;


    public CatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }


    public List<Catalog> getAllCatalog() {
        return catalogRepository.findAll();
    }

    public Optional<Catalog> getCatalogById(Long id) {
        return catalogRepository.findById(id);
    }

    public Boolean createCatalog(Catalog catalog) {
        catalogRepository.save(catalog);
        return true;
    }

    public Boolean deleteCatalogById(Long id) {
        catalogRepository.deleteById(id);
        return true;
    }

    public Optional<Catalog> updateCatalog(Catalog catalog, Long id) {
        Catalog newCatalog = new Catalog();
        if(catalogRepository.findById(id).isPresent()) {
            newCatalog.setName(catalog.getName());
            newCatalog.setDescription(catalog.getDescription());
            newCatalog.setImage(catalog.getImage());
            catalogRepository.save(newCatalog);
        } else {
            throw new RuntimeException("Catalog id not found");
    }
        return Optional.empty();
    }

}
