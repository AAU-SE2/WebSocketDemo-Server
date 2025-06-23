package at.aau.serg.websocketdemoserver.model.board;

import at.aau.serg.websocketdemoserver.model.util.Dice;
import at.aau.serg.websocketdemoserver.model.util.DicePair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DicePairTest {

    @Test
    void testRollReturnsTwoValues() {
        Dice d1 = mock(Dice.class);
        Dice d2 = mock(Dice.class);

        when(d1.roll()).thenReturn(2);
        when(d2.roll()).thenReturn(5);

        DicePair pair = new DicePair(d1, d2);

        int[] result = pair.roll();

        assertEquals(2, result[0]);
        assertEquals(5, result[1]);
    }
}
