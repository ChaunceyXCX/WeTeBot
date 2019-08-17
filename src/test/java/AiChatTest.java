import org.junit.Test;

import java.util.Calendar;
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

    @Test
    public void strTest(){
        String s = "a";
        System.out.println(s.charAt(0));
        System.out.println(s.charAt(1));
    }

    @Test
    public void dateTest() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        System.out.println(date);
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));

        calendar.set(Calendar.DAY_OF_WEEK,2);
        System.out.println(calendar.getTime());
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));

        calendar.add(Calendar.DAY_OF_WEEK,6);

        System.out.println(calendar.getTime());
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println(calendar.get(Calendar.MONTH));

        calendar.add(Calendar.MONTH,1);
        System.out.println(calendar.getTime());
        System.out.println(calendar.get(Calendar.MONTH));



    }
}
