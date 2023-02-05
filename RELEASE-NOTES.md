# Release Notes for Apache Derby 10.16.1.1

These notes describe the difference between Apache Derby release 10.16.1.1 and the preceding release 10.15.2.0.

- [Overview](#Overview)
- [New Features](#New Features)
- [Bug Fixes](#Bug Fixes)
- [Issues](#Issues)
- [Build Environment](#Build Environment)
- [Verifying Releases](#Verifying Releases)

## Overview

The most up to date information about Derby releases can be found on the [Derby download page](https://db.apache.org/derby/derby_downloads.html).

Apache Derby is a pure Java relational database engine using standard SQL and JDBC as its APIs. More information about Derby can be found on the [Apache web site](https://db.apache.org/derby/). Derby functionality includes:

- Embedded engine with JDBC drivers
- Network Server
- Network client JDBC drivers
- Command line tools: ij (SQL scripting), dblook (schema dump) and sysinfo (system info)

The 10.16 release family supports the following Java and JDBC versions:

- Java SE 17 and higher with JDBC 4.2.

## New Features

This is a feature release. The following significant change has been made:

- **SecurityManager deprecated** - Derby no longer supports the Java SecurityManager. This is because the Open JDK team deprecated the SecurityManager and marked it for removal. This deprecation happened under the aegis of [JEP 411](https://openjdk.java.net/jeps/411).

New users should consult the [10.16 documentation](https://db.apache.org/derby/manuals/index.html#docs_10.16), especially the [Getting Started With Derby](https://db.apache.org/derby/docs/10.16/getstart/index.html) guide.

Existing users who want to run Derby with a SecurityManager must NOT upgrade to version 10.16. Instead, those users must continue to use older versions of Derby. Please see the [Derby download page](https://db.apache.org/derby/derby_downloads.html) in order to understand how Derby and Java versions correspond.

## Bug Fixes

The following issues are addressed by Derby release 10.16.1.1. These issues are not addressed in the preceding 10.15.2.0 release.

| Issue Id                                                     | Description                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [DERBY-7040](https://issues.apache.org/jira/browse/DERBY-7040) | Add dependency stanzas to maven poms                         |
| [DERBY-7137](https://issues.apache.org/jira/browse/DERBY-7137) | Compile 10.16 into Java 17 byte code so that it won't run on earlier platforms |
| [DERBY-7125](https://issues.apache.org/jira/browse/DERBY-7125) | Dead link in BUILDING.html                                   |
| [DERBY-7031](https://issues.apache.org/jira/browse/DERBY-7031) | Errors running upgrade tests from 10.15.1.0 release candidate to 10.16 trunk |
| [DERBY-7124](https://issues.apache.org/jira/browse/DERBY-7124) | Errors seen while running the junit-all target               |
| [DERBY-7053](https://issues.apache.org/jira/browse/DERBY-7053) | Further top build.xml streamlining                           |
| [DERBY-7050](https://issues.apache.org/jira/browse/DERBY-7050) | In build.xml of derby root there's a vestigial target (+ patch) |
| [DERBY-7087](https://issues.apache.org/jira/browse/DERBY-7087) | Make it possible to build and run tests cleanly on Java 15   |
| [DERBY-7110](https://issues.apache.org/jira/browse/DERBY-7110) | Make it possible to build and test Derby cleanly with OpenJDK 17 |
| [DERBY-7126](https://issues.apache.org/jira/browse/DERBY-7126) | Make it possible to build and test Derby cleanly with OpenJDK 18 |
| [DERBY-7088](https://issues.apache.org/jira/browse/DERBY-7088) | Make it possible to build and test Derby using JDK 16        |
| [DERBY-7046](https://issues.apache.org/jira/browse/DERBY-7046) | NoClassDefFoundError on 'java -jar derbynet.jar'             |
| [DERBY-7138](https://issues.apache.org/jira/browse/DERBY-7138) | Remove references to the Java Security Manager               |
| [DERBY-7052](https://issues.apache.org/jira/browse/DERBY-7052) | Reordering and (mildly) reorganizing build.xml Ant targets around buildsource |
| [DERBY-7057](https://issues.apache.org/jira/browse/DERBY-7057) | Unreferenced failing target in main build                    |
| [DERBY-7038](https://issues.apache.org/jira/browse/DERBY-7038) | Upgrade ant version and re-write javadoc build targets to use improved <javadoc>task |
| [DERBY-7041](https://issues.apache.org/jira/browse/DERBY-7041) | null pointer exception when creating view based on other views |

## Issues

- [Note for DERBY-7137: Derby 10.16 compiles into Java 17 byte code.](#Note for DERBY-7137)
- [Note for DERBY-7138: As part of JEP 411, the Open JDK team deprecated the SecurityManager and marked it for future removal. In response, Derby release 10.16 removes support for the SecurityManager.](#Note for DERBY-7138)

Compared with the previous release (10.15.2.0), Derby release 10.16.1.1 introduces the following new features and incompatibilities. These merit your special attention.

------

### Note for DERBY-7137

#### Summary of Change

Derby 10.16 compiles into Java 17 byte code.

#### Symptoms Seen by Applications Affected by Change

Derby 10.16 will not run on Java 16 or earlier JVMs.

#### Rationale for Change

Derby 10.16 no longer supports the Java SecurityManager (see [DERBY-7138](https://issues.apache.org/jira/browse/DERBY-7138)) due to its deprecation by Java 17 (see [JEP 411](https://openjdk.java.net/jeps/411)). Java 17 and Derby 10.16 must not be used by applications which install a SecurityManager.

#### Application Changes Required

Applications must not use the SecurityManager if they run with Derby 10.16 on Java 17 or later JVMs. Applications which need a SecurityManager must use earlier versions of Derby and the JVM. See the [Derby download page](https://db.apache.org/derby/derby_downloads.html) for the correspondence between Derby and JVM versions.

------

### Note for DERBY-7138

#### Summary of Change

As part of JEP 411, the Open JDK team deprecated the SecurityManager and marked it for future removal. In response, Derby release 10.16 removes support for the SecurityManager.

#### Symptoms Seen by Applications Affected by Change

Previous Derby releases let database administrators install a Java SecurityManager. This, in turn, let applications customize access to security-sensitive objects such as files, network sockets, system properties, and class loaders. In previous Derby releases, the Derby network server installed a SecurityManager with default permissions if the administrator forgot to provide a customized security policy.

Recently, Open JDK 17 deprecated the SecurityManager and marked it for future removal. Open JDK 17 also began writing warnings to the console when applications called SecurityManager-related methods.

Open JDK 18 goes even further. Open JDK 18 forces users to set *-Djava.security.manager=allow* when booting an application which installs a SecurityManager. Furthermore, the meaning of that property has changed since Open JDK 11. In Open JDK 11, *java.security.manager* was the name of a user-written SecurityManager class. The property is now a directive which is required for applications which want to run a SecurityManager.

#### Incompatibilities with Previous Release

Derby 10.16 removes support for the deprecated SecurityManager. The Derby 10.16 network server no longer installs a SecurityManager with a default policy file. Derby 10.16 will fail to run under the SecurityManager.

Applications which need to run under a SecurityManager should use an earlier version of Derby, such as 10.15. Those applications will see warnings and will need to set *-Djava.security.manager=allow* when running on JVMs from Open JDK 17 upward.

#### Rationale for Change

The Open JDK team has deprecated the SecurityManager because it is too expensive to maintain. The Open JDK team feels that files and sockets can be protected better by running applications inside operating system containers.

#### Application Changes Required

Derby-powered applications should not rely on the SecurityManager and should no longer expect the Derby network server to install a SecurityManager with a default policy. Instead, applications should remove their own support for the SecurityManager.

The following table suggests defenses against several threats. Some threats have no defenses now. Applications must wait until the Open JDK team builds or recommends replacement defenses for the protections deprecated by JEP 411.

| Operation                             | Threat                                   | Mitigation                           |
| ------------------------------------- | ---------------------------------------- | ------------------------------------ |
| File access                           | PrivacyData corruptionLoading of malware | Containerize application             |
| Network access                        | Loading of malware                       | Containerize application             |
| Access to Derby internals             | PrivacyData corruptionLoading of malware | Run Derby from the module path       |
| Engine and server shutdown            | Denial of service                        | Use authorization to restrict logins |
| Creation of class loaders             | Loading of malware                       | -                                    |
| De-registration of JDBC driver        | Denial of service                        | -                                    |
| Reading and setting system properties | PrivacyDenial of service                 | -                                    |
| JMX monitoring of engine and server   | Harmless                                 | -                                    |
| Re-loading of security policy         | N/A                                      | -                                    |

## Build Environment

Derby release 10.16.1.1 was built using the following environment:

- **Branch** - Source code came from the 10.16 branch.
- **Machine** - Mac OSX 11.2.3.
- **Ant** - Apache Ant(TM) version 1.10.6 compiled on May 2 2019.
- **Compiler** - All classes were compiled by the javac from OpenJDK 64-Bit Server VM (build 17+35-2724, mixed mode, sharing).

## Verifying Releases

It is essential that you verify the integrity of the downloaded files using the PGP and SHA-512 signatures. SHA-512 verification ensures the file was not corrupted during the download process. PGP verification ensures that the file came from a certain person.

The PGP signatures can be verified using [PGP](https://www.pgpi.org/) or [GPG](https://www.gnupg.org/). First download the Apache Derby [KEYS](https://www.apache.org/dist/db/derby/KEYS) as well as the `asc` signature file for the particular distribution. It is important that you get these files from the ultimate trusted source - the main ASF distribution site, rather than from a mirror. Then verify the signatures using ...

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