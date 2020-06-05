#!/bin/bash
bin=$(cd "$(dirname $0)"; pwd)
. "${bin}/editor-config.sh"

proc=$(cat "$SQLEDITOR_HOME"/currentpid)
echo "killing SQL Editor"
kill $proc

cat /dev/null > "$SQLEDITOR_HOME"/currentpid
