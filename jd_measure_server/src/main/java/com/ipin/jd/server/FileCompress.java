package com.ipin.jd.server;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by janze on 1/10/18.
 */
public class FileCompress {


    private Configuration conf;
    private FileSystem fs;
    private String archiveDir;

    public static final String defaultOutPath = "/ArchiveData/jd_source";

    public FileCompress() throws InterruptedException, IOException, ClassNotFoundException{
        this.conf = new Configuration();
//        conf.set("fs.defaultFS", "hdfs://192.168.1.85");
//
//        this.fs = FileSystem.newInstance(URI.create("hdfs://192.168.1.85"), conf, "hdfs");
        conf.set("fs.defaultFS", "hdfs://ipin-main");
        conf.set("dfs.nameservices", "ipin-main");
        conf.set("dfs.ha.namenodes.ipin-main", "namenode209,namenode201");
        conf.set("dfs.namenode.rpc-address.ipin-main.namenode209", "dev4:8020");
        conf.set("dfs.namenode.rpc-address.ipin-main.namenode201", "dev5:8020");
        conf.set("dfs.client.failover.proxy.provider.ipin-main", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        this.fs = FileSystem.get( URI.create("hdfs://ipin-main"), conf, "hdfs");
    }

    private void setup(String outPath, Path srcPath) {

        this.archiveDir = outPath + Path.SEPARATOR + srcPath.getName();
    }

    /**
     * 默认放在 hdfs://ipin-main/ArchiveData/jd_source/ 路径下
     * @param args args[0]: 等待压缩的原始文件（或者分别对目录下的文件压缩）
     * @throws IOException
     * @throws InterruptedException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws  IOException, InterruptedException, ClassNotFoundException{

        Path srcPath = new Path(args[0]);
        FileCompress fileCompress = new FileCompress();
        fileCompress.setup(args.length > 1 && fileCompress.fs.exists(new Path(args[1])) ? args[1] : defaultOutPath, srcPath);
        fileCompress.compress(srcPath);
        fileCompress.cleanup();
    }
    private void compress(Path srcPath) throws IOException, InterruptedException, ClassNotFoundException{

        if(!fs.exists(new Path(this.archiveDir))){
            fs.mkdirs(new Path(this.archiveDir));
        }
        if(fs.isDirectory(srcPath)){
            RemoteIterator<LocatedFileStatus> remoteIterator = fs.listFiles(srcPath, true);
            while(remoteIterator.hasNext()){
                LocatedFileStatus fileStatus = remoteIterator.next();
                compress(fileStatus.getPath());
            }
        }else if(fs.isFile(srcPath)){
            FSDataInputStream inputStream = fs.open(srcPath);

            Path destPath = new Path(this.archiveDir + Path.SEPARATOR + srcPath.getName() +".bz2");
            if(fs.exists(destPath)){
                destPath = new Path(this.archiveDir + Path.SEPARATOR + srcPath.getName() + "-" + new Date().getTime() + ".bz2");
            }
            FSDataOutputStream fsDataOutputStream = fs.create(destPath);
            CompressionCodec codec = (CompressionCodec)ReflectionUtils.newInstance(Class.forName("org.apache.hadoop.io.compress.BZip2Codec"), conf);
            CompressionOutputStream compressionOutputStream = codec.createOutputStream(fsDataOutputStream);

            IOUtils.copyBytes(inputStream, compressionOutputStream, this.conf);
            IOUtils.closeStream(inputStream);
            IOUtils.closeStream(compressionOutputStream);
        }
    }

    private void cleanup() throws IOException{
        if(fs != null){
            fs.close();
        }
    }
}
