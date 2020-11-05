package log;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class LogTest {

  public static void main(String[] args) throws SQLException {
    DriverManager.setLogWriter(new PrintWriter(System.out) {
      @Override
      public void println(String x) {
        x = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()) + " " + x;
        super.println(x);
      }
    });
    Connection conn = null;
    try {
      conn = DriverManager.getConnection("jdbc:mysql://10.10.10.72:3306/hive_meta?characterEncoding=utf-8&serverTimezone=Asia/Shanghai", "hive", "hive123");
      Statement stat = conn.createStatement();
      ResultSet rs = stat.executeQuery("show tables");
      while (rs.next()) {
        System.out.println(rs.getString(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      conn.close();
    }
  }
}
