REM @echo off

call setClasspath

REM @echo on

REM java junit.textui.TestRunner tests.IntegrateTests

java junit.swingui.TestRunner tests.IntegrateTests

call RestoreDB