rem Script to compile coolets project

echo on

rem compile the code
set PSL_HOME=D:\Peppo\Codice
set WKLLIB=%PSL_HOME%\psl\jars\demo2002\worklets.jar
set ALP_INSTALL_PATH=%PSL_HOME%\tools\cougaar-8.8

rem generate assets
call makeassets

set LIBPATHS=%ALP_INSTALL_PATH%\lib\core.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\build.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\glm.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\planserver.jar
set LIBPATHS=%LIBPATHS%;%PSL_HOME%\jars\siena-1.1.2.jar

javac -deprecation -classpath .;%LIBPATHS%;%WKLLIB%;%PSL_HOME% coolets\*.java coolets\adaptors\*.java coolets\assets\*.java

rem compile the smartinf code

javac -classpath .;%LIBPATHS%;%WKLLIB%;%PSL_HOME% smartinf\*.java smartinf\smartjunction\*.java
