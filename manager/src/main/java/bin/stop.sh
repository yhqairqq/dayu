#!/usr/bin/env bash
pidfile=dayu.pid
if [ ! -f "$pidfile" ];then
	echo "otter is not running. exists"
	exit
fi

pid=`cat $pidfile`
if [ "$pid" == "" ] ; then
	pid=`get_pid "appName=otter-node"`
fi

echo -e "`hostname`: stopping dayu  $pid ... "
kill $pid
