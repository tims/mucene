Project skeleton - base your new java projects on this one

Once you have unpacked this skeleton, do the following:

1. Rename the folder from project-skeleton to your project name.

2. Setup files needed by Ant to do a build:

- Copy build.properties.example to build.properties and configure as appropriate for your environment 
- Copy test/conf/test.properties.example to test/conf/test.properties and configure as appropriate for your test environment
- Edit ivy.xml and set artifact and module name and add any dependencies.
- grep for the string "project-skeleton" in all files and replace with your project name 
- Run "ant all" (no errors should be reported)

3. Set up Paths in your IDE:

- Add src/java to the CLASSPATH of your IDE (as a Java source path). 
- Add test/java to the CLASSPATH of your IDE (as a Java source path). 
- Add src/conf to the CLASSPATH of your IDE (as a folder).
- Add test/conf to the CLASSPATH of your IDE (as a folder).
- Add test/data to the CLASSPATH of your IDE (as a folder).
- Add lib/log4j.jar to the CLASSPATH of your IDE 
- Add lib/commons-logging.jar to the CLASSPATH of your IDE
- Other jars not necessary for compilation, only for build
- Optionally, set your IDE up to compile its classes to a folder under build (e.g. build/eclipse-classes)
- TODO Optionally, setup Eclipse to use codeStyle.xml to format code (quick howto)

4. Before committing to Subversion, add the following files and folders to svn:ignore

- build
- build.properties
- test/conf/test.properties
- lib/*.jar

Eclipse users should also add the following to svn:ignore
- .settings
- .classpath
- .project

If you use a different IDE and it creates its own configuration files in the project root, add these to 
svn:ignore too.

5. You can now add your own classes, overwrite/add targets to build.xml etc.

6. Once you have committed the project to Subversion you can also add it to Hudson. To get started create a new project
 and base it on a copy of the project-skeleton project already there. Configure and replace with your project name 
 where appropriate (wherever you see "project-skeleton" in any of the input fields).
