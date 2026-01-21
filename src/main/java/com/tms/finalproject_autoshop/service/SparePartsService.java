package com.tms.finalproject_autoshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.finalproject_autoshop.model.SpareParts;
import com.tms.finalproject_autoshop.model.dto.CreatePartDto;
import com.tms.finalproject_autoshop.model.dto.UpdatePartDto;
import com.tms.finalproject_autoshop.repository.SparePartsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class SparePartsService {
    private final SparePartsRepository sparePartsRepository;

    public SparePartsService(SparePartsRepository sparePartsRepository) {
        this.sparePartsRepository = sparePartsRepository;
    }

    @Transactional
    public Boolean createPart(CreatePartDto createPartDto) {
        try {
            SpareParts newSparePart = new SpareParts();
            newSparePart.setName(createPartDto.getName());
            newSparePart.setCategory(createPartDto.getCategory());
            newSparePart.setPrice(createPartDto.getPrice());
            newSparePart.setImage(createPartDto.getImage());
            newSparePart.setDescription(createPartDto.getDescription());
            newSparePart.setSpecifications(createPartDto.getSpecifications());
            sparePartsRepository.save(newSparePart);
            return true;
        } catch (Exception ex) {
            log.error("Error in saving part: " + ex.getMessage());
            return false;
        }
    }

    public Optional<SpareParts> getPartById(Long id) {
        return sparePartsRepository.findById(id);
    }

    public List<SpareParts> getPartByCategory(String category) {
        return sparePartsRepository.findByCategory(category);
    }

    public List<SpareParts> findByCategoryAndSpec(String category, Map<String, String> filter) throws JsonProcessingException {
        String specJson = new ObjectMapper().writeValueAsString(filter);
        if (category == null && filter.isEmpty()) {
            throw new IllegalArgumentException("At least one filter must be provided");
        }
        return sparePartsRepository.findByCategoryAndSpec(category, specJson);
    }

    @Transactional
    public Boolean updateSpareParts(UpdatePartDto updatePartDto) {
        Optional<SpareParts> updatePartDB = getPartById(updatePartDto.getId());
        if (updatePartDB.isPresent()) {
            updatePartDB.get().setName(updatePartDto.getName());
            updatePartDB.get().setCategory(updatePartDto.getCategory());
            updatePartDB.get().setPrice(updatePartDto.getPrice());
            updatePartDB.get().setImage(updatePartDto.getImage());
            updatePartDB.get().setDescription(updatePartDto.getDescription());
            updatePartDB.get().setSpecifications(updatePartDto.getSpecifications());
            sparePartsRepository.save(updatePartDB.get());
            return true;
        } else {
            log.error("SpareParts not found");
            return false;
        }
    }

    public List<SpareParts> getAllSpareParts() {
        return sparePartsRepository.findAll();
    }

    @Transactional
    public Boolean deleteSparePart(Long id) {
        try {
            Optional<SpareParts> sparePartDB = getPartById(id);
            if (sparePartDB.isEmpty()) {
                return false;
            }
            sparePartsRepository.deleteById(id);
            return true;
        } catch (Exception ex) {
            log.error("SpareParts not found with id: " + id);
            return false;
        }
    }


    public List<SpareParts> getSortedPartsByField(String field, String order) {
        if (order != null && !order.isBlank() && order.equals("desc")) {
            return sparePartsRepository.findAll(Sort.by(Sort.Direction.DESC, field));
        }
        return sparePartsRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    public Page<SpareParts> getAllSparePartsWithPagination(int page, int size) {
        return sparePartsRepository.findAll(PageRequest.of(page, size));
    }
}
