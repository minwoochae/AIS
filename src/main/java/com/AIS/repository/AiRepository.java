package com.AIS.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.AIS.Dto.AiRankDto;
import com.AIS.constant.AiSellStatus;
import com.AIS.entity.Ai;

public interface AiRepository extends JpaRepository<Ai, Long>,
AiRepositoryCustom{
		@Query("select i from Ai i where i.aiNm = :aiNm and i.aiSellStatus = :sell")
		List<Ai> getAiNmAndAiSellStatus(@Param("aiNm") String aiNm, @Param("sell") AiSellStatus sell);
		
		@Query(value ="select data.num num, ai.ai_id id, ai.ai_nm aiNm, ai.price price, ai_img.img_url imgUrl, ai_img.repimg_yn repimgYn \r\n"
				+ "            from ai \r\n"
				+ "			inner join ai_img on (ai.ai_id = ai_img.ai_id)\r\n"
				+ "			inner join (select @ROWNUM\\:=@ROWNUM+1 num, groupdata.* from\r\n"
				+ "			            (select order_ai.ai_id, count(*) cnt\r\n"
				+ "			              from order_id\r\n"
				+ "			              inner join orders on (order_ai.order_id = orders.order_id)\r\n"
				+ "			              where orders.order_status = 'ORDER'\r\n"
				+ "			             group by order_ai.ai_id order by cnt desc) groupdata,\r\n"
				+ "                          (SELECT @ROWNUM\\:=0) R \r\n"
				+ "                          limit 6) data\r\n"
				+ "			on (ai.ai_id = data.ai_id)\r\n"
				+ "			where ai_img.repimg_yn = 'Y'\r\n"
				+ "			order by num", nativeQuery = true)
		List<AiRankDto> getAiRankList();
}
