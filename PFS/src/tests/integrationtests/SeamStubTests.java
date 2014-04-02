package tests.integrationtests;

import system.PFSystem;

public class SeamStubTests extends Seam
{	
	public SeamStubTests(String arg0) {
		super(arg0);
	}

	public PFSystem getPFS()
	{
		PFSystem.forceStub();
		return PFSystem.getCurrent();
	}

}
