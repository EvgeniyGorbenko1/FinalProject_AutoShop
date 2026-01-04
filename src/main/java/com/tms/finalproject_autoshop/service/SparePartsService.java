package com.tms.finalproject_autoshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.finalproject_autoshop.model.Category;
import com.tms.finalproject_autoshop.model.SpareParts;
import com.tms.finalproject_autoshop.model.User;
import com.tms.finalproject_autoshop.model.dto.PartDto;
import com.tms.finalproject_autoshop.repository.SparePartsRepository;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.http.converter.json.GsonFactoryBean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class SparePartsService {
    private final SparePartsRepository sparePartsRepository;

    public SparePartsService(SparePartsRepository sparePartsRepository) {
        this.sparePartsRepository = sparePartsRepository;
    }

    public Boolean createPart(PartDto partDto) {
        try {
            SpareParts newSparePart = new SpareParts();
            newSparePart.setName(partDto.getName());
            newSparePart.setCategory(partDto.getCategory());
            newSparePart.setPrice(partDto.getPrice());
            newSparePart.setImage(partDto.getImage());
            newSparePart.setDescription(partDto.getDescription());
            newSparePart.setSpecifications(partDto.getSpecifications());
            sparePartsRepository.save(newSparePart);
        } catch (Exception ex) {
            System.out.println("Error in saving user: " + ex.getMessage());
        }
        return true;
    }

    public Optional<SpareParts> getPartById(Long id) {
        return sparePartsRepository.findById(id);
    }

    public List<SpareParts> getPartByCategory(String category) {
        return sparePartsRepository.findByCategory(category);
    }

    public List<SpareParts> findByCategoryAndSpec(String category, Map<String, String> filter) throws JsonProcessingException {
        String specJson = new ObjectMapper().writeValueAsString(filter);
        if(category == null || filter.isEmpty()) {
            throw new IllegalArgumentException("Category or Filter is null or Filter is empty");
        }
        return sparePartsRepository.findOilByCategoryAndSpec(category, specJson);
    }

    public Optional<SpareParts> updateSpareParts(SpareParts spareParts) {
        Optional<SpareParts> updatePartDB = getPartById(spareParts.getId());
        if (updatePartDB.isPresent()) {
            return Optional.of(sparePartsRepository.saveAndFlush(spareParts));
        } else {
            throw new NullPointerException();
        }
    }

    public List<SpareParts> getAllSpareParts() {
        return sparePartsRepository.findAll();
    }

    public Boolean deleteSparePart(Long id) {
        if(sparePartsRepository.existsById(id)) {
            sparePartsRepository.deleteById(id);
            return true;
        } else{
            throw new NullPointerException(); //TODO: exception
        }
    }
}
