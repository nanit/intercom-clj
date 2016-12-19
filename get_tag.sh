#!/bin/bash
cat project.clj | grep defproject | sed 's/(defproject intercom-clj "\(.*\)"/\1/g'
