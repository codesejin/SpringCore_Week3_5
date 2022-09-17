package com.sparta.springcore.model;

import com.sparta.springcore.dto.ProductRequestDto;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    /**
     * Nested : 한 개의 depth 안에서 돌때
     * 트리처럼 안으로 파고들어서 tab방식으로 테스트코드 만드는법
     */
    @Nested
    @DisplayName("회원이 요청한 관심상품 객체 생성")
    class CreateUserProduct {
        /**
         * 기존에는 Test안에서 given에 변수로 선언해서 사용했었는데
         * 이번에는 멤버변수로 선언된 이유?
         * 공통적으로 이 모든 test에서 사용해야하기 때문에 뺐음
         */
        private Long userId;
        private String title;
        private String image;
        private String link;
        private int lprice;
        /**
         * BeforeEach : Test를 수행하기 전에
         * 모든 test에서 한번씩 여기를 거침
         * 아래는 정상케이스에서 가지고 있었던 샘플예시
         */
        @BeforeEach
        void setup() {
            userId = 100L;
            title = "오리온 꼬북칩 초코츄러스맛 160g";
            image = "https://shopping-phinf.pstatic.net/main_2416122/24161228524.20200915151118.jpg";
            link = "https://search.shopping.naver.com/gate.nhn?id=24161228524";
            lprice = 2350;
        }

        @Test
        @DisplayName("정상 케이스")
        /**정상 케이스
         * 기존 코드에서 멤버변수와 BeforeEach로 빼준것 밖에 없음
         * 기존 코드 참고해서 확인!
         */
        void createProduct_Normal() {
            /**
             *  new ProductRequestDto를 만들어 줄때, 멤버변수를 써 가져오는것
             *  사실은 this.title에서 this를 생략한 것
             *  setup()함수에서 설정을 해주고 여기로 오는 것이기 때문에 default값으로 설정되어있음
             */
            // given
            ProductRequestDto requestDto = new ProductRequestDto(
                    title,
                    image,
                    link,
                    lprice
            );

            // when
            Product product = new Product(requestDto, userId);

            // then
            assertNull(product.getId());
            assertEquals(userId, product.getUserId());
            assertEquals(title, product.getTitle());
            assertEquals(image, product.getImage());
            assertEquals(link, product.getLink());
            assertEquals(lprice, product.getLprice());
            assertEquals(0, product.getMyprice());
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCases {
            @Nested
            @DisplayName("회원 Id")
            class userId {
                @Test
                @DisplayName("null")//userId가 null경우에 대한 Test
                void fail1() {
                    // given : 정상케이스에서 default값을 설정했기 때문에 null로 할당해서 Test
                    userId = null;

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );
                    /*
                    Product클래스에서 userId가 null이면 에러를 발생하기로 했는데
                    애러가 발생하는지 확인하는 테스트 코드
                    assertThrows:에러가 throws가 돼야한다
                    조건문에서 만든 IllegalArgumentException가 일치하지 않으면 테스트코드 fail됨
                     */
                    // when - then
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });
                    /*
                    원래는 then에 모든 것이 모여있어야 하지만,
                    assertEquals에서 then에 다 모으지 못하고
                    assertThrows가 실행까지 하기 때문에 when에 위치할 수 밖에 없음
                     */
                    /*
                    expected안에 문자열을 비교하는것은 좋은 test code는 아니다
                    문자열을 그대로 비교하면, Product클래스에서 조건문의 에러메세지를 변경할때 에러가 나기 때문에
                    Product클래스에서보면 모두 같은 IllegalArgumentException을 주고 있는데,


                     */
                    // then
                    assertEquals("회원 Id 가 유효하지 않습니다.", exception.getMessage());
                }

                @Test
                @DisplayName("마이너스")
                void fail2() {
                    // given
                    userId = -100L;

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("회원 Id 가 유효하지 않습니다.", exception.getMessage());
                }
            }

            @Nested
            @DisplayName("상품명")
            class Title {
                @Test
                @DisplayName("null")
                void fail1() {
                // given
                    title = null;

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("저장할 수 있는 상품명이 없습니다.", exception.getMessage());
                }

                @Test
                @DisplayName("빈 문자열")
                void fail2() {
                // given
                    String title = "";

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("저장할 수 있는 상품명이 없습니다.", exception.getMessage());
                }
            }

            @Nested
            @DisplayName("상품 이미지 URL")
            class Image {
                @Test
                @DisplayName("null")
                void fail1() {
                    // given
                    image = null;

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("상품 이미지 URL 포맷이 맞지 않습니다.", exception.getMessage());
                }

                @Test
                @DisplayName("URL 포맷 형태가 맞지 않음")
                void fail2() {
                    // given
                    image = "shopping-phinf.pstatic.net/main_2416122/24161228524.20200915151118.jpg";

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("상품 이미지 URL 포맷이 맞지 않습니다.", exception.getMessage());
                }
            }

            @Nested
            @DisplayName("상품 최저가 페이지 URL")
            class Link {
                @Test
                @DisplayName("null")
                void fail1() {
                    // given
                    link = "https";

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("상품 최저가 페이지 URL 포맷이 맞지 않습니다.", exception.getMessage());
                }

                @Test
                @DisplayName("URL 포맷 형태가 맞지 않음")
                void fail2() {
                   // given
                    link = "https";

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                     // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                     // then
                    assertEquals("상품 최저가 페이지 URL 포맷이 맞지 않습니다.", exception.getMessage());
                }
            }

            @Nested
            @DisplayName("상품 최저가")
            class LowPrice {
                @Test
                @DisplayName("0")
                void fail1() {
                    // given
                    lprice = 0;

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("상품 최저가가 0 이하입니다.", exception.getMessage());
                }

                @Test
                @DisplayName("음수")
                void fail2() {
                    // given
                    lprice = -1500;

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("상품 최저가가 0 이하입니다.", exception.getMessage());
                }
            }
        }
    }
}