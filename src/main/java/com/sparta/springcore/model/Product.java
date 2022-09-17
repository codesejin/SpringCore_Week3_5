package com.sparta.springcore.model;
import com.sparta.springcore.dto.ProductRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Product {

    // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    // 반드시 값을 가지도록 합니다.
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private int lprice;

    @Column(nullable = false)
    private int myprice;

    @Column(nullable = false)
    private Long userId;

    // 관심 상품 생성 시 이용합니다. (Edge케이스 처리하는 조건문 추가)
    public Product(ProductRequestDto requestDto, Long userId) {
        //EdgeCase에 대한 Validation
        validateProdcutInput(requestDto, userId);

        // 관심상품을 등록한 회원 Id 저장
        this.userId = userId;
        this.title = requestDto.getTitle();
        this.image = requestDto.getImage();
        this.link = requestDto.getLink();
        this.lprice = requestDto.getLprice();
        this.myprice = 0;
    }
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
    private void validateProdcutInput(ProductRequestDto requestDto, Long userId) {
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

    boolean isValidUrl(String url)
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