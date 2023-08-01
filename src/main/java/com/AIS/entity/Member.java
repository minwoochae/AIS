package com.AIS.entity;

import lombok.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.AIS.Dto.AiFormDto;
import com.AIS.Dto.MemberFormDto;
import com.AIS.constant.Role;

import jakarta.persistence.*;


@Entity
@Table(name="member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity {
	@Id
	@Column(name="member_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id; //멤버 아이디

	@Column(unique = true, length=255)
	private String email; //이메일
	
	@Column(nullable = false, length = 255)
	private String name; //이름
	
	@Column(nullable = false, length = 255)
	private String password;//비밀번호
	
	@Column(nullable = false)
	private String phoneNumber;// 폰번호
	
	private String address;
	
	@Enumerated(EnumType.STRING)
	private Role role;//역할
	
	
	public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
		String password = passwordEncoder.encode(memberFormDto.getPassword());
		
		Member member = new Member();
		member.setName(memberFormDto.getName());
		member.setEmail(memberFormDto.getEmail());
		member.setAddress(memberFormDto.getAddress());
		member.setPhoneNumber(memberFormDto.getPhoneNumber());
		member.setPassword(password);
		member.setRole(Role.ADMIN);
		
		return member;
	}
	public void editmembers(MemberFormDto memberFormDto,PasswordEncoder passwordEncoder) {
			String password = passwordEncoder.encode(memberFormDto.getPassword());
			this.password = password;
			this.name = memberFormDto.getName();
			this.address = memberFormDto.getAddress();
			this.phoneNumber = memberFormDto.getPhoneNumber();
			this.email = memberFormDto.getEmail();
			
		}
	
	public  void updateMember(MemberFormDto memberFormDto) {
		this.password = memberFormDto.getPassword();
	}
	public String  updatePassword(String pass,PasswordEncoder passwordEncoder) {
		String password = passwordEncoder.encode(pass);
		this.password = password;
		
		return password;
	}
	public String  createPassword(String pass , String getpass) {
		this.password = pass;
		
		return password;
	}





}
