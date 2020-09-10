FROM dockergroup.vibrenthealth.com/vibrent-base/java-11-jre:master

ENV JAVA_OPTS=""
ENV HOME=/opt/empattendance
ENV LOG_DIR=/var/log/empattendance
ENV APPLICATION=empattendance
ENV VERSION=0.0.1

#
# Stage 1 - run all privileged commands

# Create our working directory
RUN mkdir -p ${HOME}
WORKDIR ${HOME}

#Copy jar/war to $HOME
COPY target/${APPLICATION}-${VERSION}*.jar ${HOME}/app.jar

#
# Stage 2 - create, switch and run as a restricted user

# Set up the user we will use for security reasons (don't run as root)
RUN adduser -D empattendance-user && \
	chown -R empattendance-user ${HOME} && \
	mkdir -p ${LOG_DIR} && \
	chown -R empattendance-user ${LOG_DIR}
USER empattendance-user

EXPOSE 8080 9010 5005

CMD java ${JAVA_OPTS} -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.net.preferIPv4Stack=true -Djava.security.egd=file:/dev/./urandom -jar app.jar
