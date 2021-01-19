package com.alibaba.otter.code;

import org.junit.Test;

import java.io.*;

public class IsoTest {

    @Test
    public void test() throws IOException {
        File f = new File("d://tmp/" + new String(("中文名称").getBytes(), "iso-8859-1"));
//        FileOutputStream fis = new FileOutputStream(f);
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write(new String("你好,哈哈，中国\n你好,哈哈，中国\n".getBytes("utf-8"), "iso-8859-1"));
        bw.close();
    }

    @Test
    public void test2() throws IOException {
        File f = new File("d://tmp/" + new String(("中文名称").getBytes(), "iso-8859-1") +".txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line1 = br.readLine();
        System.out.println(new String(line1.getBytes("iso-8859-1"), "utf-8"));
        line1 = br.readLine();
        System.out.println(new String(line1.getBytes("iso-8859-1"), "utf-8"));
    }
}
