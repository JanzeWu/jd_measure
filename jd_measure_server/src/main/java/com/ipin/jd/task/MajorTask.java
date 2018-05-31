package com.ipin.jd.task;

import com.ipin.jd.bean.db.JdJobMajor;
import com.ipin.jd.service.JdConversion;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by janze on 1/23/18.
 */
public class MajorTask implements Callable<List<JdJobMajor>> {


    private JdConversion jdConversion;

    public MajorTask(JdConversion jdConversion) {
        this.jdConversion = jdConversion;
    }

    @Override
    public List<JdJobMajor> call() throws Exception {
         jdConversion.getJdJobMajorListFromJdRaw();
         return jdConversion.getJobMajorList();
    }
}
