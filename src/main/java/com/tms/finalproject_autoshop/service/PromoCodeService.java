package com.tms.finalproject_autoshop.service;

import com.tms.finalproject_autoshop.model.PromoCode;
import com.tms.finalproject_autoshop.model.dto.PromoCreateDto;
import com.tms.finalproject_autoshop.model.dto.PromoUpdateDto;
import com.tms.finalproject_autoshop.repository.PromoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PromoCodeService {

    private final PromoRepository promoRepository;

    public PromoCodeService(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }

    public Optional<PromoCode> findByCode(String code) {
        return promoRepository.findByCode(code);
    }

    public Double applyPromo(PromoCode promoCode, double amount) {
        if (!promoCode.getIsActive()) {
            return amount;
        }
        double discount = amount * (promoCode.getDiscount() / 100);
        return amount - discount;
    }

    @Transactional
    public Boolean createPromo(PromoCreateDto promoCode) {
        try {
            PromoCode promoCodeNew = new PromoCode();
            promoCodeNew.setCode(promoCode.getCode());
            promoCodeNew.setDiscount(promoCode.getDiscount());
            promoCodeNew.setIsActive(promoCode.getIsActive());
            promoRepository.save(promoCodeNew);
            return true;
        } catch (Exception e) {
            log.info("Create Promo Code failed.");
            return false;
        }
    }

    public List<PromoCode> getAllPromo() {
        return promoRepository.findAll();
    }

    @Transactional
    public Boolean updatePromo(Integer id, PromoUpdateDto promoCode) {
        Optional<PromoCode> updatedPromo = promoRepository.findById(id);
        if (updatedPromo.isPresent()) {
            updatedPromo.get().setCode(promoCode.getCode());
            updatedPromo.get().setDiscount(promoCode.getDiscount());
            promoRepository.save(updatedPromo.get());
            return true;
        } else {
            log.info("Update Promo Code failed.");
            return false;
        }
    }

    public PromoCode deleteById(Integer id) {
        Optional<PromoCode> promoCode = promoRepository.findById(id);
        if (promoCode.isPresent()) {
            promoRepository.delete(promoCode.get());
        } else {
            log.info("Delete Promo Code failed.");
            return null;
        }
        return promoCode.get();
    }
}
