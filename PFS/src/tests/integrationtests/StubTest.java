package tests.integrationtests;

import system.PFSystem;

public class StubTest extends SeamTests
{	
	public StubTest(String arg0) {
		super(arg0);
	}

	public PFSystem getPFS()
	{
		PFSystem.forceStub();
		return PFSystem.getCurrent();
	}

}
