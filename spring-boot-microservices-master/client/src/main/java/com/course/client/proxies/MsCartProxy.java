package com.course.client.proxies;

import com.course.client.beans.CartBean;
import com.course.client.beans.CartItemBean;
import com.course.client.beans.ProductBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "ms-cart", url = "localhost:8092")
public interface MsCartProxy {

    @PostMapping(value = "/cart")
    public ResponseEntity<CartBean> createNewCart(@RequestBody CartBean cartData);

    @GetMapping(value = "/cart/{id}")
    public Optional<CartBean> getCart(@PathVariable Long id);

    @PostMapping(value = "/cart/{id}")
    public ResponseEntity<CartItemBean> addProductToCart(@PathVariable Long id, @RequestBody CartItemBean cartItem);

    @PutMapping(value = "/cart")
    public ResponseEntity<CartBean> updateCart(@RequestBody CartBean cart);

    @GetMapping(value = "/products")
    List<ProductBean> allProduct();

    @GetMapping("/cart")
    CartBean get();
    @PostMapping("/cart")
    void addItem(@RequestBody CartItemBean cartItem);
    @PostMapping("/cart/remove/{id}")
    void removeItem(@PathVariable("id") Long id);
}
