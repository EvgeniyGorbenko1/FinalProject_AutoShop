package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromoRepository extends JpaRepository<PromoCode, Long> {
    Optional<PromoCode> findByCode(String code);
    Optional<PromoCode> findById(Integer id);
}
