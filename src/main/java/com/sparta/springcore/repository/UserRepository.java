package com.sparta.springcore.repository;

import com.sparta.springcore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //유저에 대한 테이블에서 Username을 찾겠다는 의미
    //인자로 넘어오는 이 유저네임을 갖고 db에서 뒤져보겠다는 내용
    //결과는 옵셔널 유저로 나오라고 선언해줌
    //스프링 데이터 JPA위에서 쿼리가 된다
    Optional<User> findByUsername(String username);
    Optional<User> findByKakaoId(Long KakaoId);
}