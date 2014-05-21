    		   

add file PM-0.0.1-SNAPSHOT.jar
add file hive_contrib.jar;

set mapred.reduce.tasks=150;

FROM (
	FROM a_s_test 
	MAP src, dest, region, unixtime, latency
	USING 'java -cp PM-0.0.1-SNAPSHOT.jar:hive_contrib.jar com.aptima.netstorm.algorithms.aptima.bp.hive.AkamaiMapScript'
	AS isLink, srcID, destID, mismatchVector, timeWindow
	DISTRIBUTE BY timeWindow
	SORT BY timeWindow ASC, isLink ASC) temp
INSERT OVERWRITE TABLE aptima_pm_result
	REDUCE temp.timeWindow, temp.isLink, temp.srcID, temp.destID, temp.mismatchVector
	USING 'java -cp PM-0.0.1-SNAPSHOT.jar:hive_contrib.jar com.aptima.netstorm.algorithms.aptima.bp.hive.AkamaiReduceScript'
	AS result_num, modelID, dataID, mismatch, dir;
