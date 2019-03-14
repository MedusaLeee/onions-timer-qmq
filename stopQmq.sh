#!/bin/bash

cd ~/tools/qmq-dist-1.1.3.5-bin/bin

./watchdog.sh stop
./delay.sh stop
./broker.sh stop
./metaserver.sh stop