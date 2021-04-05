#!/bin/sh

set -e

IMAGE_REPOSITORY="demo"
SERVICE_NAME="demo-service"


docker rm -f ${SERVICE_NAME} || true
docker rmi ${IMAGE_REPOSITORY}:$1 || true

docker build -t ${IMAGE_REPOSITORY}:$1 .


#docker pull ${IMAGE_REPOSITORY}:$1

docker run -d --name ${SERVICE_NAME} -p 33000:30000 ${IMAGE_REPOSITORY}:$1

exit
EOF
