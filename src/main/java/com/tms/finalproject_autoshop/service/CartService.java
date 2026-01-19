package com.tms.finalproject_autoshop.service;

import com.tms.finalproject_autoshop.model.*;
import com.tms.finalproject_autoshop.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final SparePartsRepository sparePartsRepository;
    private final CartItemRepository cartItemRepository;
    private final PromoRepository promoRepository;
    private final PromoCodeService promoCodeService;

    public CartService(CartRepository cartRepository, UserRepository userRepository,
                       SparePartsRepository sparePartsRepository, CartItemRepository cartItemRepository,
                       PromoRepository promoRepository, PromoCodeService promoCodeService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.sparePartsRepository = sparePartsRepository;
        this.cartItemRepository = cartItemRepository;
        this.promoRepository = promoRepository;
        this.promoCodeService = promoCodeService;
    }

    public Optional<Cart> getCart(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Transactional
    public Optional<Cart> createCart(Long userId) {
        try{
            Optional<User> user = userRepository.findById(userId);
            if(user.isEmpty()){
                log.error("User not found");
                return Optional.empty();
            }
            Optional<Cart> cart = cartRepository.findByUserId(userId);
            if(cart.isPresent()){
                return cart;
            }

            Cart cartSaved = new Cart();
            cartSaved.setUser(user.get());
            cartRepository.save(cartSaved);
            return Optional.of(cartSaved);
        } catch (Exception ex) {
            log.error("Error creating cart");
            return Optional.empty();
        }
    }

    public boolean removeItem(Long productId, Long userId) {
        Optional<Cart> cart = getCart(userId);
        if (cart.isEmpty()) {
            return false;
        }
            cart.get().getItems().removeIf(item -> item.getProduct().getId().equals(productId));
            cartRepository.save(cart.get());
            return true;

    }

    public boolean clearCart(Long userId) {
        Optional<Cart> cart = getCart(userId);
        if (cart.isEmpty()) {
            return false;
        }
            cart.get().getItems().clear();
            cartRepository.save(cart.get());
            return true;
    }

    @Transactional
    public boolean addToCart(Long userId, Long productId, int quantity) {
        Optional<Cart> cart = getCart(userId);
        if (cart.isEmpty()) {
            return false;
        }
        SpareParts product = sparePartsRepository.findById(productId)
                .orElseThrow();

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.get().getId(), productId);
        if (item == null) {
            item = new CartItem();
            item.setCart(cart.get());
            item.setProduct(product);
            item.setQuantity(quantity);
            cartItemRepository.save(item);
            return true;
        }


        item.setQuantity(item.getQuantity() + quantity);
        cartItemRepository.save(item);
        return true;
    }

    public Optional<Double> getTotalAmountWithPromoCode(Long userId, String promoCode) {
        Optional<Cart> cart = getCart(userId);
        if(cart.isEmpty()){
            return Optional.empty();
        }
        Double totalAmount = cart.get().getTotalAmount();

        if(promoCode == null || promoCode.isEmpty()){
            return Optional.of(totalAmount);
        }

        Optional<PromoCode> promo = promoRepository.findByCode(promoCode);
        if(promo.isEmpty()){
            return Optional.of(totalAmount);
        }
        if(promo.get().getIsActive() == false) {
            return Optional.of(totalAmount);
        }
        Double finalAmount = promoCodeService.applyPromo(promo.get(), totalAmount);

        promo.get().setIsActive(false);
        promoRepository.save(promo.get());
        return Optional.of(finalAmount);
    }

}



