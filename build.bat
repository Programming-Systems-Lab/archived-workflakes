rem Script to compile coolets project

echo on

rem compile the code

set PSL_HOME=C:\valetto\columbia

set LIBPATHS=%ALP_INSTALL_PATH%\lib\core.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\build.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\glm.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\planserver.jar

javac -classpath .;%LIBPATHS%;%PSL_HOME% coolets\*.java coolets\adaptors\*.java

rem compile the smartinf code

javac -classpath .;%LIBPATHS%;%PSL_HOME% smartinf\*.java smartinf\smartjunction\*.java