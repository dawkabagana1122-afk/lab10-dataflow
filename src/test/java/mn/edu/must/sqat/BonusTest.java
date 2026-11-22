package mn.edu.must.sqat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * bonus() — мөрийн дугаар нь DataFlowTargets.java файлынх (31–56).
 * DU хүснэгт ба тестийн ID-г README-ээс хар.
 */
class BonusTest {

    // ---------- Шат 1: ALL-DEFS (хамгийн цөөн: 2 тест) ----------

    @Test
    @DisplayName("D1 [1,1,1,1,1] thr=5 -> 8: defs 31,35,36,37,38,40,41,43,51,54 reach a use")
    void bonus_fivePresentStreak5_threshold5_is8() {
        // score(51) -> 54, score(54) -> 56, best(43) -> 53 T
        assertEquals(8, DataFlowTargets.bonus(new int[]{1, 1, 1, 1, 1}, 5));
    }

    @Test
    @DisplayName("D2 [0,1] thr=5 -> 0: defs streak(46)->41, score(49)->56")
    void bonus_absentThenPresent_belowThreshold_is0() {
        // 46-ын streak = 0 зөвхөн дараагийн ирсэн өдрийн 41-д хэрэглэгдэнэ
        assertEquals(0, DataFlowTargets.bonus(new int[]{0, 1}, 5));
    }
}
