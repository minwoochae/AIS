package com.AIS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AIS.entity.Member;


public interface MemberRepository extends JpaRepository<Member, Long> {
	//select * from member where email = ?
	Member findByEmail(String email);
}
