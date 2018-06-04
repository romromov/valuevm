# ValueVM
A lightweight and secure container to execute arbitrary guest code on top of JVM.
# System Requirements
OpenJDK 10+, Apache Ant 1.10+

The following java options are **mandatory** for correct work:
-Djava.security.manager
-Djava.security.policy==file:/<path to file>/valuevm.policy
-Djava.security.properties==file:/<path to file>/valuevm.security

# Features
* **Secure** - relies on standard Java Security Framework
* **Deterministic** - guest code is prohibited to influence VM
* **Fast** - add a very small overhead on top of JVM
* **Obfuscated** jars are supported

# Approach
It implements a layered approach to isolation of guest code.

1. Standard Java Security Framework isolates guest code from the rest VM by giving no access permissions to
guest code. Java AccessController sandboxes guest code by controlling access to critical classes and methods.
[Full list of protected resources.](https://docs.oracle.com/javase/7/docs/technotes/guides/security/permissions.html#RuntimePermission)
2. The next isolation level is that it is possible to disable access to any package by one-line change in .security file.
3. Class-granularity access control is enabled on ClassLoader level [TBD]
4. Method level granularity is achieved by rewriting invoke instructions of guest byte code [TBD]
5. *Behavior* of a JDK method can be changed by using the aforementioned bytecode manipulation technique.
Normally method behavior depends on object's state so the object itself would should be altered in this case.

Examples of what kind of access is disabled for guest code can be found in *test_resources* folder.