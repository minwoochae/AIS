package com.AIS.service;



import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.AIS.Dto.AiFormDto;
import com.AIS.Dto.MemberFormDto;
import com.AIS.entity.*;
import com.AIS.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@Service
@Transactional //쿼리문 수행시 에러가 발생하면 데이터를 이전 상대로 롤백시킨다.
@RequiredArgsConstructor  //@Autowired를 사용하지 않고 필드의 의존성 주입을 시켜준다.
public class MemberService implements UserDetailsService{
	private final MemberRepository memberRepository;
	
	//회원가입 데이터를 DB에 저장한다.
	public Member saveMember(Member member) {
		validateDuplicatMember(member); //이메일 중복체크
		Member savedMember = memberRepository.save(member); //insert
		return savedMember; //회원가입된 데이터를 리턴시켜준다.
	}
	
	//이메일 중복채크
	private void validateDuplicatMember(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		
		if(findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
	}
	//이메일 체크
	public void getNameMembers(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		if(findMember == null) {
			throw new IllegalStateException("존재하지 않은 회원입니다");
		}
	}
	public String emailFind(String name, String phone) {
		
		Member member =memberRepository.findByNameAndPhoneNumber(name, phone);
		
		if(member == null) {
			return "일치하는 사용자가 없습니다";
		}
		return member.getEmail();
	}
	
	public String passwordFind(String name, String phone , String email) {
		
		Member member =memberRepository.findByNameAndPhoneNumberAndEmail(name, phone , email);
		
		if(member == null) {
			return "일치하는 사용자가 없습니다";
		}
			
		System.out.println(member.getPassword());
		return member.getPassword();
	}
	
	
	//비번 수정
	public Long updateMember(MemberFormDto memberFormDto) throws Exception {
		//1.ai 앤티티 가져와서 바꾼다.
		Member member = memberRepository.findByPassword(memberFormDto.getName());
		
		member.updateMember(memberFormDto);
		

		
		return member.getId(); //변경한 ai의 id 리턴
	}

	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		//사용자가 입력한 email이 DB에 있는지 쿼리문을 사용한다.
		Member member = memberRepository.findByEmail(email);
		
		if(member ==null) {
				throw new UsernameNotFoundException(email);
		}
		
		//사용자가 있다면 DB에서 가져온 값으로 userDetails 객체를 만들어서 반환
		return User.builder()
				.username(member.getEmail())
				.password(member.getPassword())
				.roles(member.getRole().toString())
				.build();
	}
	
	
	
	
} 
