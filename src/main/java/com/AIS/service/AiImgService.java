package com.AIS.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.AIS.entity.AiImg;
import com.AIS.repository.AiImgRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class AiImgService {
	private String aiImgLocation = "C:/ais/ai";
	
	private final AiImgRepository  aiImgRepository;
	private final FileService fileService;
	
	/*이미지 지정
	 * 1.파일을 aiImgLocation에 저장
	 * 2.ai_img 테이블에 저장
	*/
	
	public void saveAiImg(AiImg aiImg, MultipartFile aiImgFile) throws Exception{
		String oriImgName = aiImgFile.getOriginalFilename(); //파일이름 - > 이미지1.jpg
		String imgName= "";
		String imgUrl="";
		
		//1.파일을 aiImgLocation에 저장
		if(!StringUtils.isEmpty(oriImgName)) {
			//oriImgName이 반문자열이 아니라면 이미지 파일 업로드
			imgName = fileService.uploadFile(aiImgLocation, oriImgName, aiImgFile.getBytes());
			imgUrl = "/images/ai/" + imgName;
		}
		//2.ai_img 테이블에 저장 -> 이미지1.jpg , ERSFH4FDG0454.jpg, "")
		aiImg.updateAiImg(oriImgName, imgName, imgUrl);
			aiImgRepository.save(aiImg); //db에 insert
	}

	//이미지 업데이트 메소드
	public void updateAiImg(Long aiImgId, MultipartFile aiImgFile)
			throws Exception{
		if(!aiImgFile.isEmpty()) { //첨부한 이미지 파일이 있으면 
			AiImg savedAiImg  = aiImgRepository.findById(aiImgId)
					.orElseThrow(EntityNotFoundException:: new);
			//기존 이미지 파일 C:/ais/ai 폴더에서 삭제
			if(!StringUtils.isEmpty(savedAiImg.getImgName())) {
				fileService.deleteFile(aiImgLocation + "/" + savedAiImg.getImgName());
			}
			
			//수정된 이미지 파일 업로드 C:/ais/ai 에 업로드
			String oriImgName = aiImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(aiImgLocation, oriImgName, aiImgFile.getBytes());
			String imgUrl = "/images/ai/" + imgName;
			
			//update 쿼리문 실행
			/*
			 *한번 insert를 진행하면 앤티티가 영속성 컨텍스트에 저장 되므로
			 *그 이후로 변경 감지 기능이 동작하기 떄문에 개발자는 update쿼리문을 쓰지 않고
			 *앤티티만 변경해주면 된다.
			 */
			savedAiImg.updateAiImg(oriImgName, imgName, imgUrl);
		}
	}
}
