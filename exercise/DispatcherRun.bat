@echo OFF

set PSL_HOME=C:\pslcvs
set ALP_INSTALL_PATH=%PSL_HOME%\jars\cougaar

if "%ALP_INSTALL_PATH%"=="" goto AIP_ERROR
if "%1"=="" goto ARG_ERROR

set LIBPATHS=%ALP_INSTALL_PATH%\core.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\glm.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\planserver.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\xml4j_2_0_11.jar
set LIBPATHS=%LIBPATHS%;%PSL_HOME%
set LIBPATHS=.;%LIBPATHS%

REM pass in "NodeName" to run a specific named Node

set MYPROPERTIES=
set MYMEMORY=
set MYCLASSES= psl.workflakes.exercise.tutorial.Exc6WklDispatcher
set MYARGUMENTS= %1 %2 %3

@ECHO ON

java.exe %MYPROPERTIES% %MYMEMORY% -classpath %LIBPATHS% %MYCLASSES% %MYARGUMENTS%
goto QUIT

:AIP_ERROR
echo Please set ALP_INSTALL_PATH
goto QUIT

:ARG_ERROR
echo Run requires 3 arguments  eg: DispatcherRun remotehost remotename remoteport 
goto QUIT

:QUIT
