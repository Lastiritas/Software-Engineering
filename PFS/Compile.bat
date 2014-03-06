REM @echo off

REM
REM This script requires that the "javac" command be on the system command path.
REM This can be accomplished by modifying the path statement below
REM to include "C:\Program Files\Java\jre7\bin" (or whatever your path is).
REM Alternatively, you could add the path to "javac" to the PATH Environment variable: 
REM   Settings-->Control Panel-->System-->Advanced-->Environment Variables-->Path
REM


REM clean all .class files out of the bin directory

cd bin
erase /S /Q *.class
cd ..


call setClasspath


REM @echo on

REM javac -d bin\ -cp %classpath% src\domainobjects\*.java

REM javac -d bin\ -cp %classpath% src\dataAccessLayer\*.java

javac -d bin\ -cp %classpath% src\domainobjects\*.java src\dataAccessLayer\*.java src\application\*.java src\system\*.java src\system\datamining\*.java src\util\*.java src\gui\*.java

javac -d bin\ -cp %classpath% src\tests\domainobjecttests\*.java
javac -d bin\ -cp %classpath% src\tests\dataAccessLayerTests\*.java
javac -d bin\ -cp %classpath% src\systemTests\*.java
javac -d bin\ -cp %classpath% src\tests\utiltests\*.java
javac -d bin\ -cp %classpath% src\tests\*.java

pause

