#!/usr/bin/env bash
if [ -z "$LOG_PROPERTIES_PATH" ]; then
  LOG_PROPERTIES_PATH="log4j2.xml"
fi

java -cp "lib/*" -Duser.timezone=America/New_York -Dlog4j.configurationFile=${LOG_PROPERTIES_PATH} com.jms.socialmedia.app.App $PROPERTIES_FILE |& tee -a logs/console.log