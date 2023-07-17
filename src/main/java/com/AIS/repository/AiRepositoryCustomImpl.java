package com.AIS.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.AIS.constant.*;
import com.AIS.Dto.*;
import com.AIS.entity.*;

import jakarta.persistence.EntityManager;

public class AiRepositoryCustomImpl implements AiRepositoryCustom{

	private JPAQueryFactory queryFactory;
	
	public AiRepositoryCustomImpl(EntityManager em) {
		this.queryFactory =new JPAQueryFactory(em);
	}
	private BooleanExpression regDtsAfter(String searcgDateType) {
		LocalDateTime dateTime = LocalDateTime.now(); //현재날짜 , 시간
		
		if (StringUtils.equals("all",searcgDateType) || searcgDateType == null) return null;
		else if(StringUtils.equals("1d",searcgDateType))
			dateTime = dateTime.minusDays(1); // 현재날짜부터 1일전
		else if(StringUtils.equals("1w",searcgDateType))
			dateTime = dateTime.minusWeeks(1); // 현재날짜부터 1주일전
		else if(StringUtils.equals("1m",searcgDateType))
			dateTime = dateTime.minusMonths(1); // 현재날짜부터 1달전
		else if(StringUtils.equals("6m",searcgDateType))
			dateTime = dateTime.minusMonths(6); // 현재날짜부터 6개월전
			
		return QAi.ai.regtime.after(dateTime);
	}
	
	private BooleanExpression searchSellStatusEq(AiSellStatus searchSellStatus) {
		return searchSellStatus == null ? null : QAi.ai.aiSellStatus.eq(searchSellStatus);
	}
	
	
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if (StringUtils.equals("aiNm", searchBy)) {
			//등록자 검색시 
			return QAi.ai.aiNm.like("%" + searchQuery + "%"); //item_nm like %검색어%
		}else if(StringUtils.equals("createdBy",searchBy)) {
			return QAi.ai.createdBy.like("%" + searchQuery + "%"); //create_by like %검색어%
		}
		return null;
	}
	
	@Override
	public Page<Ai> getAdminItemPage(AiSearchDto aiSearchDto, Pageable pageable) {
		/* 
		 * select * from item where reg_time = ?
		 * and item_sell_status = ? and item_nm(create_by) like %검색어%
		 * order by item_id desc;
		 */
		List<Ai> content = queryFactory.selectFrom(QAi.ai)
										 .where(regDtsAfter(aiSearchDto.getSearchDateType()),
										 searchSellStatusEq(aiSearchDto.getSearchSellStatus()),
										 searchByLike(aiSearchDto.getSearchBy(), aiSearchDto.getSearchQuery()))										 
										 .orderBy(QAi.ai.id.desc())
										 .offset(pageable.getOffset())
										 .limit(pageable.getPageSize())
										 .fetch();
		
		long total = queryFactory.select(Wildcard.count)
							     .from(QAi.ai)
							     .where(regDtsAfter(aiSearchDto.getSearchDateType()),
										 searchSellStatusEq(aiSearchDto.getSearchSellStatus()),
										 searchByLike(aiSearchDto.getSearchBy(), aiSearchDto.getSearchQuery()))
							     .fetchOne();
		return new PageImpl<>(content,pageable,total);
	}
	
	private BooleanExpression aiNmLike(String searchQuery) {
		return StringUtils.isEmpty(searchQuery) ?
				null : QAi.ai.aiNm.like("%" + searchQuery + "%");
	}
	
	@Override
	public Page<IndexItemDto> getIndexItemPage(AiSearchDto aiSearchDto, Pageable pageable) {
		/*select ai_id, ai.aiNm , ai.aiDetail, ai_img.imgUrl, ai.price 
		 *from ai, ai_img 
		 *where ai.ai_id = ai_img.ai_id
		 *and ai.repimg_yn = 'Y'
		 *and ai.ai_nm like '%검색어%'
		 *order by ai.ai_id desc;
		 * */
		
		QAi ai = QAi.ai;
		QAiImg aiImg = QAiImg.aiImg;
		
		//dto로 객체로 바로 받아올 때는 
		//1.컬럼과 dto객체의 필드가 일치해야 한다.
		//2.dto 객체의 생성자에 @QueryProjection를 반드시 사용해야 한다.
		List<IndexItemDto> content = queryFactory
				.select(
						new QIndexItemDto(
								ai.id,
								ai.aiNm,
								ai.aiDetail,
								aiImg.imgUrl,
								ai.price)
						)
						.from(aiImg)
						.join(aiImg.ai, ai)
						.where(aiImg.repimgYn.eq("Y"))
						.where(aiNmLike(aiSearchDto.getSearchQuery()))
						.orderBy(ai.id.desc())
						.offset(pageable.getOffset())
						.limit(pageable.getPageSize())
						.fetch();
		
		long total = queryFactory
				.select(Wildcard.count)
				.from(aiImg)
				.join(aiImg.ai, ai)
				.where(aiImg.repimgYn.eq("Y"))
				.where(aiNmLike(aiSearchDto.getSearchQuery()))
				.fetchOne();
				
		return new PageImpl<>(content, pageable, total);
	}

}