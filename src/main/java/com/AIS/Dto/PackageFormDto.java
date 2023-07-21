package com.AIS.Dto;

import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

import org.modelmapper.ModelMapper;

import com.AIS.constant.PackageOrder;
import com.AIS.entity.SitterPackage;




@Getter
@Setter
public class PackageFormDto {
	
	private Long id;
	
	@NotBlank(message = "동물명은 필수 입력입니다.")
	private String sitterNm;
	
	@NotNull(message = "가격은 필수 입력입니다.")
	private int sitterPrice;
	
	@NotNull(message = "재고는 필수 입력입니다.")
	private int stockNumber;

	
	@NotBlank(message = "동물 상세설명은 필수 입력입니다.")
	private String sitterDetail;
	
	private PackageOrder packageOrder;
	
//	//동물 이미지 정보를 저장
//		private List<AiImgDto> aiImgDtoList = new ArrayList<>();
//		
//		//동물 이미지 아이디들을 저장 -> 수정시에 이미지 아이디들을 담아둘 용도
		private List<Long> packageImgIds = new ArrayList<>();

		private static ModelMapper modelMapper = new ModelMapper();
	
	//dto -> entity로 바꿈
	public SitterPackage createSitterPackage() {
		return modelMapper.map(this, SitterPackage.class);
	}
	
	//entity -> dto로 바꿈
	public static PackageFormDto of(SitterPackage sitterPackage) {
		return modelMapper.map(sitterPackage, PackageFormDto.class);
	}
	
}
