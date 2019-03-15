#!/bin/bash

cd ~/tools/qmq-dist-1.1.3.5-bin/bin

./metaserver.sh start
sleep 2s
./broker.sh start
sleep 1s
./delay.sh start
sleep 1s
./watchdog.sh start