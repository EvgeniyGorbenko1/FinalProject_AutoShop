package com.tms.finalproject_autoshop.controller;

import com.tms.finalproject_autoshop.model.PromoCode;
import com.tms.finalproject_autoshop.model.dto.PromoCreateDto;
import com.tms.finalproject_autoshop.model.dto.PromoUpdateDto;
import com.tms.finalproject_autoshop.service.PromoCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/promo")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
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
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping()
    public ResponseEntity<?> createPromoCode(@RequestBody PromoCreateDto promoCode) {
        Boolean result = promoCodeService.createPromo(promoCode);
        if (result) {
            return ResponseEntity.ok().body(promoCode);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get all promo codes",
            description = "Returns a list of all promo codes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of promo codes",
                            content = @Content(schema = @Schema(implementation = PromoCode.class))),})
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<PromoCode>> getAllPromoCode() {
        List<PromoCode> promoCodes = promoCodeService.getAllPromo();
        if (promoCodes.isEmpty()) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(promoCodes);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePromoCode(@PathVariable Integer id, @RequestBody PromoUpdateDto promoCode) {
        Boolean result = promoCodeService.updatePromo(id, promoCode);
        if (result) {
            return ResponseEntity.ok(promoCode);
        }
        return ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePromoCode(@PathVariable Integer id) {
        PromoCode promoCode = promoCodeService.deleteById(id);
        if (promoCode != null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
