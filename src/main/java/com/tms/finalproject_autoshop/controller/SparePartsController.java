package com.tms.finalproject_autoshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tms.finalproject_autoshop.model.Category;
import com.tms.finalproject_autoshop.model.SpareParts;
import com.tms.finalproject_autoshop.model.dto.PartDto;
import com.tms.finalproject_autoshop.service.SparePartsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/catalog/spare-parts")
public class SparePartsController {
    private final SparePartsService sparePartsService;

    public SparePartsController(SparePartsService sparePartsService) {
        this.sparePartsService = sparePartsService;
    }

    @GetMapping()
    public ResponseEntity<List<SpareParts>> getAllSpareParts() {
        List<SpareParts> spareParts = sparePartsService.getAllSpareParts();
        if (spareParts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(spareParts, HttpStatus.OK);
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<SpareParts>> getSparePartsByCategory(@PathVariable("category") String category) {
        List<SpareParts> sparePartsByCategory = sparePartsService.getPartByCategory(category);
        if (sparePartsByCategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sparePartsByCategory, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<SpareParts>> findSparePartsBySpecifications(@RequestParam(required = false) String category, @RequestParam Map<String, String> allParams) throws JsonProcessingException {
        allParams.remove("category");
        List<SpareParts> result = sparePartsService.findByCategoryAndSpec(category, allParams);
        if (category != null) {
            try {
                Category.valueOf(category);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<SpareParts>> getSparePartById(@PathVariable Long id) {
        Optional<SpareParts> spareParts = sparePartsService.getPartById(id);
        if (spareParts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(spareParts, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<SpareParts> updateParts(@RequestBody SpareParts spareParts) {
        Optional<SpareParts> updatedParts = sparePartsService.updateSpareParts(spareParts);
        if (updatedParts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(updatedParts.get(), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<HttpStatus> createPart(@RequestBody PartDto spareParts) {
        Boolean result = sparePartsService.createPart(spareParts);
        if (!result) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePart(@PathVariable Long id) {
        Boolean result = sparePartsService.deleteSparePart(id);
        if (!result) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
