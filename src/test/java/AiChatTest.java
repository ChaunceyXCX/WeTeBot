import org.junit.Test;

import java.util.Date;

/**
 * @ClassName AiChatTest
 * @Description TODO
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/8/6 下午9:51
 * @Version 1.0
 **/
public class AiChatTest {
    @Test
    public void tsTest() {
        Date date = new Date();
        long aa = date.getTime();
        long aaa = aa / 1000;
        int a = (int) date.getTime();
        String b = String.valueOf(aa).substring(0, 10);
        System.out.println(b);
    }
}
