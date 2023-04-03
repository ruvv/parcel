#!/bin/sh

export SHELL=/bin/bash

cd ../user-api || exit
$SHELL mvnw clean install

cd ../parcel-api || exit
$SHELL mvnw clean install

cd ../messaging || exit
$SHELL mvnw clean install

cd ../discovery || exit
$SHELL mvnw clean package

cd ../gateway || exit
$SHELL mvnw clean package

cd ../parcel-service || exit
$SHELL mvnw clean package

cd ../user-service || exit
$SHELL mvnw clean package