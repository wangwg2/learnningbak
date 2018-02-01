package com.dsl.devexp;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class OpsImpTest 
	extends TestCase
{
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public OpsImpTest( String testName )
	{
		super( testName );
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite( OpsImpTest.class );
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testAddOps()
	{
		AddOps add = new AddOps();
		assertTrue( add.getDesc().equals("plus"));
		assertTrue( add.op(2,1) == 3);
        
	}
	public void testSubOps()
	{
		SubOps sub = new SubOps();
		assertTrue( sub.getDesc().equals("minus"));
		assertTrue( sub.op(2,1) == 1);
        
	}
	public void testMulOps()
	{
		MulOps mul = new MulOps();
		assertTrue( mul.getDesc().equals("times"));
		assertTrue( mul.op(2,1) == 2);
        
	}

}
