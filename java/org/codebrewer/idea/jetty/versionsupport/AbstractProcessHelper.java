/*
 * Copyright 2010 Mark Scott
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codebrewer.idea.jetty.versionsupport;

import org.jetbrains.annotations.NonNls;

/**
 * @author Mark Scott
 * @version $Id$
 */
public abstract class AbstractProcessHelper implements ProcessHelper
{
  @NonNls
  static final String STOP_COMMAND_TEMPLATE = "-DSTOP.PORT={0,number,#####} -DSTOP.KEY={1} -jar start.jar --stop";
}
