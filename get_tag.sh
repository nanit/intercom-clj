#!/bin/bash
cat project.clj | grep defproject | sed 's/(defproject woody "\(.*\)"/\1/g'
