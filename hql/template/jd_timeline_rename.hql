-- Description: jd_timeline  

alter table <jd-db-name>.jd_timeline rename to <jd-db-name>.jd_timeline__tmp;
alter table <jd-db-name>.jd_timeline__b rename to <jd-db-name>.jd_timeline;
alter table <jd-db-name>.jd_timeline__tmp rename to <jd-db-name>.jd_timeline__b;
