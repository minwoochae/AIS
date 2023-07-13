package com.AIS.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Table(name ="order_item")
@ToString
public class OrderItem extends BaseEntity{
	@Id
	@Column(name = "order_item_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "ai_id")
	private Ai ai;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "sitter_id")
	private SitterPackage sitterPackage;
	
	private int orderPrice; //가격
	private int count; //수량
	
	public static OrderItem createOrderItem(Ai ai , int count ) {
		OrderItem orderItem = new OrderItem();
		orderItem.setAi(ai);
		orderItem.setCount(count);
		orderItem.setOrderPrice(ai.getPrice());
		
		ai.removeStock(count);
		
		return orderItem;
	}
	
	
	public int getTotalPrice() {
		return orderPrice + count;
	}
	

	//재고를 원래대로
	public void cancel() {
		this.getAi().addStock(count);
		
	}
}
