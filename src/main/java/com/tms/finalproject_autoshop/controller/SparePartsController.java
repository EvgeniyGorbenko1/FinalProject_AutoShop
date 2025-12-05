package com.tms.finalproject_autoshop.controller;

import com.tms.finalproject_autoshop.model.SpareParts;
import com.tms.finalproject_autoshop.model.dto.PartDto;
import com.tms.finalproject_autoshop.service.SparePartsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/catalog")
public class SparePartsController {
    private final SparePartsService sparePartsService;
    private final SpareParts spareParts;

    public SparePartsController(SparePartsService sparePartsService, SpareParts spareParts) {
        this.sparePartsService = sparePartsService;
        this.spareParts = spareParts;
    }

    @GetMapping()
    public ResponseEntity<List<SpareParts>> getAllSpareParts(){
        List<SpareParts> spareParts = sparePartsService.getAllSpareParts();
        if(spareParts.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(spareParts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<SpareParts>> getSparePartById(@PathVariable Long id){
        Optional<SpareParts> spareParts = sparePartsService.getPartById(id);
        if(spareParts.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(spareParts, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<SpareParts> updateParts(@RequestBody SpareParts spareParts){
        Optional<SpareParts> updatedParts = sparePartsService.updateSpareParts(spareParts);
        if(updatedParts.isEmpty()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(updatedParts.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createPart(@RequestBody PartDto spareParts){
        Boolean result = sparePartsService.createPart(spareParts);
        if(!result){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePart(@PathVariable Long id){
        Boolean result = sparePartsService.deleteSparePart(id);
        if(!result){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
