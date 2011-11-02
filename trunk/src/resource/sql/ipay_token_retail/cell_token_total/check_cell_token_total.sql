
-- sum from cell_token_total
select 
bought_by_retailer_id,
min(date_sold_by_us) as min_date_sold_by_us, 
max(date_sold_by_us) as max_date_sold_by_us,
sum(count_) as count_,
cast(sum(our_sale_amount) as numeric(10, 2))
from qamps_total.cell_token_total
where 
where date_sold_by_us >= $begin_date and date_sold_by_us < $end_date
;



select 
count(distinct(bought_by_retailer_id)),
count(distinct(date_trunc('day', date_sold_by_us))),
sum(count_) as count_, 
sum(our_sale_amount)::integer as our_sale_amount, 
min(min_date_sold_by_us) as min_date_sold_by_us, 
max(max_date_sold_by_us) as max_date_sold_by_us
from qamps_total.cell_token_total 
where date_sold_by_us >= '2011-01-01' and date_sold_by_us < '2011-02-01' 
and count_ > 0
;

select 
count(distinct(bought_by_retailer_id)),
count(distinct(date_trunc('day', date_sold_by_us))),
count(1) as count_, 
sum(our_sale_amount)::integer ,
min(date_sold_by_us) as min_date_sold_by_us, 
max(date_sold_by_us) as max_date_sold_by_us
from qamps.cell_token_archive 
where date_sold_by_us >= '2011-01-01' and date_sold_by_us < '2011-02-01' 
;

-- by day

select date_trunc('day', date_sold_by_us), 
sum(count_), 
min(date_sold_by_us), max(date_sold_by_us)
from qamps_total.cell_token_total 
where date_sold_by_us >= '2011-01-01' and date_sold_by_us < '2011-02-01' 
group by date_trunc('day', date_sold_by_us)
order by date_trunc('day', date_sold_by_us)
;

select date_trunc('day', date_sold_by_us), 
count(1), 
min(date_sold_by_us), max(date_sold_by_us)
from qamps.cell_token_archive 
where date_sold_by_us >= '2011-01-01' and date_sold_by_us < '2011-02-01' 
group by date_trunc('day', date_sold_by_us)
order by date_trunc('day', date_sold_by_us)
;

-- by token 

select cell_token_type_id,
sum(count_), 
min(date_sold_by_us), max(date_sold_by_us)
from qamps_total.cell_token_total 
where date_sold_by_us >= '2011-03-01' and date_sold_by_us < '2011-04-01' 
group by cell_token_type_id
order by cell_token_type_id
;

select cell_token_type_id, 
count(1), 
min(date_sold_by_us), max(date_sold_by_us)
from qamps.cell_token_archive 
where date_sold_by_us >= '2011-03-01' and date_sold_by_us < '2011-04-01' 
and bought_by_retailer_id in (
  select retailer_id from retailer where date_trunc('month', date_registered) <= date_sold_by_us and enabled
)
group by cell_token_type_id
order by cell_token_type_id
;


-- 

select 
date_trunc('month', date_sold_by_us),
count(1), sum(our_sale_amount)::integer 
from ONLY qamps.cell_token
where date_sold_by_us >= '2011-03-01' and date_sold_by_us < '2011-04-01' 
and not is_test 
group by date_trunc('month', date_sold_by_us)
order by date_trunc('month', date_sold_by_us)
;

select 
date_trunc('month', date_sold_by_us),
sum(count_), 
min(date_sold_by_us)::date, 
max(date_sold_by_us)::date, 
min(date_created)::timestamp, 
max(date_created)::timestamp
from qamps_total.cell_token_total 
group by date_trunc('month', date_sold_by_us)
order by date_trunc('month', date_sold_by_us)
;




-- report 
select 
bought_by_retailer_id,
min(date_sold_by_us) as min_date_sold_by_us, 
max(date_sold_by_us) as max_date_sold_by_us,
count(1) as count_,
cast(sum(our_sale_amount) as numeric(10, 2))
from qamps.cell_token_archive ct
where 
where date_sold_by_us >= $begin_date and date_sold_by_us < $end_date
and date_trunc('day', date_sold_by_us) > (
  select max(date_sold_by_us)
  from cell_token_total
  where bought_by_retailer = $retailer_id
)
;
