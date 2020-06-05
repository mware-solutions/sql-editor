export IMAGE_VERSION=22.0

docker login registry.cloud.bigconnect.io
docker build -t bigconnect-sqleditor:$IMAGE_VERSION -f Dockerfile .
docker tag bigconnect-sqleditor:$IMAGE_VERSION registry.cloud.bigconnect.io/bigconnect-sqleditor:$IMAGE_VERSION
docker push registry.cloud.bigconnect.io/bigconnect-sqleditor:$IMAGE_VERSION
