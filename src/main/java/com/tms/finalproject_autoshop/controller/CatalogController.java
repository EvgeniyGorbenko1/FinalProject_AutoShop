package com.tms.finalproject_autoshop.controller;

import com.tms.finalproject_autoshop.model.Catalog;
import com.tms.finalproject_autoshop.model.SpareParts;
import com.tms.finalproject_autoshop.model.dto.CatalogDto;
import com.tms.finalproject_autoshop.model.dto.PartDto;
import com.tms.finalproject_autoshop.service.CatalogService;
import com.tms.finalproject_autoshop.service.SparePartsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/catalog")
public class CatalogController {
    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping()
    public ResponseEntity<List<Catalog>> getAllSpareParts(){
        List<Catalog> catalogs = catalogService.getAllCatalogs();
        if(catalogs.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(catalogs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Catalog>> getCatalogById(@PathVariable Long id){
        Optional<Catalog> catalog = catalogService.getCatalogById(id);
        if(catalog.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(catalog, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<Catalog> updateCatalog(@RequestBody Catalog catalog){
        Optional<Catalog> updatedCatalog = catalogService.updateCatalog(catalog);
        if(updatedCatalog.isEmpty()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(updatedCatalog.get(), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<HttpStatus> createCatalog(@RequestBody CatalogDto catalogDto){
        Boolean result = catalogService.createCatalog(catalogDto);
        if(!result){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCatalog(@PathVariable Long id){
        Boolean result = catalogService.deleteCatalog(id);
        if(!result){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
