#!/usr/bin/env bash

function update_function {
    echo "======= Updating function."

    echo "======= Uploading app.zip to s3."
    aws s3 cp build/distributions/app.zip s3://aws-osm-app/app.zip
    echo "======= Updating function code."
    aws lambda update-function-code --function-name downloadUrlToS3Lambda --s3-bucket aws-osm-app --s3-key app.zip
    echo "======= Function updated"
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

echo "======= Checking is function exists."
aws lambda get-function --function-name downloadUrlToS3Lambda

if [ $? -eq 0 ]
then
    echo "======= Function exist."
    update_function
else
    echo "======= Error, function not exist."
    return 2
fi
