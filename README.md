pattern-matching
================

Hadoop MapReduce over Hive based implementation of attributed network pattern matching. 

#Prequisites

* Maven
* Java 1.6 or higher

#Building

mvn clean package 

#Datasets

This release targets the ability to run over Hive datasets from XDATA. The two datasets currently supported include the Akamai data and Bitcoin data. Future releases are focusing on documentation and extensibility to support additional datasets by allowing flexible column/dataset specification, updated mismatch functions, pattern authoring.

#Running

After building run against either dataset by using the appropriate Hive script:

```
hive -f akamaiDemo.hql
hive -f bitcoinDemo.hql
```

#Analysis and Results
Results are written to an output Hive table: aptima_pm_result. Additional documentation is forthcoming describing this format.

#Algorithm and Processing Techniques
More to come soon - ETA mid Feburary 2014.






