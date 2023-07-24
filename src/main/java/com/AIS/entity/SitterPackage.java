package com.AIS.entity;



import java.time.LocalDateTime;
import java.util.List;

import com.AIS.Dto.PackageFormDto;
import com.AIS.constant.OrderStatus;
import com.AIS.constant.PackageOrder;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Table(name ="sitter_package")
@Getter
@ToString
public class SitterPackage extends BaseEntity{
	
	@Id
	@Column(name= "sitter_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch =FetchType.LAZY)
	@JoinColumn(name ="member_id")
	private Member member;
	
	
	@Column(nullable = false , length = 255)
	private String sitterNm;
	
	@Lob //clob과 같은 큰 타입의 문자 타입으로 컬럼을 만든다.
	@Column(nullable = false) //not null여부
	private String sitterDetail; //정보
	
	@Column(nullable = false) //not null여부
	private int sitterPrice; //가격
	
	@Enumerated(EnumType.STRING)
	private PackageOrder packageOrder; //YESORDER, NOORDER
	

	
	//ai 앤티티 수정
	public  void updateAi(PackageFormDto packageFormDto) {
		this.sitterNm = packageFormDto.getSitterNm();
		this.sitterPrice = packageFormDto.getSitterPrice();
		this.sitterDetail = packageFormDto.getSitterDetail();
		this.packageOrder = packageFormDto.getPackageOrder();
	}
}
