#!/bin/sh

className=MarketGoApplication

pid=`ps -ef|grep $className|awk '$8 ~ /java/ {print $2}'`


if [ "$pid" != "" ]
then
        kill -9 $pid
        echo "kill pid "$pid
else

        echo "pid: not found"

fi