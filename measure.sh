#!/bin/bash
ROOT_DIR=$(cd "$(dirname $0)"; pwd)
HQL_RUNNING=${ROOT_DIR}/hql/running

cd $ROOT_DIR/jd_measure_server
mvn package -Dmaven.test.skip=true

cd $ROOT_DIR
sudo mkdir -p ${HQL_RUNNING}
sudo chmod -R 777 ${HQL_RUNNING}
sudo su - hdfs -c "${ROOT_DIR}/ENV/bin/python ${ROOT_DIR}/hdfs_client.py"

