package com.AIS.entity;


import java.time.LocalDateTime;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Table(name ="sitter_package")
@Getter
@ToString
public class SitterPackage extends BaseEntity{
	
	@Id
	@Column(name= "sitter_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false , length = 255)
	private String sitternm;
	
	@Lob //clob과 같은 큰 타입의 문자 타입으로 컬럼을 만든다.
	@Column(nullable = false) //not null여부
	private String sitterdetail;
	
	private int sitterprice;
	
	private LocalDateTime sitterdate;
	
}
