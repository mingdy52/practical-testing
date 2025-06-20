package sample.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.Order.Order;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {

    /**
     * 수동 테스트
     *  문제 1. 확인 단계에서 사람이 개입함
     *  뮨제 2. 제 3자가 이 테스트를 봤을 때 무엇을 검증해야하고, 어떤 것이 맞고 틀린지 판단 불가
     *  문제 3. 무조건 성공하는 테스트
     */
    @Test
    void add_manual_test() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 수 : " + cafeKiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료 : " + cafeKiosk.getBeverages().get(0).getName());
    }

    /**
     * 자동화 테스트
     *  최종 단계에서 사람 개입x
     */
    @Test
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    /**
     * 암묵적이거나 아직 드러나지 않은 요구사항이 있는가?
     * 테스트 케이스를 세분화 한 다음에 경계값이 존재하는 경우에는 경계값 에서 항상 테스트를 할 수 있도록 고민해야 한다.
     *
     * 1. 해피 케이스
     *    -> 경계값 테스트 :범위(이상, 이하, 초과, 미만), 구간, 날짜 등
     * 2. 예외 케이스
     *      조건: 3 이상
     *      해피 -> 3
     *      예외 -> 2
     */

    // 해피케이스
    @Test
    void addSeveralBeverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano, 2);

        assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
        assertThat(cafeKiosk.getBeverages().get(1).getName()).isEqualTo("아메리카노");
    }

    // 예외케이스
    @Test
    void addZeroBeverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
    }

    @Test
    void remove() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        assertThat(cafeKiosk.getBeverages()).hasSize(1);

        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    void clear() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);
        assertThat(cafeKiosk.getBeverages()).hasSize(2);

        cafeKiosk.clear();
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    void createOrder() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        Order order = cafeKiosk.createOrder();
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
        // 테스트 시간에 따라 테스트 성공 여부가 달라진다.
    }

    /**
     * 테스트하기 어려운 영역을 구분하고 분리해야 한다.
     *
     *  테스트 가능한 코드고 있을 때, 만약 테스트가 어려운 조건이 추가된다면?
     *       -> 전체 테스트 불가능한 상태가 된다.
     *
     *       테스트가 어려운 영역을 외부로 분리한다.
     *           예시: 현재 시간을 사용하는 코드
     *               - 프로덕션 코드에서는 현대 시간을 넣어주고 테스트 할 때는 원하는 값으로 넣는다.
     *       "시간", "랜덤", "환경 변수", "네트워크 호출" 같은 의존성을 외부에서 주입할 수 있도록 설계하는 것이 중요하다.
     *
     *       테스트하기 어려운 영역이 분리되면?
     *            -> 나머지 대부분의 로직은 테스트 가능한 상태로 유지된다.
     *
     * 테스트하기 어려운 영역
     *      - 관측할 때마다 다른 값에 의존하는 코드
     *          : 현재 날짜/시간, 랜덤 값, 전역 변수/함수, 사용자 입력 등
     *      - 외부 세계에 영향을 주는 코드
     *          : 표준 출력, 메시지 발송, 데이터베이스에 기록하기 등
     *
     * 테스트하기 쉬운 함수 - 순수함수(Pure function): 외부 세계와 단절된 함수
     *     - 같은 입력에는 항상 같은 결과
     *     - 외부 세상과 단절된 형태
     *     - 테스트하기 쉬운 코드
     *
     */
    @Test
    void createOrderWithCurrentTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2025, 6, 20, 10, 0));
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void createOrderOutsideOpenTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2025, 6, 20, 9, 59)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요.");
    }

}