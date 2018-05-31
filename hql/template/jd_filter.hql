set hive.execution.engine=mr;
set hive.auto.convert.join=false;

create table if not exists <jd-db-name>.jd_entry(
    jd_id                   string        comment   'jd的id，“jd_src_id+content_sign”的MD5值',
    entity_id               string        comment   'JD实体id，逻辑上唯一标识渠道内的一个JD。',
    jd_src_id               string        comment   'JD来源id，和原始jd的URL相关',
    content_sign            string        comment   '验签值，标识JD内容是否变化',
    jd_from_id              int           comment   'JD来源渠道id',
    jd_content_pub_timestamp    bigint    comment   'JD版本表动的更新/发布日期，Unix Timestamp格式',
    jd_content_pub_date     string        comment   'JD版本表动的更新/发布日期，日期格式',
    jd_src_url              string        comment   'JD来源URL',
    inc_seg_id              string        comment   '裸公司id，为裸公司名+ipin.com的MD5值',
    status                  int           comment   '状态：0-生效中,1-下架/过期,2-历史版本',
    src                     string        comment   '系统来源',
    rownum                  bigint        comment   '行号',
    random_int              int           comment   '随机整数',
    etl_date                string        comment   'ETL日期');

create external table if not exists <jd-db-name>.jd_crawler(
    entity_id string,
    content_sign string,
    jd_json string)
location '<jd-crawler-output>';


drop table if exists <jd-db-name>.jd_crawler_filter ;
create table <jd-db-name>.jd_crawler_filter as 
    select
        jd_json
    from
        <jd-db-name>.jd_crawler jd
    left join (
        select
            entity_id,
            content_sign
        from (
            select
                coalesce(entity_id, jd_src_id)  
                    as entity_id,
                content_sign
            from 
                <jd-db-name>.jd_entry
        ) t 
        group by 
            entity_id, content_sign
    ) entry
    on
        jd.entity_id = entry.entity_id
        and jd.content_sign = entry.content_sign
    where
        entry.entity_id is null
        and entry.content_sign is null;
