## Summary

Apache Derby is a standards-based, pure-Java relational database engine. Its easy-to-use data manager requires no administration by end users. Derby runs on any JVM at Java version 9 or higher and Derby supports JDBC  version 4.3. Derby's platform-independent database format may be copied to any file system.

Apache Derby is a subproject of the Apache DB project, licensed under the Apache License, Version 2.0, which you may obtain from https://www.apache.org/licenses/LICENSE-2.0 You may find Derby on the web at https://db.apache.org/derby/. You may download Apache Derby releases from https://db.apache.org/derby/derby_downloads.html.

Information on Derby configurations follows.

## Derby Configurations

Derby's modules may be wired together in several configurations. The modules are:

| Module name                    | Jar file               | Description                                                  |
| ------------------------------ | ---------------------- | ------------------------------------------------------------ |
| org.apache.derby.engine        | derby.jar              | The embedded database engine contains Derby's core      functionality. It contains its own JDBC driver, allowing      one JVM to host both Derby and the applications which use it. |
| org.apache.derby.server        | derbynet.jar           | The network server wraps the database engine, enabling      networked JDBC access. |
| org.apache.derby.client        | derbyclient.jar        | The remote JDBC client connects to a server across a      network. |
| org.apache.derby.tools         | derbytools.jar         | Basic tools include a schema dumper and an interactive SQL interpreter. |
| org.apache.derby.optionaltools | derbyoptionaltools.jar | Optional tools support metadata introspection and access to      other vendors' databases. |
| org.apache.derby.runner        | derbyrun.jar           | The runner module supports easy command-line administration      of Derby installations. |
| org.apache.derby.commons       | derbyshared.jar        | The commons module contains cross-module support utilities.  |
| org.apache.derby.locale_*      | derbyLocale_*.jar      | Message localizations support human-readable diagnostics      in languages other than English. |
| org.apache.derby.tests         | derbyTests.jar         | An extensive body of tests stresses functionality in all of      the other modules. |

The following conventions apply to the module diagrams presented here:

- Derby modules appear in **blue**.
- JVM modules appear in **black**.
- 3rd party modules appear in **pink**.
- Optional modules appear in **gray**.