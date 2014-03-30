package tests.AT;

import dataaccesslayer.Database;

public class ATdatabase {

	private Database database;
	
	public void startUp()
	{
		database = new Database("ATtest");
		database.open("ATtest");
	}
	
	public void shutDown()
	{
		database.close();
	}
}
