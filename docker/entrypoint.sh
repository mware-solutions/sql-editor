#!/bin/bash

sed -i 's@DISCOVERY_URI@'dsql-$TENANT_ID.cloud.bigconnect.io'@g' /opt/sqleditor/conf/editor.properties

/opt/sqleditor/bin/editor-run.sh
