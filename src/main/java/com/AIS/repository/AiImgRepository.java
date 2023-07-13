package com.AIS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AIS.entity.AiImg;


public interface AiImgRepository extends JpaRepository<AiImg, Long>{
	//select * from item_img where item_id =? order by item_id asc;
	List<AiImg> findByAiIdOrderByIdAsc(Long aiId);
	
	//select * from item_img where item_id = ? and repimg_yn = ?
	AiImg findByAiIdAndRepimgYn(Long aiId, String repimgYn);
}
