package com.ipin.jd.task;

import com.ipin.jd.bean.db.JobZhineng;
import com.ipin.jd.service.JdConversion;

import java.util.concurrent.Callable;

/**
 * Created by janze on 1/23/18.
 */
public class ZhinengTask implements Callable<JobZhineng> {

    private JdConversion jdConversion;

    public ZhinengTask(JdConversion jdConversion) {
        this.jdConversion = jdConversion;
    }

    @Override
    public JobZhineng call() throws Exception {
        this.jdConversion.getJobZhinengFromJdRaw();
        return jdConversion.getJobZhineng();
    }
}
