@echo OFF

if "%ALP_INSTALL_PATH%"=="" goto AIP_ERROR
if "%1"=="" goto ARG_ERROR

set PSL_HOME=C:\valetto\columbia

set LIBPATHS=%ALP_INSTALL_PATH%\lib\core.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\glm.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\planserver.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\xml4j_2_0_11.jar
set LIBPATHS=%LIBPATHS%;%PSL_HOME%

REM pass in "NodeName" to run a specific named Node

set MYPROPERTIES=-Dorg.cougaar.useBootstrapper=false
set MYMEMORY=
set MYCLASSES=psl.workflakes.coolets.WorkletNode
set MYARGUMENTS= -c -n "%1"

@ECHO ON

java.exe %MYPROPERTIES% %MYMEMORY% -classpath %LIBPATHS% %MYCLASSES% %MYARGUMENTS% %2 %3
goto QUIT

:AIP_ERROR
echo Please set ALP_INSTALL_PATH
goto QUIT

:ARG_ERROR
echo Run requires an argument  eg: Run ExerciseOneNode
goto QUIT

:QUIT
