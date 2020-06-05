# Requirements to build

* Java 11
* Node.js

## Quick Start
```
git clone https://github.com/mware-solutions/sql-editor.git
cd sql-editor
git checkout -b [version] refs/tags/[version]
./gradlew distZip
cd build/distributions
unzip sql-editor-[version].zip
cd sql-editor-[version]
vim conf/editor.properties
nohup bin/editor-start.sh >y.log 2>&1 &
```
see http://localhost:8080/

# Stop
```
bin/editor-shutdown.sh
```
