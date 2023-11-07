| ![Derby Hat](https://issues.apache.org/jira/secure/attachment/12322583/12322583_final_logo.png) | Building Derby |
| ------------------------------------------------------------ | -------------- |
|                                                              |                |

- [About This Document](file:///D:/workspace/goodstudio/derby/BUILDING.html#About This Document)
- [Downloads](file:///D:/workspace/goodstudio/derby/BUILDING.html#Downloads)
- Simple Build
  - [Verifying the Build](file:///D:/workspace/goodstudio/derby/BUILDING.html#Verifying the Build)
  - [Testing Derby](file:///D:/workspace/goodstudio/derby/BUILDING.html#Testing Derby)
- [Customized Build](file:///D:/workspace/goodstudio/derby/BUILDING.html#Customized Build)





------

## About This Document

These are the instructions for building the Derby jar files from the Derby sources.





------

## Downloads

Before building Derby, you need to download the following:

| **Prerequisite**            | **Description**                                              |
| --------------------------- | ------------------------------------------------------------ |
| ***Derby Source\***         | If you are reading these instructions, chances  are you have already unpacked a Derby source distribution. However,  if you don't have the Derby source yet, get the development source tree from subversion by following these      [instructions](http://db.apache.org/derby/dev/derby_source.html). |
| ***Java Development Kit\*** | You need to install a Java 9 JDK. Probably, your machine      already has this JDK. If not, Oracle and IBM supply free JDKs for many machines. |
| ***Ant\***                  | You need to install the Ant build tool, version 1.10.6 or      higher. You can get Ant [here](http://ant.apache.org/). |


 If you are going to run the Derby tests, then you will need to download the JUnit test harness also:               **Prerequisite**      **Description**               ***JUnit\***      You need the JUnit test tool, version 3.8.2.      The Ant build script will try to download JUnit for you      automatically if it's missing. If this fails for some reason,      you can download and install JUnit manually.      You can get JUnit 3.8.2 [here](http://www.junit.org/).      Copy *junit.jar* into your Derby source tree, in the      *tools/java* directory.               
 



## Simple Build

Before building Derby, cd to the root of your Derby source distribution. That is the top level directory which contains the LICENSE and NOTICE files. In addition, make sure that the version of Java which you are using is Java 9. You  can verify this by checking the output of the following command:

> ```
> java -version
> ```

Now use the following Ant targets to build Derby.:

| **Target**         | **Description**                                              | **Command**                    |
| ------------------ | ------------------------------------------------------------ | ------------------------------ |
| ***clobber\***     | This target deletes all build artifacts.                     | ` **ant -quiet clobber** `     |
| ***buildsource\*** | This target compiles all source files needed for the Derby      product. | ` **ant -quiet buildsource** ` |
| ***buildjars\***   | This target builds the Derby jar files.                      | ` **ant -quiet buildjars** `   |



So, do this:

> ```
> ant -quiet clobber
> ant -quiet buildsource
> ant -quiet buildjars
> ```

In order to build the Derby javadoc, you will need to run another target:

| **Target**     | **Description**                                              | **Command**                |
| -------------- | ------------------------------------------------------------ | -------------------------- |
| ***javadoc\*** | This target builds the Derby javadoc. Be patient. The      *javadoc* target takes a while. | ` **ant -quiet javadoc** ` |

> ### Verifying the Build
>
> Run the *sysinfo* command to verify that the jars built correctly. This program will print out the Derby build information:
>
> > ```
> > java -jar jars/sane/derbyrun.jar sysinfo
> > ```
>
> ### Testing Derby
>
> If you want to build and run the Derby tests, make sure that you have downloaded the [JUnit](file:///D:/workspace/goodstudio/derby/BUILDING.html#Downloading JUnit) test framework. Then build all of the Derby sources, including the test classes:
>
> | **Target** | **Description**                              | **Command**            |
> | ---------- | -------------------------------------------- | ---------------------- |
> | ***all\*** | This target compiles all Derby source files. | ` **ant -quiet all** ` |
>
> Putting all of this together, here's how you build the tests:
>
> > ```
> > ant -quiet clobber
> > ant -quiet all
> > ant -quiet buildjars
> > ```
>
> To run the tests, consult the testing [README](file:///D:/workspace/goodstudio/derby/java/testing/README.htm).





------

## Customized Build

You can customize the Derby build by setting variables in a file called *ant.properties*. The Ant tool looks for this file in your home directory.  To find out where Ant thinks your home directory is, issue the following command and look for "user.home" in the output:

> ```
> ant -diagnostics
> ```



Alternatively, or additionally, you can place properties in a file  called 'local.properties' in the top of the source tree. Properties  placed in this file have precedence over those in  user.home/ant.properties. 

Here are some Derby-specific variables which you may want to set in *ant.properties*:

| **Variable**       | **Description**                                              | **Default**            | **Example**                        |
| ------------------ | ------------------------------------------------------------ | ---------------------- | ---------------------------------- |
| ***deprecation\*** | Turn this flag off if you don't want to see pages of      warnings generated when the compiler encounters references to      deprecated methods. | *on*                   | ` **deprecation=off** `            |
| ***sane\***        | By default, this variable is set to *true*. This builds      extra assertion and debugging logic into Derby classes. If you      set this variable to false, then the Derby jar files will be      smaller and Derby will run faster. During typical development,      you will leave this variable set to *true*. However, this      variable is set to *false* when building official Derby releases. | *true*                 | ` **sane=false** `                 |
| ***junit\***       | Setting this property will overwrite the default location for           *junit.jar*, used to build the tests.       The default location is *tools/java*.      If you set this property, the build will not automatically download       *junit.jar*, and if it's not in the specified location, the build will      stop. | *tools/java/junit.jar* | ` **junit=/local0/junit.jar** `    |
| ***debuglevel\***  | Setting this property will overwrite the debug level of the      javac compilation. By default, sane (debug) builds compile with debug level      "lines,source,vars", that is, all debugging information. In      contrast, insane (production) builds compile with debug level        "lines,source", that is, just enough debugging information to      annotate stack traces with line numbers. If you set this      variable to "none" and do an insane (production) build, then no      debugging information will be produced and the jar files will be      as slim as possible. | *lines,source,vars*    | ` **sane=false debuglevel=none** ` |







------