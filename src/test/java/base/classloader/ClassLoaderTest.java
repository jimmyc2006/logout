package base.classloader;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderTest {

    /**
     * 测试从目录下的jar包中加载类
     */
    @Test
    public void loadFromDir() throws MalformedURLException, ClassNotFoundException {
        ClassLoader cl = new URLClassLoader(new URL[]{new URL("file:D:/soft/datax/plugin/writer/mysqlwriter/libs/mysql-connector-java-5.1.34.jar")}, this.getClass().getClassLoader());
        Class<?> aClass = cl.loadClass("com.mysql.jdbc.ConnectionImpl");
        System.out.println(aClass);
    }
}
