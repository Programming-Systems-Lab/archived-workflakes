rem Script to compile coolets project

echo on

rem compile the code
set PSL_HOME=C:\pslcvs
set ALP_INSTALL_PATH=%PSL_HOME%\jars\cougaar\

rem generate assets
call makeassets

set LIBPATHS=%ALP_INSTALL_PATH%\core.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\build.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\glm.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\planserver.jar
set LIBPATHS=%LIBPATHS%;%PSL_HOME%\jars\siena-1.1.2.jar

javac -classpath .;%LIBPATHS%;%PSL_HOME% coolets\*.java coolets\adaptors\*.java coolets\assets\*.java

rem compile the smartinf code

javac -classpath .;%LIBPATHS%;%PSL_HOME% smartinf\*.java smartinf\smartjunction\*.java
