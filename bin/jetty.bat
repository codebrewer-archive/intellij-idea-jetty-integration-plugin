@echo off

::
:: Copyright 2007, 2008, 2010 Mark Scott, Chris Miller
::
:: Licensed under the Apache License, Version 2.0 (the "License");
:: you may not use this file except in compliance with the License.
:: You may obtain a copy of the License at
::
::     http://www.apache.org/licenses/LICENSE-2.0
::
:: Unless required by applicable law or agreed to in writing, software
:: distributed under the License is distributed on an "AS IS" BASIS,
:: WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
:: See the License for the specific language governing permissions and
:: limitations under the License.
::

::
:: (Naïve) DOS batch file for controlling Jetty
::
:: $Id$
::

setlocal

if exist "%JAVA_HOME%\bin\java.exe" goto JavaFound
echo "JAVA_HOME does not point at a JDK or JRE.  Either set the JAVA_HOME environment variable or specify a JDK for your IDEA project."
exit 1

:JavaFound
if exist "%JETTY_HOME%\start.jar" goto StartJarFound
echo "JETTY_HOME\start.jar was not found.  Check your Jetty installation."
exit 1

:StartJarFound
::
:: The following two lines depend on command extensions being enabled, and that
:: can't be enforced when IDEA launches Jetty (the launcher isn't under the
:: plug-in's.control.  If command extensions are off by default then uncomment
:: lines 43 and 44 and comment out lines 46 and 47.
::
::if "%JETTY_OPTS%" == "" set JETTY_OPTS=-cp start.jar org.mortbay.start.Main
::if not "%JAVA_OPTS%" == "" set JETTY_OPTS=%JAVA_OPTS% %JETTY_OPTS%
::
if not defined %JETTY_OPTS% set JETTY_OPTS=-cp start.jar org.mortbay.start.Main
if defined %JAVA_OPTS% set JETTY_OPTS=%JAVA_OPTS% %JETTY_OPTS%

set PWD=%CD%
cd /d "%JETTY_HOME%"

"%JAVA_HOME%\bin\java.exe" %JETTY_OPTS% %*

cd /d "%PWD%"

exit 0
