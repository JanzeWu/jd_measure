package com.ipin.jd.task;

import com.ipin.jd.bean.db.JdJobWorkLocation;
import com.ipin.jd.service.JdConversion;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by janze on 1/23/18.
 */
public class LocationTask implements Callable<List<JdJobWorkLocation>> {

    private JdConversion jdConversion;

    public LocationTask(JdConversion jdConversion) {
        this.jdConversion = jdConversion;
    }

    @Override
    public List<JdJobWorkLocation> call() throws Exception {

        this.jdConversion.getJdJobWorkLocListFromJdRaw();
        return jdConversion.getWorkLocList();
    }
}
