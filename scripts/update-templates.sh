#!/bin/bash

while read source target; 
do
  meld ${source} ${target}
done <<EOF
EOF

