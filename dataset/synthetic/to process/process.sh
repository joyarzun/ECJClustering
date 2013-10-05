#!/bin/bash

for file in $(find . -name "*.txt" -type f | awk -F/ '{print $2}'); do

filedata=$(echo $file | sed "s/txt/data/");
touch "data/$filedata";
P=$(echo $file | egrep -o '^[0-9]+');
echo "2,NO,$P" >> "data/$filedata";
cat $file | tr "," "." | tr " " ","  >> "data/$filedata";

done