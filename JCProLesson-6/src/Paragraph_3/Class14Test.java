package Paragraph_3;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class Class14Test {

    private int[] array;
    private boolean value;

    public Class14Test(int[] array, boolean value) {
        this.array = array;
        this.value = value;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new int[]{4, 5, 7, 2}, false},
                {new int[]{1, 3, 5, 7, 1, 5}, false},
                {new int[]{9, 6, 4, 1, 6}, true}
        });
    }
    @Test
    public void check() {
        Assert.assertEquals(value, Class14.check(array));
    }
}