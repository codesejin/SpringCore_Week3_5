package com.sparta.springcore.validator;

import com.sparta.springcore.dto.ProductRequestDto;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;


/**
 * Component : 빈으로 등록하고, 해당 빈을 DI받아서 사용
 * DI을 받을 수 있는 조건 : 스프링 빈 끼리만 DI를 받을 수 있다
 * 그래서 Product클래스에서 private final ProductValidator productValidator; 해서 DI받으려 했으나
 * 생성자를 새로 만들어서 함수 사용함
 */
public class ProductValidator {

    // 입력값 Validation
    /**
     *  에러를 보면 모두 같은 IllegalArgumentException을 주고 있는데,
     * 예를 들어 InvalidUserIdException나 InvalidProdcutTitleException으로 만들어 줄 수 있음
     * 이것에 대한 장점은, 에러마다 다른 성격의 Exception을 만들어줌으로써 구분될 수 있다.
     * 지금같은 경우의 IllegalArgumentException만 test한다고 가정하면
     * 테스트코드의 when부분에서 IllegalArgumentException만 실행이 되게되는데
     * 그러면 title에서도 에러가 날 가능성을 놓치게 된다
     * 그래서 테스트코드가 성공을 해도 잘못된 테스트이게 됌!!
     * 그래서 메세지 비교로 에러 위치를 확인하는 방식으로 사용하고 있음
     */
    public void validateProductInput(ProductRequestDto requestDto, Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("회원 Id 가 유효하지 않습니다.");
        }

        if (requestDto.getTitle() == null || requestDto.getTitle().isEmpty()) {
            throw new IllegalArgumentException("저장할 수 있는 상품명이 없습니다.");
        }

        if (!isValidUrl(requestDto.getImage())) {
            throw new IllegalArgumentException("상품 이미지 URL 포맷이 맞지 않습니다.");
        }

        if (!isValidUrl(requestDto.getLink())) {
            throw new IllegalArgumentException("상품 최저가 페이지 URL 포맷이 맞지 않습니다.");
        }

        if (requestDto.getLprice() <= 0) {
            throw new IllegalArgumentException("상품 최저가가 0 이하입니다.");
        }
    }

    private boolean isValidUrl(String url)
    {
        try {
            //URL은 url형태로 만들어주는 java.net.class
            new URL(url).toURI();
            return true;
        }
        //원하던 url패스가 아닐경우 에러
        catch (URISyntaxException exception) {
            return false;
        }
        catch (MalformedURLException exception) {
            return false;
        }
    }
}
