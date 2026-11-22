package mn.edu.must.sqat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    // ---------- Шат 2: ALL-DU-PAIRS (нэмэлт 6 тест) ----------

    @Test
    @DisplayName("D3 null -> IAE: attendance(31, 32T)")
    void bonus_nullAttendance_throws() {
        assertThrows(IllegalArgumentException.class, () -> DataFlowTargets.bonus(null, 3));
    }

    @Test
    @DisplayName("D4 thr=-1 -> IAE: threshold(31, 32T)")
    void bonus_negativeThreshold_throws() {
        assertThrows(IllegalArgumentException.class, () -> DataFlowTargets.bonus(new int[]{1}, -1));
    }

    @Test
    @DisplayName("D5 [] thr=0 -> 5: present(35, 50T) best(37, 53F) score(51, 56) attendance(31, 38F)")
    void bonus_emptyArray_threshold0_is5() {
        assertEquals(5, DataFlowTargets.bonus(new int[]{}, 0));
    }

    @Test
    @DisplayName("D6 [] thr=1 -> 0: present(35, 50F) best(37, 53F) score(49, 56)")
    void bonus_emptyArray_threshold1_is0() {
        assertEquals(0, DataFlowTargets.bonus(new int[]{}, 1));
    }

    @Test
    @DisplayName("D7 [1,0,1] thr=2 -> 5: streak(41, 42F) best(43, 42F) streak(46, 41) present(40, 40) score(51, 56)")
    void bonus_presentAbsentPresent_threshold2_is5() {
        // 2-р цувааны streak = 1 нь best = 1-ээс их биш -> 42-ын F салаа
        assertEquals(5, DataFlowTargets.bonus(new int[]{1, 0, 1}, 2));
    }

    @Test
    @DisplayName("D8 [1,1,1,1,1] thr=6 -> 3: score(49, 54)")
    void bonus_streak5_belowThreshold_is3() {
        // present 5 < 6 -> 51 алгасагдана, 49-ийн score = 0 нь 54-т хэрэглэгдэнэ
        assertEquals(3, DataFlowTargets.bonus(new int[]{1, 1, 1, 1, 1}, 6));
    }
}
