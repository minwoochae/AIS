package com.AIS.Dto;

import com.AIS.constant.AiSellStatus;

import lombok.*;

@Getter
@Setter
public class AiSearchDto {
	private String searchDateType;
	private AiSellStatus searchSellStatus;
	private String searchBy;
	private String searchQuery= "";
}
