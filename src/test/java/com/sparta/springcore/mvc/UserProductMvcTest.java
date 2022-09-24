package com.sparta.springcore.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springcore.controller.ProductController;
import com.sparta.springcore.controller.UserController;
import com.sparta.springcore.dto.ProductRequestDto;
import com.sparta.springcore.model.User;
import com.sparta.springcore.model.UserRoleEnum;
import com.sparta.springcore.security.UserDetailsImpl;
import com.sparta.springcore.security.WebSecurityConfig;
import com.sparta.springcore.service.KakaoUserService;
import com.sparta.springcore.service.ProductService;
import com.sparta.springcore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

//컨트롤러를 테스트 : http 통신 필요
//클라이언트와의 관계에서 클라이언트가 요청해서 그 결과로 클라이언트의 어떤 값이 오는걸 확인
//MVC : Model + View + Controller
@WebMvcTest(
        //UserMvcTest와 ProductMvcTest 파일을 따로 나눠서 해도 됨. 여기선 단순하게 하기위해 합쳐서 진행
        controllers = {UserController.class, ProductController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class UserProductMvcTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    /*
    MockBean 가짜 Bean vs Mock 가짜 객체
    컨트롤러부분만 테스트하기 위해 Service부분부터 끊어냄 (dependency 끊기)
    가짜 Bean으로 등록해서 컨트롤러에 DI가 되는 것!
     */
    @MockBean
    UserService userService;

    @MockBean
    KakaoUserService kakaoUserService;

    @MockBean
    ProductService productService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                /*
                SpringSecurity에 Authentication가짜 사용자 정보를 넣어주기 위해 filter추가
                 */
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        String username = "제이홉";
        String password = "hope!@#";
        String email = "hope@sparta.com";
        UserRoleEnum role = UserRoleEnum.USER;
        //User < UserDetailImpl < UsernamePasswordAuthenticationToken
        User testUser = new User(username, password, email, role);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("로그인 view")
    void test1() throws Exception {
        // when - then
        /*
        mvc.perform : spring mvc를 실행하는데 이 메소드와 이 주소로 실행한다는 의미
        실제로 http통신을 보내는 작업 (UserController파일과 비교!!!!!!)
         */
        mvc.perform(get("/user/login"))
                //status : http의 status / isOk : 200
                .andExpect(status().isOk())
                //정확한 view를 내려주는지 테스트 : mvc에서 view를 넘겨주는데 view이름을 적어줌
                .andExpect(view().name("login"))
                //http의 헤더와 바디를 print해줌
                .andDo(print());
    }

    @Test
    @DisplayName("회원 가입 요청 처리")
    void test2() throws Exception {
        // given
        //SignupRequestDto로 들어갈 FormData를 만들어줌
        MultiValueMap<String, String> signupRequestForm = new LinkedMultiValueMap<>();
        signupRequestForm.add("username", "제이홉");
        signupRequestForm.add("password", "hope!@#");
        signupRequestForm.add("email", "hope@sparta.com");
        signupRequestForm.add("admin", "false");

        // when - then
        mvc.perform(post("/user/signup")
                        //params에 우리가 만든 form을 넘겨줌
                        //mockBean으로 둔 가짜 객체인 서비스를 사용!
                        .params(signupRequestForm)
                )
                /*
                리다이렉트로 반환하는것은 isOk가 아니라 redirect로 맞는 3xx코드로 맞이함
                 */
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/login"))
                .andDo(print());
    }

    @Test
    @DisplayName("신규 관심상품 등록")
    void test3() throws Exception {
        // given
        //관심상품 등록할때 사용했던 샘플
         /*
         ProductController와 비교!!!
         실제로 서비스에서는 로그인을 하지 않은 상태에서 신규 상품 등록을 하지 못함
         그래서 가짜 사용자를 만들어주고, 등록해놓지 않으면 @PostMapping("/api/products")가 처리되지 않고,
         스프링 시큐리티에 의해서 막하기때문에 mockUserSetup()
         95번 라인에 UsernamePasswordAuthenticationToken안에는 UserDetailImpl이 있었고, 그 안에 User가 있음
         실제로 mock test user가 있다고 가정하고, 로그인되있다고 알려주기 위함
          */
        this.mockUserSetup();
        String title = "Apple <b>에어팟</b> 2세대 유선충전 모델 (MV7N2KH/A)";
        String imageUrl = "https://shopping-phinf.pstatic.net/main_1862208/18622086330.20200831140839.jpg";
        String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=18622086330";
        int lPrice = 77000;
        ProductRequestDto requestDto = new ProductRequestDto(
                title,
                imageUrl,
                linkUrl,
                lPrice
        );
        /*
        @PostMapping("/api/products")의 경우에는 requestBody로 해서 Json으로 받아야함
         form형태가 아니라 Json형태로!
         바로 그 Json형태로 만들어주는 것이 objectMapper이다
         Json형태를 담은 string이 만들어지고, 그것을 가지고 176번라인 content에 넣어줌
         */
        String postInfo = objectMapper.writeValueAsString(requestDto);

        // when - then

        mvc.perform(post("/api/products")
                        .content(postInfo) //http의 body부분
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)//서버한테 나중에 json형태를 받을 수 있다고 알려주는 의미
                        /*
                        mockUserSetup()함수를 사용해서 96번라인 mockPrincipal을 가져와서 같이 담아줌
                        테스트하는 동안만 스프링 시큐리티에서 로그인된 사용자라고 인식할 수 있도록 !
                         그래서 스프링 시큐리티 테스트 모듈(빌드그레등 의존성) 사용한 이유
                         */
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
