package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromoRepository  extends JpaRepository<PromoCode, Long>{

    Optional<PromoCode> findByCode(String code);
}
