package com.kvp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;

	@OneToOne(mappedBy = "payment", cascade =CascadeType.ALL)
	private Order order;

	private String paymentMethod;

}