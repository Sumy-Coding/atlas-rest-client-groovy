#!/bin/bash

function getData() {
    res=$(curl "http://localhost:7141/rest/api/content")
    echo $res > res.txt
}

echo $(getData)