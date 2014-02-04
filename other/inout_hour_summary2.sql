SET mapred.map.tasks.speculative.execution=false;
SET mapred.reduce.tasks.speculative.execution=false;
set mapred.min.split.input.size=2550000000;
set mapred.min.split.size=2550000000;


drop table d3_in_hourly;
create table d3_in_hourly as
select
    count (distinct (src_ip)) as in_degree,
    cast (0 as bigint) as out_degree,
    start_date,
    start_time,
    dst_ip as ip,
    sum (total_packets) as incoming_packets,
    sum (total_bytes) as incoming_bytes,
    cast (0 as bigint) as outgoing_packets,
    cast (0 as bigint) as outgoing_bytes
from d3_hourly
group by
    start_date, start_time, dst_ip;



drop table d3_out_hourly;
create table d3_out_hourly as
select
    cast (0 as bigint) as in_degree,
    count (distinct (dst_ip)) as out_degree,
    start_date,
    start_time,
    src_ip as ip,
    cast (0 as bigint) as incoming_packets,
    cast (0 as bigint) as incoming_bytes,
    sum (total_packets) as outgoing_packets,
    sum (total_bytes) as outgoing_bytes
from d3_hourly
group by 
    start_date, start_time, src_ip;



drop table d3_inout_hourly;
create table d3_inout_hourly as
select
    sum (COALESCE (in_degree, cast (0 as bigint))) as in_degree,
    sum (COALESCE (out_degree, cast (0 as bigint))) as out_degree,
    start_date,
    start_time,
    ip,
    sum (COALESCE (incoming_packets ,cast (0 as double))) as incoming_packets,
    sum (COALESCE (incoming_bytes ,  cast (0 as double))) as incoming_bytes,
    sum (COALESCE (outgoing_packets ,cast (0 as double))) as outgoing_packets,
    sum (COALESCE (outgoing_bytes,   cast (0 as double))) as outgoing_bytes
from (
    select in_degree, out_degree, start_date, start_time, ip, 
        incoming_packets, incoming_bytes, 
        cast (0 as double) as outgoing_packets,
        cast (0 as double) as outgoing_bytes
    from d3_in_hourly
UNION ALL
    select in_degree, out_degree, start_date, start_time, ip,
        cast (0 as double) as incoming_packets,
        cast (0 as double) as incoming_bytes,
        outgoing_packets,
        outgoing_bytes
    from d3_out_hourly
) blah
group by
start_date, start_time, ip
;
