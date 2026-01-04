package com.tms.finalproject_autoshop.service;

import com.tms.finalproject_autoshop.model.Catalog;
import com.tms.finalproject_autoshop.model.SpareParts;
import com.tms.finalproject_autoshop.model.dto.CatalogDto;
import com.tms.finalproject_autoshop.model.dto.PartDto;
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

    public Boolean createCatalog(CatalogDto catalogDto) {
        try {
            Catalog newCatalog = new Catalog();
            newCatalog.setName(catalogDto.getName());
            newCatalog.setDescription(catalogDto.getDescription());
            newCatalog.setImage(catalogDto.getImage());

            catalogRepository.save(newCatalog);
        } catch (Exception ex) {
            System.out.println("Error in saving user: " + ex.getMessage());
        }
        return true;
    }

    public Optional<Catalog> getCatalogById(Long id) {
        return catalogRepository.findById(id);

    }

    public Optional<Catalog> updateCatalog(Catalog catalog) {
        Optional<Catalog> updateCatalogDB = getCatalogById(catalog.getId());
        if (updateCatalogDB.isPresent()) {
            return Optional.of(catalogRepository.saveAndFlush(catalog));
        } else {
            throw new NullPointerException();
        }
    }

    public List<Catalog> getAllCatalogs() {
        return catalogRepository.findAll();
    }

    public Boolean deleteCatalog(Long id) {
        if(catalogRepository.existsById(id)) {
            catalogRepository.deleteById(id);
            return true;
        } else{
            throw new NullPointerException(); //TODO: exception
        }
    }
}
