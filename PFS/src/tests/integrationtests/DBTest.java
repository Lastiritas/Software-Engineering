package tests.integrationtests;

import system.PFSystem;

public class DBTest extends SeamTests
{	
	public DBTest(String arg0) {
		super(arg0);
	}

	public PFSystem getPFS()
	{
		PFSystem.forceReal();
		return PFSystem.getCurrent();
	}

}
