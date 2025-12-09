package com.tms.finalproject_autoshop.controller;

import com.tms.finalproject_autoshop.model.Catalog;
import com.tms.finalproject_autoshop.service.CatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        List<Catalog> catalogs = catalogService.getAllCatalog();
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

    @PutMapping("/{id}")
    public ResponseEntity<Optional<Catalog>> updateCatalog(@RequestBody Catalog catalog, @PathVariable Long id){
        Optional<Catalog> updatedCatalog = catalogService.updateCatalog(catalog, id);
        if(updatedCatalog.isEmpty()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(updatedCatalog, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createCatalog(@RequestBody Catalog catalog){
        Boolean result = catalogService.createCatalog(catalog);
        if(!result){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCatalog(@PathVariable Long id){
        Boolean result = catalogService.deleteCatalogById(id);
        if(!result){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
