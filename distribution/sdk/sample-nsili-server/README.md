<!--
/*
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
-->
<img src="https://tools.codice.org/wiki/download/attachments/1179800/ddf.jpg"/>
# [Codice Alliance](http://github.com/codice/alliance/)

## Mock NSILI Server

Codice Alliance contains a sample NSILI compliant server to be used for testing purposes.  This utility can be run from the command-line within the sample-nsili-server directory by using maven and specifying a web and corba port.

```
mvn -Pcorba.server -Dexec.args=WEBPORT,CORBAPORT
```