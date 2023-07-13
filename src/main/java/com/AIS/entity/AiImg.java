package com.AIS.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity //엔티티 클래스로 정의
@Table(name="Ai_img") //테이블 이름 지정
@Getter
@Setter
@ToString
public class AiImg extends BaseEntity {
	@Id
	@Column(name ="aimg_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String imgName; //동물 이미지 이름 //바뀐 이미지 파일명(중복 방지를 위해)
	
	private String oriImgName; //원본 이미지 파일명
	
	private String imgUrl; //이미지 조회 경로
	
	private String repimgYn; //대표 이미지 여부
	
	@ManyToOne(fetch =FetchType.LAZY)
	@JoinColumn(name ="sitter_id")
	private SitterPackage sitterPackage;
	
	@ManyToOne(fetch =FetchType.LAZY)
	@JoinColumn(name ="ai_id")
	private Ai ai;
	
	public void updateAiImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName ;
		this.imgName = imgName ; 
		this.imgUrl = imgUrl;
	}
	
}
