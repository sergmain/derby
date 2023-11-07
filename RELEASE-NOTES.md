# Release Notes for Apache Derby 10.15.0.0

These notes describe the difference between Apache Derby release 10.15.0.0 and the preceding release 10.14.2.0.

- [Overview](file:///D:/workspace/goodstudio/derby/RELEASE-NOTES.html#Overview)
- [New Features](file:///D:/workspace/goodstudio/derby/RELEASE-NOTES.html#New Features)
- [Bug Fixes](file:///D:/workspace/goodstudio/derby/RELEASE-NOTES.html#Bug Fixes)
- [Issues](file:///D:/workspace/goodstudio/derby/RELEASE-NOTES.html#Issues)
- [Build Environment](file:///D:/workspace/goodstudio/derby/RELEASE-NOTES.html#Build Environment)
- [Verifying Releases](file:///D:/workspace/goodstudio/derby/RELEASE-NOTES.html#Verifying Releases)

## Overview

The most up to date information about Derby releases can be found on the [Derby download page](https://db.apache.org/derby/derby_downloads.html).

Apache Derby is a pure Java relational database engine using standard SQL and JDBC as its APIs. More information about Derby can be found on the [Apache web site](https://db.apache.org/derby/). Derby functionality includes:

- Embedded engine with JDBC drivers
- Network Server
- Network client JDBC drivers
- Command line tools: ij (SQL scripting), dblook (schema dump) and sysinfo (system info)

The 10.15 release family supports the following Java and JDBC versions:

- Java SE 9 and higher with JDBC 4.2.

## New Features

This is a feature release. The following new feature was added:

- **JPMS modularization** - Derby has been re-packaged as a set of JPMS modules. This introduced a new jar file, *derbyshared.jar*, required by all configurations. Module diagrams for Derby configurations can be found in the [javadoc](https://db.apache.org/derby/docs/10.15/publishedapi/index.html) for the 10.15 public API.

New users should consult the [10.15 documentation](https://db.apache.org/derby/manuals/index.html#docs_10.15), especially the [Getting Started With Derby](https://db.apache.org/derby/docs/10.15/getstart/index.html) guide.

Existing users who want to continue running Derby with a classpath  should read the extended release note for issue DERBY-6945 (see below).

Existing users who want to run Derby with a module path should consult the module diagrams in the [javadoc](https://db.apache.org/derby/docs/10.15/publishedapi/index.html) for the 10.15 public API. Templates for wiring together a module path can be found in the *setEmbeddedCP*, *setNetworkServerCP*, and *setNetworkClientCP* scripts located in the bin directory of the release distributions, as described by the "Manually setting the CLASSPATH/MODULEPATH environment variables" topic in the [Getting Started With Derby](https://db.apache.org/derby/docs/10.15/getstart/index.html) guide.

## Bug Fixes

The following issues are addressed by Derby release 10.15.0.0. These  issues are not addressed in the preceding 10.14.2.0 release.

| Issue Id                                                     | Description                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [DERBY-7020](https://issues.apache.org/jira/browse/DERBY-7020) | Fix release targets to account for modularization changes    |
| [DERBY-7018](https://issues.apache.org/jira/browse/DERBY-7018) | Test the demo programs after the changes made by DERBY-6945  |
| [DERBY-7016](https://issues.apache.org/jira/browse/DERBY-7016) | Adjust the set*CP scripts to include derbyshared.jar and to set a MODULEPATH variable as well |
| [DERBY-6981](https://issues.apache.org/jira/browse/DERBY-6981) | "SQLSTATE: XJ001, SQLERRMC: java.lang.NullPointerExceptionXJ001.U" |
| [DERBY-6980](https://issues.apache.org/jira/browse/DERBY-6980) | Documentation changes to accompany jigsaw-modularization of derby |
| [DERBY-6973](https://issues.apache.org/jira/browse/DERBY-6973) | Provide SHA-512 checksums on future releases                 |
| [DERBY-6945](https://issues.apache.org/jira/browse/DERBY-6945) | Re-package Derby as a collection of jigsaw modules           |
| [DERBY-6856](https://issues.apache.org/jira/browse/DERBY-6856) | Make it possible to build Derby using JDK 9                  |
| [DERBY-5543](https://issues.apache.org/jira/browse/DERBY-5543) | include debug info in derby builds uploaded to maven         |

## Issues

- [Note for DERBY-6945:  Modularize Derby, cleanly partitioning its packages across a small set of JPMS components. ](file:///D:/workspace/goodstudio/derby/RELEASE-NOTES.html#Note for DERBY-6945)

Compared with the previous release (10.14.2.0), Derby release  10.15.0.0 introduces the following new features and incompatibilities.  These merit your special attention.

------

### Note for DERBY-6945

#### Summary of Change

Modularize Derby, cleanly partitioning its packages across a small set of JPMS components.

#### Symptoms Seen by Applications Affected by Change

A new jar file (*derbyshared.jar*) has been added. All Derby configurations require it. In addition, the *derbytools.jar* library is now required when running the network server and/or when using Derby DataSources.

More privileges must be granted to the Derby jar files when running under a Security Manager.

Derby jar files can now be wired into a modulepath for use by module-aware applications.

#### Incompatibilities with Previous Release

Legacy applications may fail if their classpaths don't contain the required jar files. Code common to all Derby configurations has been isolated in the new *derbyshared.jar* file. DataSources have moved from *derbyclient.jar* and *derby.jar* into *derbytools.jar*

Legacy applications which run under a Java SecurityManager may fail due to insufficient privilege grants.

#### Rationale for Change

Derby was divided into JPMS components for the following reasons:

- **Footprint** - Modularization reduces Derby's footprint when running embedded on resource-constrained devices.
- **Security** - Modularization lets Derby protect its code via package-level encapsulation.

#### Application Changes Required

Consult the module diagrams for configurations described on the landing page of the [10.15 public API](https://db.apache.org/derby/docs/10.15/publishedapi/index.html). Then adjust your application's classpath as follows:

- **Remote client** - When running remote client applications, make sure that the classpath includes *derbyshared.jar*. Remote applications which use Derby DataSources should also include *derbytools.jar*.
- **Embedded engine** - When running the embedded engine, make sure that the classpath includes *derbyshared.jar*. Embedded applications which use Derby DataSources should also include *derbytools.jar*.
- **Network server** - When running the network server, make sure that the classpath includes *derbyshared.jar* and *derbytools.jar*.
- **Tools** - When running Derby tools like *ij*, *dblook*, and *sysinfo*, make sure that the classpath includes *derbyshared.jar*.

Java security policy files must grant additional privileges to Derby jar files. For more information, see the "Configuring Java Security" topic in the [*Derby Security Guide*](https://db.apache.org/derby/docs/10.15/security/index.html) and consult the following template policy files in the *demo/templates* directory of the bin distribution:

- **clientTemplate.policy** - Privileges needed by remote client applications.
- **engineTemplate.policy** - Privileges needed by applications which embed the Derby engine.
- **serverTemplate.policy** - Privileges needed when running the network server.
- **toolsTemplate.policy** - Privileges needed when running Derby tools.

## Build Environment

Derby release 10.15.0.0 was built using the following environment:

- **Branch** - Source code came from the 10.15 branch.
- **Machine** - Mac OSX 10.11.6.
- **Ant** - Apache Ant(TM) version 1.10.2 compiled on February 3 2018.
- **Compiler** - All classes were compiled by the javac from OpenJDK 64-Bit Server VM 18.9 (build 11+28, mixed mode).

## Verifying Releases

It is essential that you verify the integrity of the downloaded files using the PGP and SHA-512 signatures.  SHA-512 verification ensures the file was not corrupted during the download process.  PGP verification ensures that the file came from a certain person.

The PGP signatures can be verified using [PGP](https://www.pgpi.org/) or [GPG](https://www.gnupg.org/). First download the Apache Derby [KEYS](https://dist.apache.org/repos/dist/release/db/derby/KEYS) as well as the `asc` signature file for the particular distribution. It is important that you get these files from the ultimate trusted source - the main ASF distribution site, rather than from a mirror. Then verify the signatures using ...

```
% pgpk -a KEYS
% pgpv db-derby-X.Y.tar.gz.asc

or

% pgp -ka KEYS
% pgp db-derby-X.Y.tar.gz.asc

or

% gpg --import KEYS
% gpg --verify db-derby-X.Y.tar.gz.asc
```

To verify the SHA-512 checksums on the files, you need to use a platform-specific program. On Mac OSX, this program is called `shasum`, on Linux it is called `sha512sum`, and on Windows it is called `CertUtil`.

We strongly recommend that you verify your downloads with both PGP and SHA-512.