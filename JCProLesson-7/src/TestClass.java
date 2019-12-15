public class TestClass {

    @BeforeSuite
    public void start () {
        System.out.println("StartOfTests");
    }

    @Test (priority = 2)
    public void test1 () {
        System.out.println("StartTest1");
    }

    @Test
    public void test2 () {
        System.out.println("StartTest2");
    }

    @Test (priority = 10)
    public void test3 () {
        System.out.println("StartTest3");
    }

    @Test (priority = 7)
    public void test4 () {
        System.out.println("StartTest4");
    }

    @Test (priority = 4)
    public void test5 () {
        System.out.println("StartTest5");
    }

    @AfterSuite
    public void end () {
        System.out.println("EndOfTests");
    }
}
