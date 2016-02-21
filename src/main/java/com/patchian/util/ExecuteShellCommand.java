package com.patchian.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExecuteShellCommand {

    public String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            String arr[] = {"/bin/bash", "-c", command };
            p = Runtime.getRuntime().exec(arr);
            p.waitFor();
            System.out.println("Exit Value:" + p.exitValue());
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line + "--");
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }
}
