package org.dummy.junit.output;

import java.util.Date;
import org.junit.Test;

/**
 *
 */
public class DummyClassTest {

    public DummyClassTest() {
    }

    @Test
    public void testSomeMethod() throws InterruptedException {
        System.out.println("" + new Date() + " - started");

        for( int i = 0; i < 100; i++ ) {
            System.out.println("" + new Date() + " - continue");
            Thread.sleep(500);
        }
    }
}
