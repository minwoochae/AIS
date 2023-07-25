package com.AIS.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.AIS.Dto.*;
import com.AIS.entity.Member;
import com.AIS.repository.MemberRepository;
import com.AIS.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Controller 
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberservice;
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;

	//문의하기
	@GetMapping(value = "/members/qa")
	public String qa() {
		return "member/qa";
	}
	//로그인 화면
	@GetMapping(value = "/members/login")
	public String loginmember() {
		return "member/memberLoginForm";
	}
	
	//회원가입 화면
	
	@GetMapping(value = "/members/new")
	public String memberForme(Model model) {
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "member/memberForm";
	}
	
	//회원가입
	@PostMapping(value = "/members/new")
	public String memberForme(@Valid MemberFormDto memberFormDto,
			BindingResult bindingResult, Model model) {
		//@valid: 유효성을 검증하려는 객체 앞에 붙인다.
		//BindingResult: 유효성 검증 후의 결과가 들어있다.
		
		if(bindingResult.hasErrors()) {
			//에러가 있다면 회원가입 페이지로 이동
			return "member/memberForm";
		}
		
		try {
			//MemberFormDto -> Member Entity, 비밀번호 암호화
			Member member = Member.createMember(memberFormDto, passwordEncoder);
			memberservice.saveMember(member);
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "member/memberForm";
		}
		
		return "redirect:/";
	}
	//로그인 실패했을때
	@GetMapping(value="/members/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
		return "member/memberLoginForm";
	}
	
	


	


	@GetMapping(value = "/members/idFind")
	public String findIdMember(Model model, @PathVariable("memberId") Long memberId) {
//		AiFormDto member =memberservice.getMemberDtl(memberId);
//		model.addAttribute("member", member);
		
		return "member/idLoginForm";
	}
	 @PostMapping("/members/find")
	 public String findMember(@RequestParam String findId, @RequestParam String findEmail, Model model) {
		 List<Member> members = new ArrayList<>();
	     Member foundMember = null;
	     for (Member member : members) {
	         if (member.getName().equals(findId) && member.getEmail().equals(findEmail)) {
	             foundMember = member;
	             break;
	         }
	     }

	     if (foundMember != null) {
	         model.addAttribute("members", foundMember);
	     }

	     return "member/LoginForm";
	 }

		
		@GetMapping(value = "/account/search")
		public String search_id(Model model) {
			model.addAttribute("memberFormDto", new MemberFormDto());
			return "member/LoginForm";
		}
	    
		

		@PostMapping("/account/search")
		@ResponseBody
	     public HashMap<String, String> members(@RequestBody Map<String, Object> data ) {
			String name = (String)data.get("memberName");
			String phone = (String)data.get("memberPhoneNumber");
			/*
			 * Member foundMember = memberRepository.findByNameAndPhoneNumber( name ,
			 * phoneNumber);
			 */
			HashMap<String, String> msg = new HashMap<>();
			String email = memberservice.emailFind(name, phone);
			
			msg.put("message", email);
			return msg;
	     }
	 
		/*
		 * @PostMapping("/members/idFind") public String findIdMember(@RequestParam
		 * String findId, @RequestParam String findEmail, Model model) { List<Member>
		 * members = new ArrayList<>(); Member foundMember = null; for (Member member :
		 * members) { if (member.getName().equals(findId) &&
		 * member.getEmail().equals(findEmail)) { foundMember = member; break; } }
		 * 
		 * if (foundMember != null) { model.addAttribute("members", foundMember); }
		 * 
		 * return "member/idLoginForm"; }
		 */
}
