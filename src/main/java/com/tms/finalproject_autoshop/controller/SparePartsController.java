package com.tms.finalproject_autoshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tms.finalproject_autoshop.model.Category;
import com.tms.finalproject_autoshop.model.SpareParts;
import com.tms.finalproject_autoshop.model.dto.PartDto;
import com.tms.finalproject_autoshop.service.SparePartsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/catalog/spare-parts")
@Tag(name = "Spare Parts", description = "Operations with spare parts catalog")
@SecurityRequirement(name = "Bearer Authentication")
public class SparePartsController {

    private final SparePartsService sparePartsService;

    public SparePartsController(SparePartsService sparePartsService) {
        this.sparePartsService = sparePartsService;
    }

    @Operation(
            summary = "Get all spare parts",
            description = "Returns a list of all spare parts",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List returned",
                            content = @Content(schema = @Schema(implementation = SpareParts.class))),
                    @ApiResponse(responseCode = "204", description = "No spare parts found")
            }
    )
    @GetMapping
    public ResponseEntity<List<SpareParts>> getAllSpareParts() {
        List<SpareParts> spareParts = sparePartsService.getAllSpareParts();
        if (spareParts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(spareParts, HttpStatus.OK);
    }

    @Operation(
            summary = "Get spare parts by category",
            description = "Returns spare parts filtered by category",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List returned"),
                    @ApiResponse(responseCode = "204", description = "No spare parts found for this category")
            }
    )
    @GetMapping("category/{category}")
    public ResponseEntity<List<SpareParts>> getSparePartsByCategory(
            @Parameter(description = "Category name") @PathVariable("category") String category) {

        List<SpareParts> sparePartsByCategory = sparePartsService.getPartByCategory(category);
        if (sparePartsByCategory.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sparePartsByCategory, HttpStatus.OK);
    }

    @Operation(
            summary = "Filter spare parts",
            description = "Filters spare parts by category and dynamic specifications",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filtered list returned"),
                    @ApiResponse(responseCode = "400", description = "Invalid category value")
            }
    )
    @GetMapping("/filter")
    public ResponseEntity<List<SpareParts>> findSparePartsBySpecifications(
            @Parameter(description = "Category name") @RequestParam(required = false) String category,
            @Parameter(description = "Dynamic filter parameters") @RequestParam Map<String, String> allParams
    ) throws JsonProcessingException {

        allParams.remove("category");

        if (category != null) {
            try {
                Category.valueOf(category);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        List<SpareParts> result = sparePartsService.findByCategoryAndSpec(category, allParams);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            summary = "Get spare part by ID",
            description = "Returns a spare part by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Spare part found"),
                    @ApiResponse(responseCode = "204", description = "Spare part not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Optional<SpareParts>> getSparePartById(
            @Parameter(description = "Spare part ID") @PathVariable Long id) {

        Optional<SpareParts> spareParts = sparePartsService.getPartById(id);
        if (spareParts.isPresent()) {
            return new ResponseEntity<>(spareParts, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Update spare part",
            description = "Updates an existing spare part",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Spare part updated"),
                    @ApiResponse(responseCode = "404", description = "Spare part not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<SpareParts> updateParts(
            @Parameter(description = "Updated spare part object") @RequestBody SpareParts spareParts) {

        Optional<SpareParts> updatedParts = sparePartsService.updateSpareParts(spareParts);
        return updatedParts.map(parts -> new ResponseEntity<>(parts, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Create spare part",
            description = "Creates a new spare part",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Spare part created"),
                    @ApiResponse(responseCode = "409", description = "Conflict while creating spare part")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<HttpStatus> createPart(
            @Parameter(description = "Spare part DTO") @RequestBody PartDto spareParts) {

        Boolean result = sparePartsService.createPart(spareParts);
        if (!result) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete spare part",
            description = "Deletes a spare part by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Spare part deleted"),
                    @ApiResponse(responseCode = "409", description = "Conflict while deleting spare part")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePart(
            @Parameter(description = "Spare part ID") @PathVariable Long id) {

        Boolean result = sparePartsService.deleteSparePart(id);
        if (!result) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
