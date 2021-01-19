package com.alibaba.otter.bean;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class BeanUtilTest {

    @Test
    public void test() throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> testMap= new LinkedHashMap<>();
        testMap.put("id", 123);
        testMap.put("name", "shuwei");
        Map<String, Object> add = new LinkedHashMap<>();
        add.put("code", 222);
        add.put("add", "aaaaaaaaaaaaaaaaa");
        testMap.put("address", add);
        User user = new User();
        BeanUtils.populate(user, testMap);
        System.out.println(JSON.toJSONString(user));
    }

    @Test
    public void testWriterFile() throws IOException {
        String baseDir = "D:/tmp/many/";
        for (int i = 1 ; i <= 1200; i++) {
            File f = new File(baseDir + i + ".txt");
            FileWriter fw = new FileWriter(f);
            fw.write(i + ",aa" + i);
            fw.close();
        }
    }

}