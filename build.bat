rem Script to compile coolets project

echo on

rem compile the code
set PSL_HOME=C:\valetto\columbia
set ALP_INSTALL_PATH=%PSL_HOME%\jars\cougaar\

set LIBPATHS=%ALP_INSTALL_PATH%\core.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\build.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\glm.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\planserver.jar

javac -classpath .;%LIBPATHS%;%PSL_HOME% coolets\*.java coolets\adaptors\*.java

rem compile the smartinf code

javac -classpath .;%LIBPATHS%;%PSL_HOME% smartinf\*.java smartinf\smartjunction\*.java