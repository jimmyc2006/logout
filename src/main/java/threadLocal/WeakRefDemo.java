package threadLocal;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class WeakRefDemo {

    static class Entry extends WeakReference<ThreadLocal<?>> {
        private Object value;

        Entry(ThreadLocal<?> k, Object v) {
            super(k);
            value = v;
        }
    }
    public static void main(String... args) {

        Entry entry = new Entry(new ThreadLocal<>(), new Object());
        
        System.out.println(">>>>>>>>>>>>>>>");
        System.out.println(entry.value);
        System.out.println(entry.get());
        System.out.println(">>>>>>>>>>>>>>>");
        
        // all these objects have a strong reference
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();

        // other references to these objects
        Object strongA = a;
        SoftReference<Object> softB = new SoftReference<>(b);
        WeakReference<Object> weakC = new WeakReference<>(c);

        // free the former strong references to these objects:

        // there is still a strong reference(strongA) to the first object
        a = null;
        // only a soft reference(softB) refers to the second object
        b = null;
        // only a weak reference(weakC) refers to the third object
        c = null;

        System.out.println("Before gc...");
        System.out.println(String.format("strongA = %s, softB = %s, weakC = %s", strongA, softB.get(), weakC.get()));

        System.out.println("Run GC...");

        System.gc();

        // object with only soft reference will be cleaned only if memory is not enough: 用来做缓存很不错
        // object with only weak reference will be cleaned after a gc operation:
        System.out.println("After gc...");
        System.out.println(String.format("strongA = %s, softB = %s, weakC = %s", strongA, softB.get(), weakC.get()));
        System.out.println(">>>>>>>>>>>>>>>");
        System.out.println(entry.value);
        System.out.println(entry.get());
        System.out.println(">>>>>>>>>>>>>>>");
    }

}
