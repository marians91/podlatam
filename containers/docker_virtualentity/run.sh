#!/bin/sh

i=1
sleep=2
while [ $i -le $AWS_TEST_CONNECTION_MAX_RETRIES ]
do
    curl --connect-timeout 0.5 -s https://sts.eu-central-1.amazonaws.com/
    if [ $? -ne 0 ]
    then
        echo "Try $i of $AWS_TEST_CONNECTION_MAX_RETRIES: Error could not connect to sts. Wait $sleep seconds for next."
        i=$((i+1))
        sleep $sleep
    else
        echo "Try $i of $AWS_TEST_CONNECTION_MAX_RETRIES: sts connected"
        exit 0
    fi
done
echo "Error could not connect to sts. Aborting execution..."
exit 1
