package com.AIS.controller;

import java.security.Principal;
import java.util.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.AIS.Dto.*;
import com.AIS.entity.Member;
import com.AIS.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberservice;
	private final PasswordEncoder passwordEncoder;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

	// 비밀번호 찾고 난수생성기로 랜덤비밀번호 생성
	@PostMapping("/account/pssearch")
	@ResponseBody
	public HashMap<String, String> memberps(@RequestBody Map<String, Object> psdata) {

		String name = (String) psdata.get("memberName");
		String phone = (String) psdata.get("memberPhoneNumber");
		String email = (String) psdata.get("memberEmail");

		HashMap<String, String> msg = new HashMap<>();
		String pass = memberservice.passwordFind(name, phone, email);
		// pass 암호화된 비밀번호
		String ramdomps = memberservice.getRamdomPassword(12);

		// ramdomps 를 view에 출력
		String password = memberservice.updatePassword(ramdomps, email, passwordEncoder);

		msg.put("message", ramdomps);

		return msg;

	}

	@GetMapping("/checkPwd")
	public String checkPwdView(Model model) {
		model.addAttribute("passwordDto",new PasswordDto());
		
		
		return "member/checkPwd";
	}
	
	/** 회원 수정 전 비밀번호 확인 **/
	@PostMapping(value = "/checkPwd")
	public String checkPwd(@Valid PasswordDto passwordDto,Principal principal,Model model) {
		
		Member member = memberservice.findByEmail(principal.getName());
		
		boolean result = bCryptPasswordEncoder.matches(passwordDto.getPassword(), member.getPassword());
		
		if(!result) {
			model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
			return "member/checkPwd";
		}
		
		return "member/EditMember/" + member.getId();
	} 
	 

	
	
	@GetMapping("/member/EditMember" )
	public String EditMembers(@Valid PasswordDto passwordDto,Principal principal,Model model) {
		try {
			MemberFormDto memberFormDto = memberservice.getmemberDtl(principal.getName());
			model.addAttribute("memberFormDto",memberFormDto);
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("memberFormDto", new MemberFormDto());
			return "member/checkPwd";
		}
	
		
		return "member/EditMember";
	}

	@PostMapping(value = "/member/EditMember")
	@ResponseBody
	public String aiUpdate(@Valid MemberFormDto memberFormDto, Model model, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return "member/EditMember";
		}

		try {
			memberservice.updateMember(memberFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "회원 수정 중 에러가 발생했습니다.");
			return "member/EditMember";
		}

		return "redirect:/";
	}  


}
