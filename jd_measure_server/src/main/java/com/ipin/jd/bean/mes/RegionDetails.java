package com.ipin.jd.bean.mes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by janze on 1/2/18.
 */
public class RegionDetails {
    private List<RegionDetail> regionDetails = new ArrayList<>();

    public List<RegionDetail> getRegionDetails() {
        return regionDetails;
    }

    public void setRegionDetails(List<RegionDetail> regionDetails) {
        this.regionDetails = regionDetails;
    }
}
