package li.jesse.javadevdatastructures.collection.list;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

public class ArrayListTest {

    @Test
    public void testCapacityWithNull() {
        ArrayList<Integer> list = new ArrayList<>();
        System.out.println("begin: " + list.size());

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        System.out.println("added 10 elements: " + list.size() + " " + getCapacity(list));

        list.add(11);
        System.out.println("added 11 elements: " + list.size() + " " + getCapacity(list));

        list.add(null);
        System.out.println("added null elements: " + list.size() + " " + getCapacity(list));
    }

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        // 进行10次测试
        for(int i = 0; i < 10; i++)
        {
            test();
        }

        System.out.println("总共用时" + (System.currentTimeMillis() - startTime) + "ms");
    }

    public static Integer getCapacity(ArrayList list) {
        Integer length = null;
        Class c = list.getClass();
        Field f;
        try {
            f = c.getDeclaredField("elementData");
            f.setAccessible(true);
            Object[] o = (Object[]) f.get(list);
            length = o.length;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return  length;
    }

    @Test
    public void testCapacity() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            list.add(i);
        }

        Integer length = getCapacity(list);
        System.out.println(list.size());
        System.out.println(length);
    }


    public static void test()
    {
        // 用来测试的List
        List<Object> list = new ArrayList<>();

        // 线程数量(1000)
        int threadCount = 1000;

        // 用来让主线程等待threadCount个子线程执行完毕
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // 启动threadCount个子线程
        for(int i = 0; i < threadCount; i++)
        {
            Thread thread = new Thread(new MyThread(list, countDownLatch));
            thread.start();
        }

        try
        {
            // 主线程等待所有子线程执行完成，再向下执行
            countDownLatch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        // List的size
        System.out.println(list.size());
    }

    static class MyThread implements Runnable
    {
        private List<Object> list;

        private CountDownLatch countDownLatch;

        public MyThread(List<Object> list, CountDownLatch countDownLatch)
        {
            this.list = list;
            this.countDownLatch = countDownLatch;
        }

        public void run()
        {
            // 每个线程向List中添加100个元素
            for(int i = 0; i < 100; i++)
            {
                list.add(new Object());
            }

            // 完成一个子线程
            countDownLatch.countDown();
        }
    }
}
