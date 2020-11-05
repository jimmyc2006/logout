package test.mysql.diff;

/**
 * 使用不同的classloader，需要有一步去反射,我可以创建一个类，专门用于执行sql
 * 问题是它的返回值我需要能解析,所以只能使用上层的ClassLoader来传递数据
 * 类加载器的图:
 * bootstrap -> extension -> application(这里有个mysql驱动)
 *                        -> URL(这里也有一个驱动)
 * 如果反射来调用service，会需要反射很多方法，太复杂了
 * 可以直接使用resultSet，这个是上层的类
 *
 */
public class DriverTest {


}
