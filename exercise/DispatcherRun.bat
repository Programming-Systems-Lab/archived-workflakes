@echo OFF

set PSL_HOME=C:\pslcvs
set ALP_INSTALL_PATH=%PSL_HOME%\tools\cougaar-8.8

if "%ALP_INSTALL_PATH%"=="" goto AIP_ERROR
if "%1"=="" goto ARG_ERROR

set LIBPATHS=%ALP_INSTALL_PATH%\lib\core.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\glm.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\planserver.jar
set LIBPATHS=%LIBPATHS%;%PSL_HOME%\tools\xerces.jar
set LIBPATHS=%LIBPATHS%;%PSL_HOME%
set LIBPATHS=.;%LIBPATHS%

REM pass in "NodeName" to run a specific named Node

set MYPROPERTIES=
set MYMEMORY=
set MYCLASSES= psl.workflakes.exercise.tutorial.Exc6WklDispatcher
set MYARGUMENTS= %1 %2 %3 %4

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
