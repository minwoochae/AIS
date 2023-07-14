package com.AIS.Dto;


import java.util.*;

import org.modelmapper.ModelMapper;

import com.AIS.constant.AiSellStatus;
import com.AIS.entity.Ai;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class AiFormDto {
	private Long id;
	
	@NotBlank(message = "상품명은 필수 입력입니다.")
	private String aiNm;
	
	@NotNull(message = "가격은 필수 입력입니다.")
	private int price;
	@NotNull(message = "재고는 필수 입력입니다.")
	private int stockNumber;

	
	@NotBlank(message = "상품 상세설명은 필수 입력입니다.")
	private String aiDetail;
	
	private AiSellStatus aiSellStatus;
	
	//상품 이미지 정보를 저장
		private List<AiImgDto> aiImgDtoList = new ArrayList<>();
		
		//상품 이미지 아이디들을 저장 -> 수정시에 이미지 아이디들을 담아둘 용도
		private List<Long> aiImgIds = new ArrayList<>();
		
		private static ModelMapper modelMapper = new ModelMapper();
	
	//dto -> entity로 바꿈
	public Ai createAi() {
		return modelMapper.map(this, Ai.class);
	}
	
	//entity -> dto로 바꿈
	public static AiFormDto of(Ai ai) {
		return modelMapper.map(ai, AiFormDto.class);
	}
	
}
