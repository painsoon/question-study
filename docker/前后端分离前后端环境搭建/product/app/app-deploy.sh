#!/bin/sh

set -e

IMAGE_REPOSITORY="demo"


function build_image {
    docker build -t $IMAGE_REPOSITORY:$1 .
}

docker rm -f ${IMAGE_REPOSITORY}_app || true

#build_image service
build_image app

#upload_image_if_needed service
#upload_image_if_needed app

#docker run -d --name ${IMAGE_REPOSITORY}_service -p 30000:30000  $IMAGE_REPOSITORY:service
docker run -d --name ${IMAGE_REPOSITORY}_app -p 8080:80 $IMAGE_REPOSITORY:app

exit
EOF
