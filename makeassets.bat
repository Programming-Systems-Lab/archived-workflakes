rem Script to generate asset classes
set PSL_HOME=C:\pslcvs
set ALP_INSTALL_PATH=%PSL_HOME%\tools\cougaar-8.8


set LIBPATHS=%ALP_INSTALL_PATH%\lib\core.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\lib\build.jar
echo on

rem Regenerate and recompile all property/asset files
cd coolets\assets
java -classpath %LIBPATHS% org.cougaar.tools.build.AssetWriter properties.def -Ppsl.workflakes.coolets.assets ExecAgents_assets.def 
java -classpath %LIBPATHS% org.cougaar.tools.build.PGWriter properties.def
cd ..\..
