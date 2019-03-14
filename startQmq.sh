#!/bin/bash

cd ~/tools/qmq-dist-1.1.3.5-bin/bin

./metaserver.sh start
./broker.sh start
./delay.sh start
./watchdog.sh start