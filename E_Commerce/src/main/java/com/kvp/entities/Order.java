package com.kvp.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	
	@NonNull
	private String email;

	@OneToMany(mappedBy = "order", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<OrderItem> orderItems = new ArrayList<>();

	private LocalDate orderDate;
	
	@OneToOne
	@JoinColumn(name = "payment_id")
	private Payment payment;
	
	private Double totalAmount;
	private String orderStatus;
}