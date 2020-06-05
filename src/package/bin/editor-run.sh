#!/bin/bash

bin=$(cd "$(dirname $0)"; pwd)
. "${bin}/editor-config.sh"

exec $JAVA_HOME/bin/java $SQLEDITOR_OPTS -cp $CLASSPATH yanagishima.server.YanagishimaServer -conf $SQLEDITOR_CONF_DIR "$@"
