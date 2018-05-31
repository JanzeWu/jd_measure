-- Description: job_zhineng A B 表改名

alter table <jd-db-name>.job_zhineng rename to <jd-db-name>.job_zhineng_tmp;
alter table <jd-db-name>.job_zhineng__B rename to <jd-db-name>.job_zhineng;
alter table <jd-db-name>.job_zhineng_tmp rename to <jd-db-name>.job_zhineng__B;

