package com.tms.finalproject_autoshop.service;

import com.tms.finalproject_autoshop.model.*;
import com.tms.finalproject_autoshop.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
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
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Optional.empty();
        }
    }

    public boolean removeItem(Long productId, Long userId) throws Exception {
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
    public CartItem addToCart(Long userId, Long productId, int quantity) {
        Optional<Cart> cart = getCart(userId);
        if (cart.isEmpty()) {
            return null;
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
            return item;
        }


        item.setQuantity(item.getQuantity() + quantity);
        cartItemRepository.save(item);
        return item;
    }

    public Double getTotalAmountWithPromoCode(Long userId, String promoCode) {
        Optional<Cart> cart = getCart(userId);
        if(cart.isEmpty()){
            return 0.0;
        }
        Double totalAmount = cart.get().getTotalAmount();

        if(promoCode == null || promoCode.isEmpty()){
            return totalAmount;
        }

        Optional<PromoCode> promo = promoRepository.findByCode(promoCode);
        if(promo.isEmpty()){
            return totalAmount;
        }
        if(promo.get().getIsActive() == false) {
            return totalAmount;
        }
        Double finalAmount = promoCodeService.applyPromo(promo.get(), totalAmount);

        promo.get().setIsActive(false);
        promoRepository.save(promo.get());
        return finalAmount;
    }

}



