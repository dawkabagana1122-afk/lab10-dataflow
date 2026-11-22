package mn.edu.must.sqat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * gcd() — all-DU-pairs. Мөрийн дугаар нь DataFlowTargets.java файлынх:
 * 13 def x,y · 15 p-use y · 16 def tmp, c-use x,y · 17 def x, c-use y · 18 def y, c-use tmp · 20 c-use x.
 */
class GcdTest {

    @Test
    @DisplayName("G1 gcd(7,0)=7, 0 iterations: x(13,20) y(13,15F)")
    void gcd_zeroIterations_7_0_is7() {
        // Давталтад огт орохгүй: зөвхөн энэ тест x-ийн 13-ын def-ийг шууд 20-д хүргэнэ
        assertEquals(7, DataFlowTargets.gcd(7, 0));
    }

    @Test
    @DisplayName("G2 gcd(9,3)=3, 1 iteration: x(13,16) x(17,20) y(13,15T) y(13,16) y(13,17) y(18,15F) tmp(16,18)")
    void gcd_oneIteration_9_3_is3() {
        assertEquals(3, DataFlowTargets.gcd(9, 3));
    }

    @Test
    @DisplayName("G3 gcd(12,8)=4, 2 iterations: x(17,16) y(18,15T) y(18,16) y(18,17) + G2 pairs")
    void gcd_twoIterations_12_8_is4() {
        // 2-р давталт л 17/18-ын def-ийг давталтын эхэнд (15T, 16, 17) хүргэнэ
        assertEquals(4, DataFlowTargets.gcd(12, 8));
    }
}
