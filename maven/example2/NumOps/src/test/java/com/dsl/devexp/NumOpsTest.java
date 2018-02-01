package com.dsl.devexp;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class NumOpsTest 
	extends TestCase
{
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public NumOpsTest( String testName )
	{
		super( testName );
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite( NumOpsTest.class );
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testNumOps()
	{
		NumOps nops = new NumOps();
		assertTrue( nops.size() == 3);
		assertTrue( nops.getOp(0).getDesc().equals("plus"));
		assertTrue( nops.getOp(0).op(2,1) == 3);
		assertTrue( nops.getOp(1).getDesc().equals("minus"));
		assertTrue( nops.getOp(1).op(2,1) == 1);
		assertTrue( nops.getOp(2).getDesc().equals("times"));
		assertTrue( nops.getOp(2).op(2,1) == 2);

	}
}
