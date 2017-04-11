#!/usr/bin/env bash

function create_stack {
    echo "======= Creating stack."

    echo "======= Uploading app.zip to s3."
    aws s3 cp build/distributions/app.zip s3://aws-osm-app/app.zip
    echo "======= Creating stack using template."
    aws cloudformation create-stack --stack-name aws-osm-stack --template-body file://src/main/resources/CF_trancaction_lambda.json
    echo "======= Stack created"
}

function update_stack {
    echo "======= Updating stack."

    echo "======= Uploading app.zip to s3."
    aws s3 cp build/distributions/app.zip s3://aws-osm-app/app.zip
    echo "======= Updating stack using template."
    aws cloudformation deploy --stack-name aws-osm-stack --template-file src/main/resources/CF_trancaction_lambda.json
    echo "======= Stack updated"
}

echo "======= Cleaning project."
./gradlew clean
echo "======= Rebuilding"
./gradlew buildAppZip

if [ ! -f build/distributions/app.zip ]
then
    echo "======= app.zip not found. Can't deploy stack."
    return 1
fi

echo "======= Project rebuilt."
echo "======= Checking is stack exists."
aws cloudformation describe-stacks --stack-name aws-osm-stack

if [ $? -eq 0 ]
then
    echo "======= Stack exist."
    update_stack
else
    echo "======= Stack not exist."
    create_stack
fi
