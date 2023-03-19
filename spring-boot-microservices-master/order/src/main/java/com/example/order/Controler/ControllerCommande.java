package com.example.order.Controler;

import com.example.order.Commande.Commande;
import com.example.order.repositories.OrderItemRepository;
import com.example.order.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
public class ControllerCommande {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PostMapping(value = "/order")
    public ResponseEntity<Commande> createNewOrder(@RequestBody Commande orderDomain)
    {
        Commande orderSaved = orderRepository.save(orderDomain);

        if (orderSaved == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't create a new cart");

        return new ResponseEntity<Commande>(orderSaved, HttpStatus.CREATED);
    }

    @GetMapping(value = "/orders")
    public List<Commande> getOrderList()
    {
        return orderRepository.findAll();
    }


    @GetMapping(value = "/order/{id}")
    public Optional<Commande> getOrder(@PathVariable Long id)
    {
        Optional<Commande> orderDomain = orderRepository.findById(id);

        if (orderDomain == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't get cart");

        return orderDomain;
    }

}
