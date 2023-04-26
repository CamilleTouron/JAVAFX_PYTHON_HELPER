#!/bin/bash
mvn clean install

# Execute the jar file with the specified version of Java
compile exec:java -Dexec.mainClass=pyhtonhelper.Main
