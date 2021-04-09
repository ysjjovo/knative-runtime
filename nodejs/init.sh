#!/bin/bash
#/bin/bash init.sh eyJ0eXBlIjoianNvbiIsImhlbGxvIjoid29ybGQifQ==
if [ $# -eq 0 ];then
 node index.js
else
 node test.js $@
fi