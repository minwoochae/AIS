package com.AIS.entity;



import com.AIS.Dto.AiFormDto;
import com.AIS.constant.AiSellStatus;
import com.AIS.exception.OutOfStockException;


import jakarta.persistence.*;
import lombok.*;

@Entity //엔티티 클래스로 정의
@Table(name="ai") //테이블 이름 지정
@Getter
@Setter
@ToString
public class Ai extends BaseEntity{
	@Id
	@Column(name ="ai_id")//테이블 명
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, length = 255) //not null여부, 컬럼 크기지정
	private String aiNm; //분양명
	
	@Column(nullable = false) //not null여부
	private int price; //가격
	
	@Column(nullable = false) //not null여부
	private int stockNumber; //분양여부 0 1 
	
	@Lob //clob과 같은 큰 타입의 문자 타입으로 컬럼을 만든다.
	@Column(nullable = false) //not null여부
	private String	aiDetail; // 동물 상세설명
	
	@Enumerated(EnumType.STRING) //enum의 이름을 DB에 저장
	private AiSellStatus aiSellStatus; //판매상태(SELL 혹은 SOLD_OUT) -> ai_sell_status
	
	//ai 앤티티 수정
	public  void updateAi(AiFormDto aiFormDto) {
		this.aiNm = aiFormDto.getAiNm();
		this.price = aiFormDto.getPrice();
		this.aiDetail = aiFormDto.getAiDetail();
		this.aiSellStatus = aiFormDto.getAiSellStatus();
	}
	//재고를  감소시킨다.
	public void removeStock(int stockNumber) {
		int restStock = this.stockNumber - stockNumber; //남은 재고 수정
		
		if(restStock < 0 ) {
			throw new OutOfStockException("분양이 진행중인 동물입니다,");
		}
		
		this.stockNumber = restStock; //남은 재고수량 반영
	}
	//재고증가
	public void addStock(int stockNumber) {
		this.stockNumber += stockNumber;
	}
}
