# Google-Search-Auto-Complete

## Overview
In this project, I implemented google search auto complete based on N-Gram Model using Hadoop MapReduce in Java.

## Step

* Build N-Gram Library from input.
* Build Language Model based on N-Gram Library and probability.
* Load the data of Language Model into MySQL.
* Utilize JQuery, Ajax, Spring Boot, Spring JDBC to build auto-complete in web demo.

## Demo

Here is how my auto-complete looks like.

![](images/demo.png)

## Arguments

* args0: input path
* args1: output path
* args2: ngram size
* args3: threshold size, it would be ignored if the count of the word's occurrence smaller than threshold. 
* args4: how many following words need be picked

