import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestREX {
    @Test
    public void test() {
        // 创建pattern
        Pattern pattern = Pattern.compile("hello\\s\\w*d");
        // 生成matcher
        Matcher matcher = pattern.matcher("hello worl1d__hello worl2d");
        // 进行match及操作：重载了pattern的组数参数
        if (matcher.matches()) { // 整体匹配判断，并更新matcher匹配标
            int start = matcher.start(); // 匹配开始下标
            int end = matcher.end(); // 匹配结束下标+1
            String group = matcher.group(); // 匹配串
        }
        matcher.reset();
        while (matcher.find()) { // 从matcher匹配标开始，局部匹配判断，并更新matcher匹配标
            int start = matcher.start();
            int end = matcher.end();
            String group = matcher.group();
        }
        if (matcher.lookingAt()) { // 从头局部匹配判断，并更新matcher匹配标
            int start = matcher.start();
            int end = matcher.end();
            String group = matcher.group();
        }
    }
}
