#!/bin/bash

for i in `find |grep \/CVS\$`;
do
	rm -rf $i
done;
