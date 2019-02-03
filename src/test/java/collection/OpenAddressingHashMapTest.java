package collection;

import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class OpenAddressingHashMapTest {

    Map map = new OpenAddressingHashMap();

    @Test
    public void defaultPutTest() {
        map.put(1, 2);

        assertEquals(2, ((long) map.get(1)));
    }

    @Test
    public void rewritingOldValueTest() {
        map.put(1, 2);
        map.put(1, 5);

        assertEquals(5, ((long) map.get(1)));
    }

    @Test
    public void properSizeValueTest1() {
        map.put(0, 2);
        map.put(3, 2);

        assertEquals(2, ((long) map.size()));
    }

    @Test
    public void properSizeValueTest2() {
        map.put(0, 2);
        map.put(3, 2);
        map.put(3, 4);

        assertEquals(2, ((long) map.size()));
    }

    @Test
    public void absentValuesTest() {
        map.put(1, 2);

        assertNull(map.get(5));
    }

    @Test
    public void properResizingTest() {
        IntStream.range(0, 11).forEach(a -> map.put(a, a));

        map.put(17, 17);
        map.put(15, 15);

        assertEquals(17, ((long) map.get(17)));
    }
}