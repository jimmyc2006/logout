package thread;

public class A extends Thread {
    @Override
    protected void finalize() {
        System.out.println(this + " was finalized!");
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("================================");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        A a = new A();
        a.start();
        System.out.println("Created " + a);
        for (int i = 0; i < 1_000_000_000; i++) {
            if (i % 1_000_00 == 0)
                System.gc();
        }
        System.out.println("done.");
    }
}
