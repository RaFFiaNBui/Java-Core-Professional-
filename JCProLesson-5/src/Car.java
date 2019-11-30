import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;
    //private CountDownLatch cd;
    private CyclicBarrier cb;
    private static boolean win;
    //CyclicBarrier cb = new CyclicBarrier(4);
    //CountDownLatch cd = new CountDownLatch(3);
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed, CyclicBarrier cd) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.cb = cd;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            cb.await();
            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }
            isWiner(this);
            cb.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void isWiner (Car car) {
        if(!win) {
            System.out.println("Победитель " + car.name);
            win = true;
        }
    }
}