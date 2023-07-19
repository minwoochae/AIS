package com.AIS.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.AIS.Dto.*;
import com.AIS.entity.*;
import com.AIS.repository.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
	private final AiRepository aiRepository;
	private final MemberRepository memberRepository;
	private final OrderRepository orderRepository;
	private final AiImgRepository aiImgRepository;
	
	//분양 신청하기
	public Long order(OrderDto orderDto , String email) {
		//1 분양할 동물을 조회
		Ai ai =  aiRepository.findById(orderDto.getAiId())
							 .orElseThrow(EntityNotFoundException::new);
		//2.현재 로그인한 회원의 이메일을 이용해 회원정보를 조회
		Member member = memberRepository.findByEmail(email);
		
		//3.분양할 동물 앤티티의 분양수을 이용해여 분양대상 앤티티를 생성
		List<OrderItem> orderItemList = new ArrayList<>();
		OrderItem orderitem = OrderItem.createOrderItem(ai, orderDto.getCount());
		orderItemList.add(orderitem);
		
		//4. 회원정보와 분양 정보 리스트 이용하여 분양 앤티티를 생성
		Order order = Order.createOrder(member, orderItemList);
		orderRepository.save(order); // insert
		
		return order.getId();
		
	}

	//분양 목록을 가져오는 서비스
	@Transactional(readOnly = true)
	public  Page<OrderHistDto> getorderList(String email, Pageable pageable){
		
		//1. 유저 아이디와 페이징 조건을 이용하여 분양 목록을 조회
		List<Order> orders =orderRepository.findOrders(email, pageable);
		//2. 유저의 분양 총수를 구한다.
		Long totalcount = orderRepository.countOrder(email);
	
		
		//3. 분양 리스트를 순회하면서 구매 이력 페이지 전달형 DTO(orderHistDto)를 생성
		List<OrderHistDto> orderHistDtos = new ArrayList<>();
		for(Order order : orders) {
			OrderHistDto orderHistDto = new OrderHistDto(order);
			List<OrderItem> orderItems = order.getOrderItems();
			
			for(OrderItem orderItem : orderItems) {
				//동물의 대표 이미지 가져오기
				AiImg aiImg = aiImgRepository.findByAiIdAndRepimgYn(orderItem.getAi().getId(), "Y");
					OrderItemDto orderItemDto = new OrderItemDto(orderItem, aiImg.getImgUrl());
					orderHistDto.addOrderItemDto(orderItemDto);
					
			}
			orderHistDtos.add(orderHistDto);
		}
	
		
		return new PageImpl<>(orderHistDtos,pageable, totalcount); //4.페이지 구현 객체를 생성하여 return
	}
	
	

	//본인확인 (현재 로그인한 사용자와 분양 데이터를 생성한 사용자가 같은지 검사)
	public boolean validateOrder(Long orderId, String email) {
		Member curMember = memberRepository.findByEmail(email); //로그인한 사용자 찾기
		Order order = orderRepository.findById(orderId)
									 .orElseThrow(EntityNotFoundException::new); //분양 내역
		
		Member saveMember = order.getMember(); //분양한 사용자 찾기
		//로그인한 사용자의 이메일과 분양한 사용자의 이메일의 값이 같은지 최종 비교
		if(!StringUtils.equals(curMember.getEmail(), saveMember.getEmail())) {
			//같지 않으면
			return false;
		}
		return true;
	}
	
	//분양취소
	public void cancelOrder(Long orderId) {
		Order order = orderRepository.findById(orderId)
									 .orElseThrow(EntityNotFoundException::new);
		//OrderStatus를 update -> entity의 필드 값을 바꿔주면 된다.
		order.cancelOrder();
		
	}
	
	//입양 기록 삭제
	public void deleteOrder(Long orderId) {
		//+delete하기 전에 select 를 한번 해준다
		//-> 영속성 컨텍스트에 앤티티를 저장한 후 변경 감지를 하도록 하기 위해
		Order order = orderRepository.findById(orderId)
				.orElseThrow(EntityNotFoundException::new);
		
//		delete
		orderRepository.delete(order);
	}
}
