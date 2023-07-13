package com.AIS.entity;


import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.*;

@EntityListeners(value = (AuditingEntityListener.class)) //Auditing를 적용하기 
@MappedSuperclass //부모 클래스를 상속받는 자식 클래스한테 매핑정보를 제공하기 위해 사용
@Getter
@Setter
public class BaseTimeEntity {
	@CreatedDate
	@Column(updatable =  false)
	private LocalDateTime regtime;
	
	@LastModifiedDate
	private LocalDateTime updateTime; //수정날짜
}
