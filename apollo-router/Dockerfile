FROM ghcr.io/apollographql/router:v1.45.1
USER root
RUN apt update && apt install -y ca-certificates wget && rm -rf /var/lib/apt/lists/* && apt-get clean
USER router