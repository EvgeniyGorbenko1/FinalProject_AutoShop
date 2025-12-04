package com.tms.finalproject_autoshop.service;

import com.tms.finalproject_autoshop.model.SpareParts;
import com.tms.finalproject_autoshop.model.User;
import com.tms.finalproject_autoshop.model.dto.PartDto;
import com.tms.finalproject_autoshop.model.dto.UserCreateDto;
import com.tms.finalproject_autoshop.repository.SparePartsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        } catch (Exception ex) {
            System.out.println("Error in saving user: " + ex.getMessage());
        }
        return true;
    }

    public Optional<SpareParts> getPartById(Long id) {
        return sparePartsRepository.findById(id);

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
