package com.AIS.controller;

import java.util.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.AIS.Dto.*;
import com.AIS.entity.Member;
import com.AIS.repository.MemberRepository;
import com.AIS.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberservice;
	private final PasswordEncoder passwordEncoder;

	// 문의하기
	@GetMapping(value = "/members/qa")
	public String qa() {
		return "member/qa";
	}

	// 로그인 화면
	@GetMapping(value = "/members/login")
	public String loginmember() {
		return "member/memberLoginForm";
	}

	// 회원가입 화면

	@GetMapping(value = "/members/new")
	public String memberForme(Model model) {
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "member/memberForm";
	}

	// 회원가입
	@PostMapping(value = "/members/new")
	public String memberForme(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
		// @valid: 유효성을 검증하려는 객체 앞에 붙인다.
		// BindingResult: 유효성 검증 후의 결과가 들어있다.

		if (bindingResult.hasErrors()) {
			// 에러가 있다면 회원가입 페이지로 이동
			return "member/memberForm";
		}

		try {
			// MemberFormDto -> Member Entity, 비밀번호 암호화
			Member member = Member.createMember(memberFormDto, passwordEncoder);
			memberservice.saveMember(member);
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "member/memberForm";
		}

		return "redirect:/";
	}

	// 로그인 실패했을때
	@GetMapping(value = "/members/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
		return "member/memberLoginForm";
	}

	// 아이디 찾기
	@GetMapping(value = "/account/search")
	public String search_id(Model model) {
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "member/LoginForm";
	}

	@PostMapping("/account/search")
	@ResponseBody
	public HashMap<String, String> members(@RequestBody Map<String, Object> data) {
		String name = (String) data.get("memberName");
		String phone = (String) data.get("memberPhoneNumber");
		/*
		 * Member foundMember = memberRepository.findByNameAndPhoneNumber( name ,
		 * phoneNumber);
		 */
		HashMap<String, String> msg = new HashMap<>();
		String email = memberservice.emailFind(name, phone);

		msg.put("message", email);
		return msg;
	}

	// 비번찾기
	@GetMapping(value = "/account/pssearch")
	public String search_ps(Model model) {
		model.addAttribute("memberFormDto", new MemberFormDto());
		
		return "member/psLoginForm";
	}

	@PostMapping("/account/pssearch")
	@ResponseBody
	public HashMap<String, String> memberps(@RequestBody Map<String, Object> psdata) {
		String name = (String) psdata.get("memberName");
		String phone = (String) psdata.get("memberPhoneNumber");
		String email = (String) psdata.get("memberEmail");

		HashMap<String, String> msg = new HashMap<>();
		String pass = memberservice.passwordFind(name, phone, email);
		
		msg.put("message", pass);
		return msg;
		
	}
	
	
	// 분양 수정 페이지 화면 보기
	@GetMapping(value = "/account/pssearch/{memberId}")
	public String memberDtl(@Valid  MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
			model.addAttribute("memberFormDto", new MemberFormDto());

		return "member/memberLoginForm";
	}
	
		// 분양 수정
		@PostMapping(value = "/account/pssearch/{memberId}")
		public String memberUpdate(@Valid MemberFormDto memberFormDto, Model model,
							     BindingResult bindingResult) {
			if(bindingResult.hasErrors()) {
				return "/account/pssearch";
			}
			
			try {
				memberservice.updateMember(memberFormDto);
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("errorMessage", "분양 수정 중 에러가 발생했습니다.");
				return "/account/pssearch";
			}
			
			return "member/memberLoginForm";
		}

}
