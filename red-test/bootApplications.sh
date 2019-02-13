#!/usr/bin/env bash

echo '============================================'
echo '======    STARTING PRODUCER    ======'
echo '============================================'
cd ../demo-producer && ./gradlew bootRun > producer.log &
pwd
echo '============================================'
echo '======    STARTING CONSUMER    ======'
echo '============================================'
cd ../demo-consumer && ./gradlew bootRun > consumer.log &


status_code=$(curl -s -o /dev/null -w "%{http_code}" -X POST -d "sample message" http://localhost:1234/makeMessage)
i=0
while [[ "$status_code" -ne "200" ]] && [[ "$i" -lt 20 ]]; do
    sleep 1
    status_code=$(curl -s -o /dev/null -w "%{http_code}" -X POST -d "sample message" http://localhost:1234/makeMessage)
    i=$[$i+1]
    echo "...Waiting for Producer, try $i"
done

if [[ "$status_code" -eq "200" ]]; then
    echo '         PRODUCER started'
fi

status_code=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:4321/getMessages)
i=0
while [[ "$status_code" -ne "200" ]] && [[ "$i" -lt 20 ]]; do
    sleep 1
    status_code=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:4321/getMessages)
    i=$[$i+1]
    echo "...Waiting for Consumer, try $i"
done

if [[ "$status_code" -eq "200" ]]; then
    echo '         CONSUMER started'
fi


echo '============================================'
echo '======    RUNNING TESTS    ======'
echo '============================================'
./gradlew clean test

exit_value=$?

echo '============================================'
echo '======    Tearing down servers...    ======='
echo '============================================'

pkill java

exit $exit_value