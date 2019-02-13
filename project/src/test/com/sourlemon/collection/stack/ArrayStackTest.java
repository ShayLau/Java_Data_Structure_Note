package test.com.sourlemon.collection.stack;

import com.sourlemon.collection.stack.ArrayStack;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * ArrayStack Tester.
 *
 * @author sourlemon
 * @version 1.0
 * @since <pre>Feb 13, 2019</pre>
 */
public class ArrayStackTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testStack() {

        ArrayStack<Integer> stack = new ArrayStack<Integer>();

        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);

        System.out.println("stack result: " + stack);

        stack.pop();
        stack.pop();
        stack.pop();

        System.out.println("stack result: " + stack);

        stack.pop();
        System.out.println("stack result: " + stack);
        System.out.println("stack isEmpty: " + stack.isEmpty());


    }


} 
