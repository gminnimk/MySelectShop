//package com.sparta.myselectshop.util;
//
//import com.sparta.myselectshop.entity.Product;
//import com.sparta.myselectshop.entity.User;
//import com.sparta.myselectshop.entity.UserRoleEnum;
//import com.sparta.myselectshop.naver.dto.ItemDto;
//import com.sparta.myselectshop.naver.service.NaverApiService;
//import com.sparta.myselectshop.repository.ProductRepository;
//import com.sparta.myselectshop.repository.UserRepository;
//import com.sparta.myselectshop.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.sparta.myselectshop.service.ProductService.MIN_MY_PRICE;
//
///**
// * ✅ TestDataRunner 클래스는 애플리케이션 시작 시 테스트 데이터를 생성하는 컴포넌트입니다.
// *
// *    ➡️ 사용자와 상품 데이터를 데이터베이스에 생성하여 테스트 환경을 설정합니다.
// */
//@Component // 이 클래스가 Spring의 컴포넌트임을 나타냅니다.
//public class TestDataRunner implements ApplicationRunner {
//
//    @Autowired
//    private UserService userService; // 사용자 관련 서비스
//    @Autowired
//    private ProductRepository productRepository; // 상품 정보를 처리하는 리포지토리
//    @Autowired
//    private UserRepository userRepository; // 사용자 정보를 처리하는 리포지토리
//    @Autowired
//    private PasswordEncoder passwordEncoder; // 비밀번호 암호화 서비스
//    @Autowired
//    private NaverApiService naverApiService; // 네이버 API를 통한 상품 검색 서비스
//
//    /**
//     * ✅ 애플리케이션 시작 시 호출되어 테스트 데이터를 생성합니다.
//     *
//     *    ➡️ 테스트 사용자와 그 사용자의 관심상품을 생성하여 데이터베이스에 저장합니다.
//     *
//     * @param args 애플리케이션 실행 시 전달된 인자들입니다.
//     */
//    @Override
//    public void run(ApplicationArguments args) {
//        // 테스트 사용자 생성
//        User testUser = new User("Robbie", passwordEncoder.encode("1234"), "robbie@sparta.com", UserRoleEnum.USER);
//        testUser = userRepository.save(testUser);
//
//        // 테스트 사용자의 관심상품 등록
//        // 여러 검색어에 대해 관심상품 10개씩 등록합니다.
//        createTestData(testUser, "신발");
//        createTestData(testUser, "과자");
//        createTestData(testUser, "키보드");
//        createTestData(testUser, "휴지");
//        createTestData(testUser, "휴대폰");
//        createTestData(testUser, "앨범");
//        createTestData(testUser, "헤드폰");
//        createTestData(testUser, "이어폰");
//        createTestData(testUser, "노트북");
//        createTestData(testUser, "무선 이어폰");
//        createTestData(testUser, "모니터");
//    }
//
//    /**
//     * ✅ 주어진 사용자와 검색어를 기반으로 테스트 상품 데이터를 생성합니다.
//     *
//     *    ➡️ 네이버 쇼핑 API를 통해 검색된 상품 목록을 데이터베이스에 저장합니다.
//     *
//     * @param user 테스트 상품을 등록할 사용자 객체입니다.
//     * @param searchWord 상품 검색에 사용될 검색어입니다.
//     */
//    private void createTestData(User user, String searchWord) {
//        // 네이버 쇼핑 API를 통해 상품 목록을 검색합니다.
//        List<ItemDto> itemDtoList = naverApiService.searchItems(searchWord);
//
//        List<Product> productList = new ArrayList<>();
//
//        for (ItemDto itemDto : itemDtoList) {
//            Product product = new Product();
//            // 상품의 소유자 설정
//            product.setUser(user);
//            // 상품의 상세 정보 설정
//            product.setTitle(itemDto.getTitle());
//            product.setLink(itemDto.getLink());
//            product.setImage(itemDto.getImage());
//            product.setLprice(itemDto.getLprice());
//
//            // 희망 최저가를 랜덤값으로 설정합니다.
//            // 최저 가격은 100원, 최대 가격은 현재 최저가 + 10000원으로 설정합니다.
//            int myPrice = getRandomNumber(MIN_MY_PRICE, itemDto.getLprice() + 10000);
//            product.setMyprice(myPrice);
//
//            productList.add(product);
//        }
//
//        // 생성된 상품 목록을 데이터베이스에 저장합니다.
//        productRepository.saveAll(productList);
//    }
//
//    /**
//     * ✅ 주어진 최소값과 최대값 사이에서 랜덤한 정수를 생성합니다.
//     *
//     *    ➡️ 생성된 랜덤 값은 최소값과 최대값 사이의 범위에 포함됩니다.
//     *
//     * @param min 생성될 랜덤 수의 최소값입니다.
//     * @param max 생성될 랜덤 수의 최대값입니다.
//     * @return min과 max 사이의 랜덤한 정수값입니다.
//     */
//    public int getRandomNumber(int min, int max) {
//        return (int) ((Math.random() * (max - min)) + min);
//    }
//}