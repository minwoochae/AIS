package com.AIS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AIS.entity.AiImg;


public interface AiImgRepository extends JpaRepository<AiImg, Long>{
	List<AiImg> findByAiIdOrderByIdAsc(Long aiId);
	
	AiImg findByAiIdAndRepimgYn(Long aiId, String repimgYn);
}
