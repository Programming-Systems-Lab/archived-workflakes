rem Script to generate asset classes
set PSL_HOME=C:\valetto\columbia
set ALP_INSTALL_PATH=%PSL_HOME%\jars\cougaar

set LIBPATHS=%ALP_INSTALL_PATH%\core.jar
set LIBPATHS=%LIBPATHS%;%ALP_INSTALL_PATH%\build.jar
echo on

rem Regenerate and recompile all property/asset files
cd tutorial\assets
java -classpath %LIBPATHS% org.cougaar.tools.build.AssetWriter properties.def -Ppsl.workflakes.exercise.tutorial.assets programmer_assets.def
java -classpath %LIBPATHS% org.cougaar.tools.build.PGWriter properties.def
cd ..\..
