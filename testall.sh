#!/bin/sh
(cd druid-templates/ && mvn clean install)
(cd all-types-sample/ && mvn clean install)
