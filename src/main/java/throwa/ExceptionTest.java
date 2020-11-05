package throwa;

public class ExceptionTest {
    public static void main(String[] args) {
        ExceptionTest test = new ExceptionTest();
        test.testExceptionOverwrite();
//        ExceptionTest test = new ExceptionTest();
//        try {
//            System.out.println("1 before throw");
//            test.testThrowError();
//            System.out.println("2 after throw");
//        } catch (Exception e) {
//            System.out.println("eeee");
//        } finally {
//            System.out.println("finally");
////            throw new RuntimeException("reeeee");
//        }
    }

    public void testExceptionOverwrite() {
        try {
            throw new RuntimeException("inner 111");
        } catch (Exception e) {
            throw e;
        } finally {
            throw new RuntimeException("finally Exception");
        }
    }

    public void testThrowError(){
        throw new Error("TTTT ERROR");
    }

}
