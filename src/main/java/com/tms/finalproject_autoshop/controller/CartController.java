package com.tms.finalproject_autoshop.controller;

import com.tms.finalproject_autoshop.model.Cart;
import com.tms.finalproject_autoshop.security.CustomUserDetailService;
import com.tms.finalproject_autoshop.service.CartService;
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

import java.util.Optional;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/cart")
@Tag(name = "Cart", description = "Operations with user shopping cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(
            summary = "Get user's cart",
            description = "Returns the cart of the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cart found",
                            content = @Content(schema = @Schema(implementation = Cart.class))),
                    @ApiResponse(responseCode = "404", description = "Cart not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized")
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCart() {
        Long userId = CustomUserDetailService.getUserId();
        Optional<Cart> cart = cartService.getCart(userId);
        if (cart.isPresent()) {
            return ResponseEntity.ok(cart.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(
            summary = "Create a new cart",
            description = "Creates a cart for the authenticated user if it does not exist",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cart created"),
                    @ApiResponse(responseCode = "400", description = "Cart already exists or error occurred")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createCart() {
        Long userId = CustomUserDetailService.getUserId();
        Optional<Cart> cart = cartService.createCart(userId);
        if (cart.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(cart.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(
            summary = "Delete item from cart",
            description = "Removes a specific product from the user's cart",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item removed"),
                    @ApiResponse(responseCode = "400", description = "Failed to remove item")
            }
    )
    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteItem(
            @Parameter(description = "ID of the product to remove")
            @PathVariable Long productId) {

        Long userId = CustomUserDetailService.getUserId();
        if (cartService.removeItem(productId, userId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(
            summary = "Clear cart",
            description = "Removes all items from the user's cart",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cart cleared"),
                    @ApiResponse(responseCode = "400", description = "Failed to clear cart")
            }
    )
    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> clearCart() {
        Long userId = CustomUserDetailService.getUserId();
        if (cartService.clearCart(userId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(
            summary = "Add item to cart",
            description = "Adds a product to the user's cart",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item added"),
                    @ApiResponse(responseCode = "400", description = "Failed to add item")
            }
    )
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addItem(
            @Parameter(description = "Product ID") @RequestParam Long productId,
            @Parameter(description = "Quantity to add") @RequestParam int quantity) {

        Long userId = CustomUserDetailService.getUserId();
        if (cartService.addToCart(userId, productId, quantity)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Apply promo code",
            description = "Returns total cart amount with applied promo code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Promo applied")
            }
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/promo")
    public ResponseEntity<?> getTotalAmountWithPromo(@RequestParam(required = false) String promoCode) {
        Long userId = CustomUserDetailService.getUserId();
        Optional<Double> total = cartService.getTotalAmountWithPromoCode(userId, promoCode);
        return total.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());

    }
}
