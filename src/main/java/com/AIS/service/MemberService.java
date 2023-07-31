package com.AIS.service;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.AIS.Dto.MemberFormDto;
import com.AIS.entity.*;
import com.AIS.repository.MemberRepository;

import ch.qos.logback.core.encoder.Encoder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional // 쿼리문 수행시 에러가 발생하면 데이터를 이전 상대로 롤백시킨다.
@RequiredArgsConstructor // @Autowired를 사용하지 않고 필드의 의존성 주입을 시켜준다.
public class MemberService implements UserDetailsService {
	private final MemberRepository memberRepository;

	// 회원가입 데이터를 DB에 저장한다.
	public Member saveMember(Member member) {
		validateDuplicatMember(member); // 이메일 중복체크
		Member savedMember = memberRepository.save(member); // insert
		return savedMember; // 회원가입된 데이터를 리턴시켜준다.
	}

	// 이메일 중복채크
	private void validateDuplicatMember(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());

		if (findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
	}

	// 이메일 체크
	public void getNameMembers(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		if (findMember == null) {
			throw new IllegalStateException("존재하지 않은 회원입니다");
		}
	}

	public String emailFind(String name, String phone) {

		Member member = memberRepository.findByNameAndPhoneNumber(name, phone);

		if (member == null) {
			return "일치하는 사용자가 없습니다";
		}
		return member.getEmail();
	}

	public String passwordFind(String password) {

		Member member = memberRepository.findByPassword(password);

		if (member == null) {
			return "일치하는 사용자가 없습니다";
		}

		System.out.println(member.getPassword());
		return member.getPassword();
	}
	
	public String passwordFind(String name, String phone, String email) {

		Member member = memberRepository.findByNameAndPhoneNumberAndEmail(name, phone, email);

		if (member == null) {
			return "일치하는 사용자가 없습니다";
		}

		System.out.println(member.getPassword());
		return member.getPassword();
	}

	// 비번 수정
	public String updateMember(MemberFormDto memberFormDto , String password) throws Exception {
		// 1.ai 앤티티 가져와서 바꾼다.
		Member members = memberRepository.findByPassword(memberFormDto.getEmail());

		members.updateMember(memberFormDto);

		return memberFormDto.getPassword(); // 변경한 ai의 id 리턴
	}

	// 동물 정보 가져오기
	@Transactional(readOnly = true) // 트랜잭션 읽기 전용(변경감지 수행하지 않음) -> 성능 향상
	public MemberFormDto getmemberDtl(String password) {

		// 2. ai 테이블에 있는 데이터를 가져온다.
		Member member = memberRepository.findByPassword(password);

		// Ai 엔티티 객체 -> dto로 변환
		MemberFormDto memberFormDto = MemberFormDto.of(member);

		return memberFormDto;
	}
	
	
    public String getRamdomPassword(int size) {
        char[] charSet = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&' };
 
        StringBuffer sb = new StringBuffer();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());
 
        int idx = 0;
        int len = charSet.length;
        for (int i=0; i<size; i++) {
            // idx = (int) (len * Math.random());
            idx = sr.nextInt(len);    // 강력한 난수를 발생시키기 위해 SecureRandom을 사용한다.
            sb.append(charSet[idx]);
        }
 
        return sb.toString();
    }
    public String updatePassword(String pass,String email,PasswordEncoder passwordEncoder) {
    	Member member = memberRepository.findByEmail(email);
    	
    	String password = member.updatePassword(pass, passwordEncoder);
    	
    	return password;
    }
    
    
	public String passwordFin(String name, String phone, String email) {

		Member member = memberRepository.findByNameAndPhoneNumberAndEmail(name, phone, email);

		if (member == null) {
			return "일치하는 사용자가 없습니다";
		}

		System.out.println(member.getPassword());
		return member.getPassword();
	}
	public boolean checkPassword(String email, String checkPassword) {
    	   Member member = memberRepository.findByEmail(email);
    	   
    	   String realPassword = member.getPassword();
    	   BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
    	   boolean matches = bc.matches(checkPassword, realPassword);

    	return matches;
    }
    
    
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// 사용자가 입력한 email이 DB에 있는지 쿼리문을 사용한다.
		Member member = memberRepository.findByEmail(email);

		if (member == null) {
			throw new UsernameNotFoundException(email);
		}

		// 사용자가 있다면 DB에서 가져온 값으로 userDetails 객체를 만들어서 반환
		return User.builder().username(member.getEmail()).password(member.getPassword())
				.roles(member.getRole().toString()).build();
	}
	


}
