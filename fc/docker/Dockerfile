# syntax=docker/dockerfile:1
ARG TOMCAT_IMAGE=tomcat:9.0.122-jdk8-temurin-noble@sha256:79afc21f937ab4d1fdb385459c3551a68c409090b8c8b887c1a8ea4af3daaef0
FROM ${TOMCAT_IMAGE} AS builder
ARG GRAILS_VERSION=2.5.6
ARG GRAILS_SHA256=58c1283d7748449bc3cecf8c9fe883157f2a279fa75ff14aa6d6388461527187
RUN apt-get update && apt-get install -y --no-install-recommends curl unzip python3 \
    && rm -rf /var/lib/apt/lists/*
RUN curl -fsSL --retry 3 "https://github.com/apache/grails-core/releases/download/v${GRAILS_VERSION}/grails-${GRAILS_VERSION}.zip" -o /tmp/grails.zip \
    && echo "${GRAILS_SHA256}  /tmp/grails.zip" | sha256sum -c - \
    && unzip -q /tmp/grails.zip -d /opt && rm /tmp/grails.zip
ENV GRAILS_HOME=/opt/grails-2.5.6
ENV PATH="${GRAILS_HOME}/bin:${PATH}"
ENV GRAILS_OPTS="-Xms256m -Xmx2048m -Dfile.encoding=UTF-8"
WORKDIR /build/fc
COPY . .
RUN python3 docker/prepare-source.py
RUN --mount=type=cache,target=/root/.grails \
    --mount=type=cache,target=/root/.m2 \
    --mount=type=cache,target=/build/fc/dependency-cache \
    grails -non-interactive prod war /build/fc/output/fc.war

FROM ${TOMCAT_IMAGE} AS runtime
RUN apt-get update && apt-get install -y --no-install-recommends curl python3 \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --gid 10001 flightcontest \
    && useradd --uid 10001 --gid flightcontest --home-dir /var/lib/flightcontest flightcontest
COPY --from=builder /build/fc/output/fc.war /tmp/fc.war
RUN mkdir -p webapps/fc /var/lib/flightcontest/db /var/lib/flightcontest/save/.fc /var/lib/flightcontest/work \
    && cd webapps/fc && jar -xf /tmp/fc.war && rm /tmp/fc.war \
    && mkdir -p gpxupload jobs/done jobs/error map live live2 \
    && chown -R flightcontest:flightcontest /usr/local/tomcat /var/lib/flightcontest
COPY --chmod=755 docker/entrypoint.sh /usr/local/bin/flightcontest-entrypoint
COPY docker/scripts/restore-volume.py /usr/local/lib/flightcontest/restore-volume.py
ENV FC_SAVE_DIR=/var/lib/flightcontest/save \
    FC_DB_PATH=/var/lib/flightcontest/db/fcdb \
    FC_INTERNAL_BASE_URL=http://127.0.0.1:8080/fc \
    FC_MAP_MODE=disabled \
    FC_REMOTE_CONFIG=false \
    CATALINA_OPTS="-Xms256m -Xmx2048m -Dfile.encoding=UTF-8 -Djava.awt.headless=true -Duser.language=de -Duser.country=DE"
USER flightcontest
WORKDIR /var/lib/flightcontest/work
EXPOSE 8080
HEALTHCHECK --interval=15s --timeout=5s --start-period=120s --retries=4 \
    CMD curl -fsS http://127.0.0.1:8080/fc/health/ready || exit 1
ENTRYPOINT ["/usr/local/bin/flightcontest-entrypoint"]
CMD ["catalina.sh", "run"]
