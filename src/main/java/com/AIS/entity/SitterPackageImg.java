package com.AIS.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity //엔티티 클래스로 정의
@Table(name="sitter_package_img") //테이블 이름 지정
@Getter
@Setter
@ToString
public class SitterPackageImg  extends BaseEntity {
	
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
	@OnDelete(action = OnDeleteAction.CASCADE)
	private SitterPackage sitterPackage;
	
	
	public void updateSitterImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName ;
		this.imgName = imgName ; 
		this.imgUrl = imgUrl;
	}
	
	
}
