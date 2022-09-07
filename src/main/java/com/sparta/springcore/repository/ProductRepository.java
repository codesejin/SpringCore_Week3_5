package com.sparta.springcore.repository;

import com.sparta.springcore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //userId로 상품 전체를 찾는다(4주차에 배움)
    List<Product> findAllByUserId(Long userId);//원래 있던 함수가 아니라 만들어줌(4주차에배움)
}