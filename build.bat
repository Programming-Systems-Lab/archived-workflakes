rem Script to compile coolets project

echo on

rem compile the code
set PSL_HOME=C:\pslcvs
set WKLLIB=%PSL_HOME%\psl\demo\2002\jun\workflakes\worklets.jar
set ALP_INSTALL_PATH=%PSL_HOME%\tools\cougaar-8.8

rem generate assets
call makeassets

set LIBPATHS=%ALP_INSTALL_PATH%\lib\core.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\build.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\glm.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\planserver.jar
set LIBPATHS=%LIBPATHS%;%PSL_HOME%\tools\siena-1.4.2\siena-1.4.2.jar

javac -deprecation -classpath .;%LIBPATHS%;%PSL_HOME% coolets\*.java coolets\adaptors\*.java coolets\assets\*.java

rem compile the smartinf code

javac -classpath .;%LIBPATHS%;%PSL_HOME% smartinf\*.java smartinf\smartjunction\*.java
