#!/bin/sh

#
# Copyright 2007, 2008 Mark Scott
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#
# $Id$
#

set -e

#/usr/bin/env | /usr/bin/sort >/tmp/jetty-env

if [ ! -x "${JAVA_HOME}/bin/java" ]; then
  echo "JAVA_HOME does not point at a JDK or JRE.  Either set the JAVA_HOME environment variable or specify a JDK for your IDEA project."
  exit 1
fi

if [ ! -f "${JETTY_HOME}/start.jar" ]; then
  echo "JETTY_HOME/start.jar was not found.  Check your Jetty installation."
  exit 1
fi

if [ -z "${JETTY_OPTS}" ]; then
  echo "JETTY_OPTS was not set before attempting to launch Jetty."
  exit 1
fi

if [ ! -z "${JAVA_OPTS}" ]; then
  JETTY_OPTS="${JAVA_OPTS} ${JETTY_OPTS}"
fi

cd "${JETTY_HOME}" || exit 1

"${JAVA_HOME}/bin/java" ${JETTY_OPTS} "${@}"

cd - >/dev/null

exit 0