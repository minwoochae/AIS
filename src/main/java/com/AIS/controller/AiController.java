package com.AIS.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.AIS.Dto.AiFormDto;
import com.AIS.Dto.AiSearchDto;
import com.AIS.Dto.IndexItemDto;
import com.AIS.entity.Ai;
import com.AIS.service.AiService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor //의존성 주입 위해 사용
public class AiController {
	private final AiService aiService;
	
	//분양 리스트
	@GetMapping(value = "/ai/animalInformation")
	public String animalInformationList(Model model, AiSearchDto aiSearchDto, Optional<Integer> page ) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 6);
		Page<IndexItemDto> ais =aiService.getIndexItemPage(aiSearchDto, pageable);
		
		model.addAttribute("ais", ais);
		model.addAttribute("aiSearchDto", aiSearchDto);
		model.addAttribute("maxPage", 5);
		
		return "ai/animalInformationList";
	}
	
	// 분양등록 페이지
	@GetMapping(value = "/admin/ai/new")
	public String aiForm(Model model) {
		model.addAttribute("aiFormDto", new AiFormDto());
		
		return "ai/aiForm";
	}

	
	// 분양전체 리스트
	@GetMapping(value = "/ai/ais")
	public String aiShopList(Model model, AiSearchDto aiSearchDto, Optional<Integer> page)  {
			
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 4);
		Page<IndexItemDto> ais = aiService.getIndexItemPage(aiSearchDto, pageable);
			
		model.addAttribute("ais",ais);
		model.addAttribute("aiSearchDto",aiSearchDto);
		model.addAttribute("maxPage",5);
			
		
		return "ai/aiShopList";
	}
	
	//분양 상세 페이지
	@GetMapping(value = "/ai/{aiId}")
	public String aiDtl(Model model, @PathVariable("aiId") Long aiId) {
		AiFormDto aiFormDto =aiService.getAiDtl(aiId);
		model.addAttribute("ai", aiFormDto);
		return "ai/aiDtl";
	}
	
	
	
	//분양, 분양이미지 등록(insert)
	@PostMapping(value = "/admin/ai/new")
	public String aiNew(@Valid AiFormDto aiFormDto, BindingResult bindingResult, Model model, @RequestParam("aiImgFile") List<MultipartFile> aiImgFileList){
		if(bindingResult.hasErrors()) {
			return "ai/AiForm";
		}
		//분양등록전에 첫번째 이미지가 있는지 없느지 검사(첫번째 이미지는 필수 입력값)
		if(aiImgFileList.get(0).isEmpty()) {
			model.addAttribute("errorMessage", "첫번째 분양 이미지는 필수 입니다.");
			return "ai/aiForm";
		}
		
		try {
			aiService.saveAi(aiFormDto, aiImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage",  "분양 등록 중 에러가 발생했습니다.");
			return "ai/aiForm";
		}
		
		return "redirect:/";
	}
	
	// 분양 관리 페이지
		@GetMapping(value = {"/admin/ais", "/admin/ais/{page}"})
		public String aiManage(AiSearchDto aiSearchDto , 
				@PathVariable("page") Optional<Integer> page, Model model ) {
			//of(조회할 페이지의 번호: *0부터 시작, 한페이지당 조회할 데이터 갯수)
			//url경로에 페이지가 있으면 해당 페이지 번호를 조회하도록 하고 페이지 번호가 없으면 0페이지를 조회
			Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 3); 
			
			Page<Ai> ais = aiService.getAdminaiPage(aiSearchDto, pageable);
			
			model.addAttribute("ais", ais);
			
			model.addAttribute("aiSearchDto", aiSearchDto);
			model.addAttribute("maxPage", 5); //분양관리 페이지 하단에 보여줄 최대 페이지 번호
			
			return "ai/aimng";
		}
		

		// 분양 수정 페이지 화면 보기
		@GetMapping(value = "/admin/ai/{aiId}")
		public String aiDtl(@PathVariable("aiId") Long aiId, Model model) {
			
			try {
				AiFormDto aiFormDto = aiService.getAiDtl(aiId);
				model.addAttribute("aiFormDto", aiFormDto);
				
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("errorMessage",  "분양 수정 페이지를 불러오는 중 에러가 발생했습니다.");
				model.addAttribute("aiFormDto", new AiFormDto());
				return "ai/aiForm";
			}

			return "ai/aiModifyForm";
		}
		
			// 분양 수정
			@PostMapping(value = "/admin/ai/{aiId}")
			public String aiUpdate(@Valid AiFormDto aiFormDto, Model model,
								     BindingResult bindingResult, @RequestParam("aiImgFile") List<MultipartFile> aiImgFileList) {
				if(bindingResult.hasErrors()) {
					return "ai/aiForm";
				}
				
				// 첫번째 이미지가 있는지 검사
				if(aiImgFileList.get(0).isEmpty() && aiFormDto.getId() == null) {
					model.addAttribute("errorMessage", "첫번째 분양 이미지는 필수 입니다.");
					return "ai/aiForm";
				}
				
				try {
					
					aiService.updateAi(aiFormDto, aiImgFileList);
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("errorMessage", "분양 수정 중 에러가 발생했습니다.");
					return "ai/aiForm";
				}
				
				return "redirect:/";
			}
			//분양 기록 삭제
			@DeleteMapping(value = "/admin/{aiId}/delete")
			public @ResponseBody ResponseEntity deleteOrder(@RequestBody @PathVariable("aiId") Long aiId,
						Principal principal) {
				
				//2. 입약 기록 삭제
				aiService.deleteAi(aiId);
				
				return new ResponseEntity<Long>(aiId, HttpStatus.OK);
			}
			
		
	
}
