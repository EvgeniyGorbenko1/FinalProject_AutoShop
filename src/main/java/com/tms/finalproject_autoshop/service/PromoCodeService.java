package com.tms.finalproject_autoshop.service;

import com.tms.finalproject_autoshop.model.PromoCode;
import com.tms.finalproject_autoshop.repository.PromoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        if(!promoCode.getIsActive()){
            return amount;
        }
        double discount = amount * (promoCode.getDiscount()/100);
        return amount-discount;
    }
}
