package tests.integrationtests;

import system.PFSystem;
import junit.framework.TestCase;

public abstract class Seam extends TestCase
{
	public abstract PFSystem getPFS();
	private PFSystem test;
	
	public Seam(String arg0)
	{
		super(arg0);
	}

	protected void setUp()
	{
		test = getPFS();
	}
	
	protected void tearDown()
	{
		test.closePFSystem();
	}
	
	
}
