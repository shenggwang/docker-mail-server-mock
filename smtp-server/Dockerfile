FROM node:13 AS builder

WORKDIR /opt/smtp-local

RUN yarn global add fake-smtp-server

ENTRYPOINT ["fake-smtp-server"]

