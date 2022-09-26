package com.sparta.springcore.repository;

import com.sparta.springcore.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    /*
    스프링프레임워크에서 만들어진 Spring data jpa의 pageable 사용하면 페이징 가능
    페이징 관련된 정보를 매개인자로 넣어주면 결과로 List가 아니라 Page로 나타내줌
    */
    Page<Product> findAllByUserId(Long userId, Pageable pageable);
}