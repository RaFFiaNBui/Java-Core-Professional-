import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MainTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new int[]{1, 2, 4, 5, 2, 3, 4, 1, 7},new int[]{7}},
                {new int[]{1, 2, 4, 5, 2, 3, 4, 1, 7, 3},new int[]{7,3}},
                {new int[]{1, 2, 4, 5, 2, 3, 4},new int[]{2, 3, 4}}
        });
    }

    private int[] enterArray, exitArray;

    public MainTest(int[] enterArray, int[] exitArray) {
        this.enterArray = enterArray;
        this.exitArray = exitArray;
    }

    @Test
    public void test () {
        int [] enter = MainClass.method(enterArray);
        Assert.assertArrayEquals(exitArray, enter);
        MainClass.info(enter);
    }

    @Test(expected = RuntimeException.class)
    public void testRE () {
        MainClass.method(new int[]{1, 2, 4});
    }
}