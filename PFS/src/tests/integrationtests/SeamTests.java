package tests.integrationtests;

import system.PFSystem;

public class SeamTests extends Seam
{	
	public SeamTests(String arg0) {
		super(arg0);
	}

	public PFSystem getPFS()
	{
		PFSystem.forceReal();
		return PFSystem.getCurrent();
	}

}
