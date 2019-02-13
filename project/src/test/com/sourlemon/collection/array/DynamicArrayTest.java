package test.com.sourlemon.collection.array;

import com.sourlemon.collection.array.DynamicArray;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * DynamicArray Tester.
 *
 * @author sourlemon
 * @version 1.0
 * @since <pre>Feb 13, 2019</pre>
 */
public class DynamicArrayTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }


    @Test
    public void testDynamicArray() {
        DynamicArray<Integer> array=new DynamicArray<Integer>();

        array.addLast(0);
        array.addLast(1);
        array.addLast(2);
        array.addLast(3);
        array.addLast(4);
        array.addLast(5);
        array.addLast(6);
        array.addLast(7);
        array.addLast(8);
        array.addLast(9);
        array.addLast(10);
        array.addLast(11);
        array.add(9, 100);

        array.set(1, 2);

        System.out.println("获取下标为1的元素："+array.get(1));

        System.out.println("数组内容："+array);
    }


    @Test
    public void testDynamicArrayRemove() {
        DynamicArray<Integer> array=new DynamicArray<Integer>();

        array.addLast(11);
        array.addLast(22);
        array.addLast(33);
        array.addLast(44);
        array.addLast(55);

        System.out.println("删除元素："+array.remove(1));

        System.out.println("数组内容："+array);
    }


} 
