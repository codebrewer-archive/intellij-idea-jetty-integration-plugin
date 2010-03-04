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
echo JAVA_HOME does not point at a JDK or JRE.  Either set the JAVA_HOME environment variable or specify a JDK for your IDEA project.
exit 1

:JavaFound
if exist "%JETTY_HOME%\start.jar" goto StartJarFound
echo JETTY_HOME\start.jar was not found.  Check your Jetty installation.
exit 1

:StartJarFound
if defined JETTY_OPTS goto JettyOptsSet
echo JETTY_OPTS was not set before attempting to launch Jetty.
exit 1

:JettyOptsSet
if defined JAVA_OPTS set JETTY_OPTS=%JAVA_OPTS% %JETTY_OPTS%

set PWD=%CD%
cd /d "%JETTY_HOME%"

"%JAVA_HOME%\bin\java.exe" %JETTY_OPTS% %*

cd /d "%PWD%"

exit 0
