Readme.txt

Packages: 
	dataaccesslayer - The package responsible for handling the database backend and stub database. This package should only be used by the system package and never by the gui package.

 	dataaccesslayertests - The package responsible for handling the testing of the dataaccesslayer package.

 	domainobjects - The package responsible for handling the core data types used by all parts of the software suite: GUI, System, and Database.

 	domainobjecttests - The package responsible for handling the testing of the domainobject package.

 	gui - The package responsible for handling all GUI classes used by the software suite. There should only be gui classes in this package.

 	system - The package responsible for handling all business logic for the application. This handles the access to the core data types: Label, PayTo, and Expenses.

 	systemtest - The package responsible for handling the testing of the system package.

 	util - The package responsible for handling all classes that contain helper functions that could be used in any part of the application.

 	utiltests - The package responsible for handing the testing of the util package.

 
 Main Files:
 	Main.java - The main entry point for the application.

 	ViewExpense.java - The main gui used for the application.

 	PFSystem.java - The main access point to all systems from the system package.
 	
 	*EditExpense.java - The main window that handles the editing and viewing of expeenses.

 	ExpenseManagment.java - The system used to manage expenses.

 	LabelManagement.java - The system used to manage labels.

 	PayToManagement.java - The system used to manage payTos.

 	Expense.java - The main object used in this system. The expense is the core object for our expense tracking system.

 	IDSet.java - The most used class that contains a set of ids used by the system to get expenses, labels, or paytos.

 Architecture:
 	GUI <--> System <--> *HSQL Database

 	Domain Objects are used by the GUI, System, and Stub Database
 	
 	The system does not return collections of labels, paytos, or expenses. Instead it returns an ID set. The system can return a single expense, label, or payto based on a given id. This is to avoid accidently editing a value in the class and changing a value else where.
 	
 	*The systemTests using jmock are not included in the test scripts as the are not threadsafe, but when run using and IDE they compile and pass.
