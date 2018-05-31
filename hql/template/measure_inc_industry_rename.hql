-- A表改B表，B表改A表
alter table <jd-db-name>.measure_inc_industry      rename to <jd-db-name>.measure_inc_industry__tmp;
alter table <jd-db-name>.measure_inc_industry__b   rename to <jd-db-name>.measure_inc_industry;
alter table <jd-db-name>.measure_inc_industry__tmp rename to <jd-db-name>.measure_inc_industry__b;

