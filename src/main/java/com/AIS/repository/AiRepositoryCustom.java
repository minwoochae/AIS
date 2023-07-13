package com.AIS.repository;

import org.springframework.data.domain.*;

import com.AIS.Dto.AiSearchDto;
import com.AIS.Dto.IndexItemDto;
import com.AIS.entity.Ai;


public interface AiRepositoryCustom {
	Page<Ai> getAdminItemPage(AiSearchDto aiSearchDto, Pageable pageable);
	
	Page<IndexItemDto> getIndexItemPage(AiSearchDto aiSearchDto, Pageable pageable);
}
