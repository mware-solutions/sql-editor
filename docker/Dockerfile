FROM registry.cloud.bigconnect.io/service-container-base:latest

ENV SQLEDITOR_VERSION=22.0
ENV JAVA_HOME=${JDK11_HOME}

RUN set -xe \
    && apt-get install -y unzip

COPY ./sql-editor-${SQLEDITOR_VERSION}.zip /opt/
RUN  unzip -d /opt /opt/sql-editor-${SQLEDITOR_VERSION}.zip \
    && mv /opt/sql-editor-${SQLEDITOR_VERSION} /opt/sqleditor \
    && rm -f /opt/*.zip

COPY ./conf/editor.properties /opt/sqleditor/conf/

USER root
COPY ./entrypoint.sh /
RUN chmod a+x /entrypoint.sh

EXPOSE 9888

ENTRYPOINT ["/entrypoint.sh"]
