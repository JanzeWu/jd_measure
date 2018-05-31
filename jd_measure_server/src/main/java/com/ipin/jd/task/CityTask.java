package com.ipin.jd.task;

import com.ipin.jd.bean.db.JdJobWorkCity;
import com.ipin.jd.service.JdConversion;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by janze on 1/23/18.
 */
public class CityTask implements Callable<List<JdJobWorkCity>> {

    private JdConversion jdConversion;
    public CityTask(JdConversion jdConversion) {
        this.jdConversion = jdConversion;
    }

    @Override
    public List<JdJobWorkCity> call() throws Exception {

        jdConversion.getJdJobWorkCityListFromJdRaw();
        return jdConversion.getWorkCityList();
    }
}
