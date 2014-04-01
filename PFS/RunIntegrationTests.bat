REM @echo off

call setClasspath

REM @echo on

REM java junit.textui.TestRunner tests.IntegrationTests

java junit.swingui.TestRunner tests.IntegrationTests

call RestoreDBTests

pause

