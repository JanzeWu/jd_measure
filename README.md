周期性JD数据入库
================

要求
====
1. 源文件将会放在 HDFS 的 /Data/crawler_jd 目录下。
2. 入库完成后的文件压缩为 tar.xz 文件，放置 /ArchiveData/jd_source/YYYYMMDD 目录下, YYYYMMDD 为处理完成日期。

解析JD文件格式
==============
需和爬虫约定3种类型的JSON格式，在JSON中以字段 data_type 标识
1. 类型1，数据内容为解析后的JD数据。
2. 类型2，数据内容为 jd_page 的数据，为解析前页面的地址等。
3. 类型3，数据内容为 jd_timeline 的数据，记录的是爬取JD的动作历史。

注意事项 
========
1. 新增字段中 rownum 为行号，类型为 bigint ；random_int 为随机整数，类型为 int 。
2. 拉链表 job_zhineng 及 measure_inc_industry ，确保一个 jd_id 只有一条记录的 end_date IS NULL 。拉链表均采用 A/B 表模式，当前表表名依旧，非当前表表名后添加 "\_\_B"(hive 上使用小写b) 的后缀。jd_timeline 也是 A/B 表模式。
3. 注意 jd_job 的 job_salary_min 及 job_salary_max 的类型应为 decimal(24,3)。
4. jd_id 的含义发生了变化，为 entity_id + content_sign 的 MD5 值。对存量数据，entity_id = jd_src_id 。
5. hdfs 上 /Data/crawler_jd/tmp  目录用于文件上传，上传完毕后需改名后才能被量化处理。
6. 量化程序处理hdfs 上/Data/crawler_jd/ 目录下除tmp外的所有目录。

程序说明
========
1. conf 配置文件目录
2. hql hiveQL模板
3. jd_measure_server java编写的Mapreduce
4. 启动脚本 measure.sh


运行流程
========

筛选未量化入库JD
--------------------
从解析JD中提取entity_id, content_sign 和整个解析JD组成一行，构建表 edw_jd.jd_crawler。
利用edw_jd.jd_entry 筛选出未量化入库的JD，存入表edw_jd.jd_crawler_filter。

量化JD
------
遍历hdfs://ipin-main/Data/crawler_jd 目录，将该目录下的文件夹除tmp外作为输入文件路径，hdfs://ipin-main/Data/crawler_jd/tmp 目录为文件上传目录，避免文件上传过程中被量化处理。
量化结果保存在hdfs://ipin-main/edw/edw_jd_measure 目录下，不同表存放与对应表名的目录。

jd_timeline
-----------
新旧jd_timeline 排序，设置is_first/is_last.


measure_inc_industry 和 job_zhineng
-----------------------------------
同个公司的行业保存最新量化出来的结果，将旧行业的end_date 设置为新行业 start_date 的前一天。
同个JD的职能保存最新量化出来的结果，将旧职能的end_date 设置为新职能 start_date 的前一天。

入库
----
创建外部表jd_entry\_\_b, jd_inc\_\_b, jd_job\_\_b, jd_job_major\_\_b, jd_job_type\_\_b, jd_job_work_city\_\_b, jd_job_work_location\_\_b, jd_others\_\_b, jd_page\_\_b 
location到hdfs://ipin-main/edw/edw_jd_measure/(table_name)/ ，将新量化出来的记录对hive 表中的记录去重后添加到表中。更新load_max_rownum表。


解析JD归档
----------
hdfs://ipin-main/Data/crawler_jd/ 目录下的JD压缩后归档到hdfs://ipin-main/ArchiveData/jd_source/ .

