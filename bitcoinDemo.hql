    		   

add file PM-0.0.1-SNAPSHOT.jar
add file hive_contrib.jar;

set mapred.reduce.tasks=150;

FROM (
	FROM bitcoin_inout_edges
	MAP id, source_edge_ID, destination_edge_ID, dtg, amount, in_degree, out_degree, node, incoming_amount, outgoing_amount
	USING 'java -cp PM-0.0.1-SNAPSHOT.jar:hive_contrib.jar com.aptima.netstorm.algorithms.aptima.bp.hive.HiveMapScript'
	AS isLink, srcID, destID, mismatchVector, timeWindow, amount
	DISTRIBUTE BY timeWindow
	SORT BY timeWindow ASC, isLink ASC) temp
INSERT OVERWRITE TABLE aptima_pm_result
	REDUCE temp.timeWindow, temp.isLink, temp.srcID, temp.destID, temp.mismatchVector, temp.amount
	USING 'java -cp PM-0.0.1-SNAPSHOT.jar:hive_contrib.jar com.aptima.netstorm.algorithms.aptima.bp.hive.HiveReduceScript'
	AS result_num, modelID, dataID, mismatch, dir;
