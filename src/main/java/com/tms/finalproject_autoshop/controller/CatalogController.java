package com.tms.finalproject_autoshop.controller;

import com.tms.finalproject_autoshop.model.Catalog;
import com.tms.finalproject_autoshop.model.dto.CatalogDto;
import com.tms.finalproject_autoshop.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/catalog")
@Tag(name = "Catalog", description = "Operations with catalog categories")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Operation(
            summary = "Get all catalog categories",
            description = "Returns a list of all catalog categories",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List returned",
                            content = @Content(schema = @Schema(implementation = Catalog.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<Catalog>> getAllSpareParts() {
        List<Catalog> catalogs = catalogService.getAllCatalogs();
        if (catalogs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(catalogs, HttpStatus.OK);
    }

    @Operation(
            summary = "Get catalog by ID",
            description = "Returns a catalog category by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Catalog found",
                            content = @Content(schema = @Schema(implementation = Catalog.class))),
                    @ApiResponse(responseCode = "404", description = "Catalog not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Catalog> getCatalogById(
            @Parameter(description = "Catalog ID") @PathVariable Long id) {

        Optional<Catalog> catalog = catalogService.getCatalogById(id);
        return catalog
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.OK));
    }

    @Operation(
            summary = "Update catalog",
            description = "Updates an existing catalog category",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Catalog updated",
                            content = @Content(schema = @Schema(implementation = Catalog.class))),
                    @ApiResponse(responseCode = "404", description = "Catalog not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<Catalog> updateCatalog(
            @Parameter(description = "Catalog object") @RequestBody Catalog catalog) {

        Optional<Catalog> updatedCatalog = catalogService.updateCatalog(catalog);
        return updatedCatalog
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Create new catalog",
            description = "Creates a new catalog category",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Catalog created"),
                    @ApiResponse(responseCode = "409", description = "Catalog already exists")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createCatalog(
            @Parameter(description = "Catalog DTO") @RequestBody CatalogDto catalogDto) {

        Boolean result = catalogService.createCatalog(catalogDto);
        if (!result) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Delete catalog",
            description = "Deletes a catalog category by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Catalog deleted"),
                    @ApiResponse(responseCode = "404", description = "Catalog not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatalog(
            @Parameter(description = "Catalog ID") @PathVariable Long id) {

        Boolean result = catalogService.deleteCatalog(id);
        if (!result) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
