package com.dsl.devexp;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class DemoAppTest 
	extends TestCase
{
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public DemoAppTest( String testName )
	{
		super( testName );
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite( DemoAppTest.class );
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testDemoApp()
	{
		assertTrue( true );
	}
}
