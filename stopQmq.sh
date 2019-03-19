#!/bin/bash

cd ~/tools/qmq-dist-1.1.3.5-bin/bin

./watchdog.sh stop
sleep 1s
./delay.sh stop
sleep 1s
./broker.sh stop
sleep 1s
./metaserver.sh stop

brew services stop rabbitmq