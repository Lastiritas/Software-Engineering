REM Not all .act scripts are active

REM The tests do not comply with database usage the way it is invoked

REM The tests also cannot test the graphing in the program properly

REM @echo off

IF "%1" NEQ "" (SET SLEEP=%1) ELSE (SET SLEEP=1)

call setClasspath

set SLEEP=1

java -cp %CLASSPATH% acceptanceTests.TestRunner %SLEEP%

call RestoreDBTests

pause



