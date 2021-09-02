#!/bin/bash

echo "Run simulation"

classpath=$(echo libs/*.jar | tr ' ' ':')

if [ -f "run.log" ]; then
    rm -rf run.log
fi

config="./config.json"

if [ -n "$1" ]; then
    config="$1"
fi

java -classpath $classpath ac.kcl.inf.has.simulation.Simulation $config >> run.log 2>&1