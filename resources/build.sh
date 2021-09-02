#!/bin/bash

echo "Build Hide-and-seek platform"
version="0.1"

if [ ! -f "pom.xml" ]; then
    cd ..
fi

if [ -d "./build" ]; then
    rm -rf ./build
fi

mkdir "build"

mvn clean package


mv "./target/libs" "./build/libs"
mv "./hide-and-seek-agents/target/hide-and-seek-agents-$version.jar" "./build/libs/hide-and-seek-agents-$version.jar"
mv "./hide-and-seek-environment/target/hide-and-seek-environment-$version.jar" "./build/libs/hide-and-seek-environment-$version.jar"
mv "./hide-and-seek-simulation/target/hide-and-seek-simulation-$version.jar" "./build/libs/hide-and-seek-simulation-$version.jar"

cp "./resources/run.sh" "./build/run.sh"
cp "./resources/config.json" "./build/config.json"

rm -rf "target"