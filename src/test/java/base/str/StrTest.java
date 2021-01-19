package base.str;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StrTest {

    @Test
    public void test() {
        String[] all = new String[]{"aaa.txt", "bbb.csv", "xxxx.xxx", "ccc.TXT"};
        List<String> filters = new ArrayList<>();
        filters.add(".txt");
        List<String> result = new ArrayList<>();
        for (String ele : all) {
            if (filters.size() > 0) {
                if (StringUtils.endsWithAny(ele, filters.toArray(new String[filters.size()]))) {
                    result.add(ele);
                }
            } else {
                result.add(ele);
            }
        }
        System.out.println(result);
    }

    @Test
    public void testLibDiff() throws IOException {
        List<String> list1 = toLibNames("C:/Users/shuwei17/Desktop/new 4");
        System.out.println(list1);
        List<String> list2 = toLibNames("C:/Users/shuwei17/Desktop/new 3");;
        Set<String> sets = new HashSet<>(list2);
        Set<String> oneMore = new HashSet<>();
        for (String str : list1) {
            if (!sets.remove(str)) {
                oneMore.add(str);
            }
        }
        System.out.println("list1多余的jar包为:" + oneMore);
        System.out.println("list2多余的jar包为:" + sets);
    }

    private List<String> toLibNames(String fileName) throws IOException {
        File file = new File(fileName);
        List<String> strings = FileUtils.readLines(file);
        StringBuilder sb = new StringBuilder();
        for (String ele : strings) {
            sb.append(ele.replace(" ", ""));
        }
        String[] split = sb.toString().split("lib/");
        List<String> collect = Stream.of(split).map(str -> str.replace(" ", "")).filter(str ->
                StringUtils.isNotBlank(str)).collect(Collectors.toList());
        return collect;
    }
}
