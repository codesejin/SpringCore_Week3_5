package com.sparta.springcore.model;

import com.sparta.springcore.dto.ProductRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProductTest { // 클래스 옆 실행 버튼은 클래스 내에 있는 모든 함수를 다 test
    @Test // Junit으로 test를 돌려주겠다
    @DisplayName("정상 케이스")
    void createProduct_Normal() { //함수 옆 실행버튼은 해당 함수에 대한 test
        // given // 샘플 값 : 이런 환경이 주어졌을 때
        Long userId = 100L;
        String title = "오리온 꼬북칩 초코츄러스맛 160g";
        String image = "https://shopping-phinf.pstatic.net/main_2416122/24161228524.20200915151118.jpg";
        String link = "https://search.shopping.naver.com/gate.nhn?id=24161228524";
        int lprice = 2350;

        ProductRequestDto requestDto = new ProductRequestDto(
                title,
                image,
                link,
                lprice
        );

        // when : 이코드가 실행되면
        // 생성자를 test해보겠다. 인자에 given넣어줌
        Product product = new Product(requestDto, userId);
        //requestDto 인자를 넣어주기 위해서는 ProductRequestDto dto 객체 생성해줘야함

        // then : 실행된 결과
        //assert:강하게 주장하다
        assertNull(product.getId());//인자값이 null이여야 한다고 강하게 주장, null이 아닐경우 에러
        assertEquals(userId, product.getUserId()); // 인자 2개 : 기대값, 실제값
        assertEquals(title, product.getTitle());
        assertEquals(image, product.getImage());
        assertEquals(link, product.getLink());
        assertEquals(lprice, product.getLprice());
        assertEquals(0, product.getMyprice());
    }
}