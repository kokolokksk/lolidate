#!/bin/bash

AppName="lolidate-0.0.1"



    echo "Stop $AppName"

    PID=""
    query(){
        PID=`ps -ef |grep $AppName | grep -v grep|awk '{print $2}'`
    }

    query
    if [ x"$PID" != x"" ]; then
        kill -TERM $PID
        echo "$AppName (pid:$PID) exiting..."
        while [ x"$PID" != x"" ]
        do
            sleep 1
        query
        done
        echo "$AppName exited."
        else
         echo "$AppName already stopped."
    fi
