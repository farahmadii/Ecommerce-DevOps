package com.example.demo.controllers;

import java.util.List;

import com.example.demo.model.persistence.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private final static Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		logger.info("Attempting to submit order for user: " + username);
		User user = userRepository.findByUsername(username);
		if(user == null) {
			logger.info("submit request failed, Reason: user not found.");
			return ResponseEntity.notFound().build();
		}
		Cart cart = user.getCart();
		if(cart.getItems().size() == 0) {
			logger.info("submit request failed, Reason: cart is empty.");
			return ResponseEntity.badRequest().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		user.setCart(new Cart());
		userRepository.save(user);
		logger.info("order places successfully for user: {}, response code: 200" + username);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		logger.info("Attempting to get order history for user: " , username);
		User user = userRepository.findByUsername(username);
		if(user == null) {
			logger.info("get history request failed, Reason: user not found.");
			return ResponseEntity.notFound().build();
		}
		logger.info("get history successfully done for user: {}, response code: 200" , username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
