public class ABC {

    private final Object object = new Object();
    private String defaultLetter = "A";

    public static void main(String[] args) {

        ABC abc = new ABC();

        Thread t1 = new Thread(abc::printA);
        Thread t2 = new Thread(() -> abc.printB());
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                abc.printC();
            }
        });
        
        t1.start();
        t2.start();
        t3.start();
    }
    
    private void printA() {
        synchronized (object) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (!defaultLetter.equals("A")) {
                        object.wait();
                    }
                    System.out.print("A");
                    defaultLetter = "B";
                    object.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void printB() {
        synchronized (object) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (!defaultLetter.equals("B")) {
                        object.wait();
                    }
                    System.out.print("B");
                    defaultLetter = "C";
                    object.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void printC() {
        synchronized (object) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (!defaultLetter.equals("C")) {
                        object.wait();
                    }
                    System.out.print("C");
                    System.out.print(" ");
                    defaultLetter = "A";
                    object.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
