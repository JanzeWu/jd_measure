package com.ipin.jd.server;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by janze on 1/10/18.
 */
public class FileUncompress {

    /**
     * 解压文件，该文件后缀必须与压缩方式对应
     * @param args args[0]: 待解压的文件
     * @throws IOException
     * @throws InterruptedException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws  IOException, InterruptedException, ClassNotFoundException{

        Path uri = new Path(args[0]);
        Configuration conf = new Configuration();
        // conf.set("fs.defaultFS", "hdfs://192.168.1.85");
        // FileSystem fs = FileSystem.newInstance(URI.create("hdfs://192.168.1.85"), conf, "hdfs");
        conf.set("fs.defaultFS", "hdfs://ipin-main");
        conf.set("dfs.nameservices", "ipin-main");
        conf.set("dfs.ha.namenodes.ipin-main", "namenode209,namenode201");
        conf.set("dfs.namenode.rpc-address.ipin-main.namenode209", "dev4:8020");
        conf.set("dfs.namenode.rpc-address.ipin-main.namenode201", "dev5:8020");
        conf.set("dfs.client.failover.proxy.provider.ipin-main", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        FileSystem fs = FileSystem.get( URI.create("hdfs://ipin-main"), conf, "hdfs");
        
        CompressionCodecFactory factory = new CompressionCodecFactory(conf);
        CompressionCodec codec = factory.getCodec(uri);
        String destPath = CompressionCodecFactory.removeSuffix(args[0], codec.getDefaultExtension());

        FSDataInputStream fsDataInputStream = fs.open(uri);
        FSDataOutputStream fsDataOutputStream = fs.create(new Path(destPath));

        CompressionInputStream compressionInputStream = codec.createInputStream(fsDataInputStream);
        IOUtils.copyBytes(compressionInputStream, fsDataOutputStream, conf);
        IOUtils.closeStream(fsDataOutputStream);
        IOUtils.closeStream(compressionInputStream);
        fs.close();
    }

}
