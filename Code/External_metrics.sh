#!/bin/sh
touch commitsperuser.csv
cd jmeter

echo  "Version, Number of commits, Number of Contributors ,Number of Lines changed,Number of Bugs " >> ../externalmetrics.csv
for branch in docs-3.1 v3_1_RC4 v3_0_RC5 v2_13_RC2 v3_1_RC3 v3_0_RC4 v2_13_RC1 v3_1_RC2 v3_0_RC3 v3_1_RC1 v3_0_RC2 v3_0_RC1 docs-3.0 docs-2.13 docs-2.12 
do 
A=$branch
git checkout $branch
B=$(git rev-list --count $branch)
C=$(git shortlog -s -n | wc -l) 
D=$(git whatchanged  --format=oneline | wc -l)
E=$(git log --pretty=oneline | sed -n '/^[a-z0-9]* Bug /p' | wc -l)
echo  "$A, $B ,$C ,$D, $E" >> ../externalmetrics.csv
done