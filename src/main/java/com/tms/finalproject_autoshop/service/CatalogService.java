package com.tms.finalproject_autoshop.service;

import com.tms.finalproject_autoshop.model.Catalog;
import com.tms.finalproject_autoshop.model.dto.CreateCatalogDto;
import com.tms.finalproject_autoshop.repository.CatalogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CatalogService {
    private final CatalogRepository catalogRepository;

    public CatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @Transactional
    public Boolean createCatalog(CreateCatalogDto createCatalogDto) {
        try {
            Catalog newCatalog = new Catalog();
            newCatalog.setName(createCatalogDto.getName());
            newCatalog.setDescription(createCatalogDto.getDescription());
            newCatalog.setImage(createCatalogDto.getImage());
            catalogRepository.save(newCatalog);
            return true;
        } catch (Exception ex) {
            log.error("Error in saving catalog", ex);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    public Optional<Catalog> getCatalogById(Long id) {
        return catalogRepository.findById(id);

    }

    @Transactional
    public Optional<Catalog> updateCatalog(Catalog catalog) {
        Optional<Catalog> updateCatalogDB = getCatalogById(catalog.getId());
        if (updateCatalogDB.isPresent()) {
            return Optional.of(catalogRepository.saveAndFlush(catalog));
        } else {
            log.error("Catalog not found with id {}", catalog.getId());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Optional.empty();
        }
    }

    public List<Catalog> getAllCatalogs() {
        return catalogRepository.findAll();
    }

    @Transactional
    public Boolean deleteCatalog(Long id) {
        try {
            Optional<Catalog> catalogDb = catalogRepository.getCatalogById(id);
            if (catalogDb.isPresent()) {
                catalogRepository.deleteById(id);
                return true;
            }
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("Catalog id not found");
            return false;
        }
        return false;
    }
}
