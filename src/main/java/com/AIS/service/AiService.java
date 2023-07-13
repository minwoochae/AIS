package com.AIS.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.AIS.Dto.*;
import com.AIS.entity.Ai;
import com.AIS.entity.AiImg;
import com.AIS.repository.AiImgRepository;
import com.AIS.repository.AiRepository;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class AiService {
	private  final AiRepository aiRepository;
	private final AiImgService  aiImgService;
	private final AiImgRepository  aiImgRepository;
	
	public Long saveAi(AiFormDto aiFormDto, List<MultipartFile> aiImgFileList) throws Exception {
		//1. 상품등록
		Ai ai = aiFormDto.createAi(); //dto -> entity
		aiRepository.save(ai);

		//2.이미지등록
		for(int i=0; i<aiImgFileList.size(); i++) {
			//부모테이블에 해당하는 entity를 먼저 넣어줘야 한다.
			AiImg aiImg = new AiImg();
			aiImg.setAi(ai);
			
			
			//첫번째 이미지 일때 대표상품 이미지 지정
			if(i == 0) {
				aiImg.setRepimgYn("Y");
			} else {
				aiImg.setRepimgYn("N");
			}
			
			aiImgService.saveAiImg(aiImg, aiImgFileList.get(i));
		}
		
		
		return ai.getId(); //등록된 상품 id 리턴
	}
	
	
	
	// 상품 가져오기
			@Transactional(readOnly = true) // 트랜잭션 읽기 전용(변경감지 수행하지 않음) -> 성능 향상
			public AiFormDto getAiDtl(Long aiId) {
				// 1. ai_img 테이블의 이미지를 가져온다.
				List<AiImg> aiImgList = aiImgRepository.findByAiIdOrderByIdAsc(aiId);
				
				// AiImg 엔티티 객체 -> AiImgDto로 변환
				List<AiImgDto> aiImgDtoList = new ArrayList<>();
				for(AiImg aiImg : aiImgList) {
					AiImgDto aiImgDto = AiImgDto.of(aiImg);
					aiImgDtoList.add(aiImgDto);
				}
				
				// 2. ai 테이블에 있는 데이터를 가져온다.
				Ai ai = aiRepository.findById(aiId)
										  .orElseThrow(EntityNotFoundException::new);
				
				// Ai 엔티티 객체 -> dto로 변환
				AiFormDto aiFormDto = AiFormDto.of(ai);
				
				// 3. FormDto에 이미지 정보(aiImgDtoList)를 넣어준다.
				aiFormDto.setAiImgDtoList(aiImgDtoList);
				
				return aiFormDto;		
			}
		
		public Long updateAi(AiFormDto aiFormDto, List<MultipartFile> aiImgFileList) throws Exception {
			//1.ai 앤티티 가져와서 바꾼다.
			Ai ai = aiRepository.findById(aiFormDto.getId())
							.orElseThrow(EntityNotFoundException::new);
			
			ai.updateAi(aiFormDto);
			
			//2. ai_img를 바꿔준다.->5개의 레코드 전부 변경
			List<Long> aiImgIds = aiFormDto.getAiImgIds(); //상품 이미지 아이디 리스트 조회
			
			for(int i=0; i<aiImgFileList.size(); i++) {
				aiImgService.updateAiImg(aiImgIds.get(i), aiImgFileList.get(i));
		
			}
			
			return ai.getId(); //변경한 ai의 id 리턴
		}
	
	
	
	
	
	
	
	@Transactional(readOnly = true)
	public Page<Ai> getAdminaiPage(AiSearchDto aiSearchDto, Pageable pageable){
		Page<Ai> aiPage = aiRepository.getAdminItemPage(aiSearchDto, pageable);
		return aiPage;
	}
	
	@Transactional(readOnly = true)
	public Page<IndexItemDto> getIndexItemPage (AiSearchDto aiSearchDto, Pageable pageable){
		Page<IndexItemDto> aiPage = aiRepository.getIndexItemPage(aiSearchDto, pageable);
		return aiPage;
	}
	
	@Transactional(readOnly = true)
	public List<AiRankDto> getAiRankList(){
		return aiRepository.getAiRankList();
		
	}
}
