package com.tms.finalproject_autoshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/catalog")
public class SparePartsController {
    private final SparePartsService sparePartsService;

    public SparePartsController(SparePartsService sparePartsService) {
        this.sparePartsService = sparePartsService;
    }

    @GetMapping()
    public ResponseEntity<List<Parts>> getAllSpareParts(){
        List<Parts> spareParts = sparePartsService.getAllSpareParts;
        if(spareParts.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(spareParts, HttpStatus.OK);
    }

    @GetMapping("/spare-parts/{id}")
    public ResponseEntity<Parts> getSparePartById(@PathVariable Long partId){
        Optional<Parts> parts = sparePartsService.getSparePartById;
        if(parts.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(parts, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Parts> updateParts(@RequestBody Parts parts){
        Optional<Parts> updatedParts = sparePartsService.updateParts;
        if(updatedParts.isEmpty()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(updatedParts.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createPart(@RequestBody Parts parts){
        Boolean result = sparePartsService.createPart(parts);
        if(!result){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePart(@PathVariable Long partId){
        Boolean result = sparePartsService.deletePart(partId);
        if(!result){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
