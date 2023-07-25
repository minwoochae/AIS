package com.AIS.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.AIS.Dto.AiSearchDto;
import com.AIS.Dto.IndexItemDto;
import com.AIS.service.AiService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GalleryController {
	private final AiService aiService;
	
	
	@GetMapping(value= {"/gallery", "/gallery/{page}" })
	public String gallery(AiSearchDto aiSearchDto , 
			@PathVariable("page") Optional<Integer> page, Model model ) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 4); 
		
		Page<IndexItemDto> ais = aiService.getGalleryPag(aiSearchDto, pageable);
		
		model.addAttribute("ais", ais);
		
		model.addAttribute("aiSearchDto", aiSearchDto);
		model.addAttribute("maxPage", 5); //분양관리 페이지 하단에 보여줄 최대 페이지 번호
		
		return "gallerymain/gallery";
	}
	
	

}
