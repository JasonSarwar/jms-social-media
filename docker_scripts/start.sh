#!/usr/bin/env bash
if [ ! -n "$LOG_PROPERTIES_PATH" ]; then
  LOG_PROPERTIES_PATH="log4j2.xml"
fi

java -cp "/opt/app/lib/*" -Dlog4j.configurationFile=${LOG_PROPERTIES_PATH} com.jms.socialmedia.app.App $PROPERTIES_FILE
