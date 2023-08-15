package com.AIS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AIS.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	//select * from member where email = ?
	Member findByEmail(String email);
	
	//select * from member where password = ?
    Member findByPassword(String password);
    
    Optional<Member> findById(Long memberId);
    Member findByName(String name);
	
    Member findByNameAndPhoneNumber(String name, String  phoneNumber);
    Member findByNameAndPhoneNumberAndEmail(String name, String  phoneNumber, String email);
    

}
