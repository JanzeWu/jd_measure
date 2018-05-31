package com.ipin.jd.test;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by janze on 1/3/18.
 */
public class TestProcessBuilder {

    @Test
    public void test() throws Exception{
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();
        commands.add("hive");
        commands.add("-e");
        commands.add("select * from edw_jd.jd_entry limit 1;");
        processBuilder.command(commands);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null){
            System.out.println(line);
        }

    }
}
