#!/usr/bin/env bash
if [ -z "$LOG_PROPERTIES_PATH" ]; then
  LOG_PROPERTIES_PATH="log4j2.xml"
fi

JAVA_OPT="-server -Duser.timezone=America/New_York -Djava.net.preferIPv4Stack=true -Dlog4j.configurationFile=${LOG_PROPERTIES_PATH}"

java -cp "lib/*" $JAVA_OPT  com.jms.socialmedia.app.App $PROPERTIES_FILE |& tee -a logs/console.log
