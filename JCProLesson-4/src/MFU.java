public class MFU {

    private final  Object lock = new Object();
    private final  Object lock2 = new Object();

    public static void main(String[] args) {
        MFU mfu = new MFU();
        new Thread(() -> mfu.print("1")).start();
        new Thread(() -> mfu.print("2")).start();
        new Thread(() -> mfu.scan("1")).start();
        new Thread(() -> mfu.scan("2")).start();
    }
//Оба метода могут выполняться параллельно в двух разных потоках, но выполнение одного
//метода в двух разных потоках будет выполняться последовательно, пока первый поток не освободит монитор
    private void print(String str) {
        synchronized (lock) {
            try {
                for (int i = 1; i < 4; i++) {
                    Thread.sleep(50);
                    System.out.println("Отпечатано " + i + " страницы");
                }
                System.out.println("     Документ " + str + " напечатан");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void scan(String str) {
        synchronized (lock2) {
            try {
                for (int i = 1; i < 4; i++) {
                    Thread.sleep(50);
                    System.out.println("Отсканировано " + i + " страницы");
                }
                System.out.println("     Документ " + str + " отсканирован");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
