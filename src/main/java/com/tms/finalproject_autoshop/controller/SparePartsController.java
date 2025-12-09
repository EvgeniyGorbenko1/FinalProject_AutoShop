package com.tms.finalproject_autoshop.controller;

import com.tms.finalproject_autoshop.model.OilParts;
import com.tms.finalproject_autoshop.model.ProductAbstract;
import com.tms.finalproject_autoshop.model.ServiceParts;
import com.tms.finalproject_autoshop.model.SpareParts;
import com.tms.finalproject_autoshop.model.dto.ProductUpdateDto;
import com.tms.finalproject_autoshop.service.SparePartsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping ("/catalog/product")
public class SparePartsController {
    private final SparePartsService sparePartsService;

    public SparePartsController(SparePartsService sparePartsService) {
        this.sparePartsService = sparePartsService;
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> getAllProducts(@PathVariable String type){
        List<?> product = sparePartsService.getAllProducts(type);
        if(product.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/{type}/{id}")
    public ResponseEntity<?> getSparePartById(@PathVariable String type, @PathVariable Long id){
        Optional<?> product = sparePartsService.getProductById(type, id);
        if(product.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{type}/{id}")
    public ResponseEntity<?> updateServiceParts(@PathVariable String type, @PathVariable Long id, @RequestBody ProductAbstract product){
        Optional<?> updatedParts = sparePartsService.updateProduct(type,id, product);
        if(updatedParts.isEmpty()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/{type}")
    public ResponseEntity<?> createSpareParts(@PathVariable String type , @RequestBody ProductAbstract product){
        try{
            Optional<?> newPart = sparePartsService.createSparePart(type, product);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch(Exception e){
            // Вывод ошибки в консоль для отладки
            // Возвращаем более информативный ответ
            return new ResponseEntity<>("Conflict: " + e.getMessage(), HttpStatus.CONFLICT);
        }
    }


    @DeleteMapping("/{type}/{id}")
    public ResponseEntity<?> deletePart(@PathVariable String type, @PathVariable Long id){
        Boolean result = sparePartsService.deleteProductById(type, id);
        if(!result){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
