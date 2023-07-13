package com.AIS.Dto;

import org.modelmapper.ModelMapper;

import com.AIS.entity.AiImg;

import lombok.*;

@Getter
@Setter
public class AiImgDto {
	private Long id;
	
	private String imgName;
	
	private String oriImgName; //원본 이미지 파일명
	
	private String imgUrl; //이미지 조회 경로
	
	private String repimgYn; //대표 이미지 여부
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static AiImgDto of(AiImg aiImg) {
		return modelMapper.map(aiImg, AiImgDto.class);
	}	
}
