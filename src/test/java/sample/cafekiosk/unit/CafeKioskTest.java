package sample.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;

import static org.assertj.core.api.Assertions.assertThat;
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

}