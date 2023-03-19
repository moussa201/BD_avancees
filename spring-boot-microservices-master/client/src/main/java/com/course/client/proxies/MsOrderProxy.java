package com.course.client.proxies;


import com.course.client.beans.OrderBean;
import com.course.client.beans.ProductBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Exchanger;

@FeignClient(name = "ms-order", url = "localhost:8086")
public interface MsOrderProxy {


    @PostMapping(value = "/order")
    ResponseEntity<OrderBean> createNewOrder(@RequestBody OrderBean orderDomain);

    @GetMapping(value = "/orders")
    public List<OrderBean> getOrderList();

    @GetMapping(value = "/order/{id}")
    public Optional<OrderBean> getOrder(@PathVariable Long id);

    @PostMapping("/orders")
    public default OrderBean addOrder(@RequestBody OrderBean orderBean) {
        RestTemplate restTemplate = new RestTemplate();
        String orderServiceUrl = null;
        return restTemplate.postForObject(orderServiceUrl + "/orders", orderBean, OrderBean.class);
    }


    public default OrderBean submit(OrderBean orderBean) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderBean> request = new HttpEntity<>(orderBean, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<OrderBean> responseEntity = restTemplate.exchange(
                "/api/orders",
                HttpMethod.POST,
                request,
                OrderBean.class);

        return responseEntity.getBody();
    }

    @GetMapping("/products")
    public List<ProductBean> list();

}


