package com.course.client.controllers;

import com.course.client.beans.*;
import com.course.client.proxies.MsCartProxy;
import com.course.client.proxies.MsOrderProxy;
import com.course.client.proxies.MsProductProxy;
import com.course.client.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ClientController {

    @Autowired
    private MsProductProxy msProductProxy;

    @Autowired
    private MsCartProxy msCartProxy;

    @Autowired
    private MsOrderProxy msOrderProxy;

    @Autowired
    private ClientService clientService;

    @GetMapping("/")
    public String index(Model model) {
        List<ProductBean> products = msProductProxy.list();
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/details/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        Optional<ProductBean> product = msProductProxy.get(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "details";
        }
        return "error";
    }

    @PostMapping("/cart/{id}")
    public String addToCart(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity) {
        // Get the selected product from the product microservice
        Optional<ProductBean> product = msProductProxy.get(id);
        // If the product exists, add it to the shopping cart microservice
        if (product.isPresent()) {
            // Add the product to the shopping cart microservice
            CartItemBean cartItemBean = new CartItemBean(product.get(), quantity);
            msCartProxy.addItem(cartItemBean);
            return "redirect:/cart"; // Redirect the user to the shopping cart page
        }
        return "error";
    }

    @GetMapping("/cart")
    public String viewCart(Model model) throws Exception {
        // Get the contents of the shopping cart microservice
        CartBean cartBean = msCartProxy.get();
        List<CartItemBean> cartItemBeanList = cartBean.getProducts();
        // Convert cart items to product final beans
        List<finalProduit> productFinalBeanList = clientService.CartToProductFinalBean(cartItemBeanList);
        // Calculate the total price of the cart
        Double totalPrice = clientService.totalPrice(productFinalBeanList);
        // Set the model attributes for the cart view
        model.addAttribute("cart", cartBean);
        model.addAttribute("products", productFinalBeanList);
        model.addAttribute("totalPrice", totalPrice);
        return "cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeCartItem(@PathVariable("id") Long id) {
        msCartProxy.removeItem(id);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) throws Exception {
        // Get the contents of the shopping cart microservice
        CartBean cartBean = msCartProxy.get();
        // Convert cart items to order items
        List<OrderItemBean> orderItemBeanList = clientService.ListCartItemToListOrderItem(cartBean.getProducts());
        // Calculate the total price of the cart
        Double totalPrice = clientService.totalPrice(clientService.CartToProductFinalBean(cartBean.getProducts()));
        // Create an order bean
        OrderBean orderBean = new OrderBean(orderItemBeanList, totalPrice, cartBean.getId());
        // Submit the order to the order microservice
        OrderBean submittedOrder = msOrderProxy.submit(orderBean);
        // Set the model
        model.addAttribute("order", submittedOrder);
        return "checkout";
    }

    @GetMapping("/orders")
    public String viewOrders(Model model) throws Exception {
        // Get the orders for the current user
        List<ProductBean> orders = msOrderProxy.list();
        // Set the model attribute for the orders view
        model.addAttribute("orders", orders);
        return "orders";
    }
}