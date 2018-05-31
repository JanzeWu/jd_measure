package com.ipin.jd.test;

import com.ipin.jd.bean.db.Base;
import com.ipin.jd.bean.db.JdOthers;
import org.junit.Test;

import java.io.File;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Spliterator;

/**
 * Created by janze on 1/5/18.
 */
public class TestBase {

    private Base base = new Base();

    @Test
    public void testJdOthers(){
//        JdOthers others = base.getObjectFromHiveStringTemplate("ffffe9479465eb517f437e13bd860c6c\u0001渠道销售\u0001猎头职位\u0001\\N\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u00011950024\u00019046\u00012017-02-09", JdOthers.class);
//        System.out.println(others.toString());
        File file = new File("/hladhg/fhalds/hive.sql");
        System.out.println(file.getName());

        Map<String, String> map = new HashMap<>();
        map.put(null, null);
        map.put("3", "2");
        System.out.println(map.get(null));
        System.out.println(System.identityHashCode(null));
        System.out.println("1".hashCode());
        System.out.println(System.identityHashCode("1"));

        System.out.println(new Date(-1L));

    }
}
