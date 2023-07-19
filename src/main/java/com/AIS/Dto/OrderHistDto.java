package com.AIS.Dto;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.AIS.constant.OrderStatus;
import com.AIS.entity.Order;

import lombok.*;

@Getter
@Setter
public class OrderHistDto {
	public OrderHistDto(Order order) {
		this.orderId = order.getId();
		this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		this.orderStatus =order.getOrderStatus();
}
	private Long orderId; //분양아이디
	
	private String orderDate; //분양날짜
	
	private OrderStatus orderStatus;
	
	private List<OrderItemDto> orderItemDtoList = new ArrayList<>();
	
	//orderItemDto 객체를 분양 리스트에 추가 하는 메소드
	public void addOrderItemDto(OrderItemDto orderAiDto) {
		this.orderItemDtoList.add(orderAiDto);
	}
}
