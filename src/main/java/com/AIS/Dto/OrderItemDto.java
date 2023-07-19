package com.AIS.Dto;

import com.AIS.entity.OrderItem;

import lombok.*;

@Getter
@Setter
public class OrderItemDto {
	//앤티티 - > Dto로 바꿔준다.
		public OrderItemDto(OrderItem orderItem, String imgUrl) {
			this.itemNm = orderItem.getAi().getAiNm();
			this.count = orderItem.getCount();
			this.orderPrice =orderItem.getOrderPrice();
			this.imgUrl = imgUrl;
		}
		
		public String itemNm; //분양명
		
		
		public int count;
		
		public int orderPrice; //분양 비용
		
		public String imgUrl; //분양 이미지 경로
		
}
