#!/bin/bash
#description: springboot_redis
export LANG=zh_CN.UTF8

PRG_KEYWORD=onlineexam
PRG_HOME=/ainmc/work/onlineexam
PRG=$PRG_HOME/server.sh

JAVA_HOME=/aifs01/work/bigdata/jdk1.8.0_111
LOGBACK="--logging.config=$PRG_HOME/config/logback.xml"
#PRG_RUN_USER=nmcuser
jvmOpt="-Xms512m -Xmx1024m -XX:PermSize=256M -XX:MaxPermSize=512m"
pidFile=$PRG_HOME/server.pid

MAIN_CLASS=$PRG_HOME/onlineexam-0.0.1-SNAPSHOT.jar
cd $PRG_HOME
function start(){
        echo "will start $PRG_KEYWORD"
        nohup "$JAVA_HOME"/bin/java -Djobname="$PRG_KEYWORD" -jar $jvmOpt $MAIN_CLASS $LOGBACK >/dev/null 2>&1&
        #"$JAVA_HOME"/bin/java -Djobname="$PRG_KEYWORD" -jar $MAIN_CLASS
#        for(( i=1;i<=10;i++));do
#                if [ -e $pidFile ]; then
#                        echo "$PRG_KEYWORD is started,pid=`cat $pidFile`"
#                        break
 #               fi
  #              sleep 2
     #   done
}


case "$1" in
start)
        if [ -e $pidFile ]; then
                pid=`cat $pidFile`
                tpid=`ps -ef | grep java | grep -v grep | grep jobname=$PRG_KEYWORD | grep -c " $PRG_RUN_USER "`
                if [ $tpid -lt 1 ]; then
                        start
                else
                        echo "$PRG_KEYWORD already started!! pid="$pid
                fi
        else
                tpid=`ps -ef | grep java | grep -v grep | grep jobname=$PRG_KEYWORD | grep -c " $PRG_RUN_USER "`
                pid=`ps -ef | grep java | grep -v grep | grep jobname=$PRG_KEYWORD | grep " $PRG_RUN_USER " | awk '{print $2}'`
                if [ $tpid -lt 1 ]; then
                        start
                else
                        echo "$PRG_KEYWORD already started!! pid="$pid
                        echo "$pid">server.pid
                fi
        fi
;;

stop)
        if [ -e $pidFile ]; then
                pid=`cat $pidFile`
                echo "will kill $pid"
                kill -9 $pid
                rm $pidFile
                echo "$PRG_KEYWORD already stop!!"
        else
                pid=`ps -ef | grep java | grep -v grep | grep jobname=$PRG_KEYWORD | grep " $PRG_RUN_USER " | awk '{print $2}'`
                echo "will kill $pid"
                ps -ef | grep java | grep -v grep  | grep jobname=$PRG_KEYWORD | grep " $PRG_RUN_USER " | awk '{print $2}' | xargs kill -9
                echo "$PRG_KEYWORD already stop!!"
        fi
;;

restart)
        echo "$PRG_KEYWORD service restart..."
        $PRG stop
        sleep 1
        $PRG start
;;

*)
        echo "Usage: server.sh (start|stop|restart)"
esac
