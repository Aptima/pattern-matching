

drop table if exists bitcoin_in;
drop table if exists bitcoin_out;
drop table if exists bitcoin_inout;
drop table if exists bitcoin_inout_edges;

set hive.exec.reducers.max = 24;
set mapred.reduce.tasks=24;

create table bitcoin_in as
select
    count (distinct (source_edge_ID)) as in_degree,
    cast (0 as bigint) as out_degree,
    destination_edge_ID as node,
    sum (amount) as incoming_amount,
    cast (0 as bigint) as outgoing_amount
from bitcoin
group by
destination_edge_ID;

create table bitcoin_out as
select
    cast (0 as bigint) as in_degree,
    count (distinct (destination_edge_ID)) as out_degree,
    source_edge_ID as node,
    cast (0 as bigint) as incoming_amount,
    sum (amount) as outgoing_amount
from bitcoin
group by
source_edge_ID;

create table bitcoin_inout as
select
    sum (COALESCE (in_degree, cast (0 as bigint))) as in_degree,
    sum (COALESCE (out_degree, cast (0 as bigint))) as out_degree,
    node,
    sum (COALESCE (incoming_amount, cast (0 as float))) as incoming_amount,
    sum (COALESCE (outgoing_amount, cast (0 as float))) as outgoing_amount
from (
    select in_degree, out_degree, node,
        incoming_amount, 
        cast (0 as float) as outgoing_amount
    from bitcoin_in
UNION ALL
    select in_degree, out_degree, node,
        cast (0 as float) as incoming_amount,
        outgoing_amount
    from bitcoin_out
) blah
group by
node;

create table bitcoin_inout_edges as
select
    id, 
    source_edge_ID,
    destination_edge_ID, 
    dtg, 
    amount,
    in_degree,
    out_degree,
    node,
    incoming_amount,
    outgoing_amount
from (
    select 
    null as id,
    node as source_edge_ID,
    null as destination_edge_ID,
    null as dtg,
    null as amount,
    in_degree, 
    out_degree, 
    node,
    incoming_amount, 
    outgoing_amount
    from bitcoin_inout
UNION ALL
    select 
    id, 
    source_edge_ID,
    destination_edge_ID, 
    dtg, 
    amount,
    null as in_degree,
    null as out_degree,
    null as node,
    null as incoming_amount,
    null as outgoing_amount
    from bitcoin
) blah
ORDER BY source_edge_ID, in_degree DESC;
