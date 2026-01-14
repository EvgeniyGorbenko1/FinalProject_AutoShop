package com.tms.finalproject_autoshop.controller;

import com.tms.finalproject_autoshop.model.PromoCode;
import com.tms.finalproject_autoshop.repository.PromoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/promo")
public class PromoCodeController {

    private final PromoRepository promoRepository;

    public PromoCodeController(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }
    @Operation(
            summary = "Create promo code",
            description = "Creates a new promo code. Accepts JSON body with promo code fields.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Promo code created",
                            content = @Content(schema = @Schema(implementation = PromoCode.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @PutMapping()
    public ResponseEntity<PromoCode> createPromoCode(@RequestBody PromoCode promoCode) {
        promoRepository.save(promoCode);
        return ResponseEntity.ok().body(promoCode);
    }

    @Operation(summary = "Get all promo codes",
            description = "Returns a list of all promo codes",
            responses = {
            @ApiResponse(responseCode = "200", description = "List of promo codes",
            content = @Content(schema = @Schema(implementation = PromoCode.class))),})
    @GetMapping()
    public ResponseEntity<List<PromoCode>> getAllPromoCode() {
        return ResponseEntity.ok().body(promoRepository.findAll());
    }
}
