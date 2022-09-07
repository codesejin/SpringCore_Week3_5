package com.sparta.springcore.controller;

import com.sparta.springcore.model.Product;
import com.sparta.springcore.dto.ProductMypriceRequestDto;
import com.sparta.springcore.dto.ProductRequestDto;
import com.sparta.springcore.model.UserRoleEnum;
import com.sparta.springcore.service.ProductService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.sparta.springcore.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController // JSON으로 데이터를 주고받음을 선언합니다.
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 신규 상품 등록
    @PostMapping("/api/products")
    public Product createProduct(@RequestBody ProductRequestDto requestDto,
                                 //로그인 성공한 사용자의 정보가 여기로 넘어옴
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인 되어 있는 회원 테이블의 ID
        Long userId = userDetails.getUser().getId();
        //신규상품등록은 productService가 할거니까 userId도 같이 넘겨줌
        Product product = productService.createProduct(requestDto, userId);

        // 응답 보내기
        return product;
    }

    // 설정 가격 변경
    @PutMapping("/api/products/{id}")
    //로그인한 사용자의 정보를 확인할 필요는 없다
    // 어차피 로그인한 회원의 상품만 클라이언트에서 갖고있기에 그걸로 업데이트한다
    public Long updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) {
        Product product = productService.updateProduct(id, requestDto);

    // 응답 보내기 (업데이트된 상품 id)
        return product.getId();
    }

    // 로그인한 회원이 등록한 관심 상품 조회
    @GetMapping("/api/products")
    public List<Product> getProducts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인 되어 있는 회원 테이블의 ID
        Long userId = userDetails.getUser().getId();

        return productService.getProducts(userId);
    }
    //관리자용 전체 상품 조회
    @Secured(UserRoleEnum.Authority.ADMIN)//static한 값사용하겠다
    @GetMapping("/api/admin/products")
    //함수 이름은 동일하면 안된다는 자바의 규칙
    public List<Product> getAllProducts() {
        // 로그인 되어 있는 회원 테이블의 ID
        // 로그인되어있는 회원 아이디로 조회가 아니라 전체 상품 조회할거니까 기존코드 삭제
//        Long userId = userDetails.getUser().getId();
        return productService.getAllProducts();
    }
}