FROM openjdk:8-jdk-alpine

RUN apk update
RUN apk add --no-cache curl nginx 
#openrc

RUN adduser -D -g 'www' www \
	&& mkdir /www \
	&& chown -R www:www /var/lib/nginx \
	&& chown -R www:www /www \
	&& mv /etc/nginx/nginx.conf /etc/nginx/nginx.conf.orig

RUN curl -o /dist.tar https://s3-us-west-2.amazonaws.com/jzpro/dist.tar \
	&& tar -xf dist.tar -C /www \
	&& chown -R www:www /www
#RUN rc-update add nginx default

COPY server/target/server-0.0.1-SNAPSHOT.jar /app.jar
COPY docker/entrypoint.sh /entrypoint.sh
COPY docker/nginx.conf /etc/nginx/nginx.conf

RUN chmod 700 /entrypoint.sh \
  && mkdir /mylog

EXPOSE 80

ENTRYPOINT ["/bin/sh", "/entrypoint.sh"]
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=dev","-jar","/app.jar"]

