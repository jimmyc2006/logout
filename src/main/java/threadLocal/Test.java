package threadLocal;

public class Test {
    private static final ThreadLocal<Integer> TL_INT = ThreadLocal.withInitial(() -> 666);
    private static final ThreadLocal<Integer> TL_INT2 = ThreadLocal.withInitial(() -> 888);
    private static final ThreadLocal<String> TL_STRING = ThreadLocal.withInitial(() -> "Hello, world");
    private static ThreadLocal<TestObject> TO = new ThreadLocal<>();
    private static ThreadLocal<TestObject> TO2 = new ThreadLocal(){
        @Override
        protected void finalize() throws Throwable {
            System.out.println("TO@@@@@222222");
            super.finalize();
        }
    };

    public static void main1(String... args) {
        // 6
        System.out.println(TL_INT.get());
        System.gc();
        TL_INT.set(TL_INT.get() + 1);
        System.out.println(TL_INT2.get());
        // 7
        System.out.println(TL_INT.get());
//        TL_INT.remove();
        // 会重新初始化该value，6
        System.out.println(TL_INT.get());
    }
    
    public static void main(String[] args) {
        TO.set(new TestObject("111"));
        TO2.set(new TestObject("222"));
        System.out.println(TO.get());
        System.out.println(TO2.get());
//        TO.remove();
        TO2 = null;
        System.gc();
        System.out.println(TO.get());
//        System.out.println(TO2.get());
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.gc();
//                System.out.println(TO2.get());
//            }
//        }.start();
    }

    static class TestObject {
        String name;
        public TestObject(String name) {
            this.name = name;
        }
        @Override
        protected void finalize() throws Throwable {
            System.out.println(name  + "被回收");
            super.finalize();
        }
    }
}
