rem Script to compile tutorial project

echo on
set PSL_HOME=C:\pslcvs
set ALP_INSTALL_PATH=%PSL_HOME%\jars\cougaar\

rem Regenerate and recompile all property/asset files
call makeassets

rem compile the code
set LIBPATHS=%ALP_INSTALL_PATH%\core.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\build.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\glm.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\planserver.jar
set LIBPATHS=%LIBPATHS%;%PSL_HOME%


javac -classpath %LIBPATHS% tutorial\*.java tutorial\assets\*.java
