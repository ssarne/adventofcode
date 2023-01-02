#!/bin/bash
if [ "$1" == ""] ; then
    echo "Supply year as first arg, e.g.:  aoc2017"
fi

mkdir input
mkdir input/$1

for i in {1..9} ; do
   touch input/$1/dec0$i.out
done

for i in {10..25} ; do
   touch input/$1/dec$i.out
done
