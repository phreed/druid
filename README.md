File Structure
============

This is a code generator.
It should typically be run using maven.
Given a data contract the generator creates
as set of android source files.
The druid-maven-plugin contains the source code for the
maven plugin which can be built and installed using 'mvn clean install'.
In order for the maven plugin to do anything useful it must 
have access to a set of templates.

Presently there is no maven archetype but there are
two projects provided as samples.

  * location-sample consists of a pom.xml and a data contract, pli.xml.
  * all-types-sample is similar to location-sample but its contract
     demonstrates all of the defined datatypes.

This test result can be built and run from
that directory with the following:
  mvn clean install android:deploy android:run


