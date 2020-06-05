#!/bin/bash

bin=$(cd "$(dirname $0)"; pwd)
SQLEDITOR_HOME="${bin}/.."

if [ -z "$YANAGISHIMA_CONF_DIR" ]; then
  SQLEDITOR_CONF_DIR=$SQLEDITOR_HOME/conf
fi

for file in "$SQLEDITOR_HOME"/lib/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done

if [ -z "$SQLEDITOR_OPTS" ]; then
  SQLEDITOR_OPTS=-Xmx2G
fi
