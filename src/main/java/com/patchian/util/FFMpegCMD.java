package com.patchian.util;

public class FFMpegCMD {

    String command;
    String audioFile;

    private String fL;
    private String fR;
    private String fC;
    private String lFE;
    private String sL;
    private String sR;
    private String fileNameWoExtension;

    public FFMpegCMD(String fileToBeSplited) {

        String[] filePaths = fileToBeSplited.split("\\/");
        String fileName = filePaths[filePaths.length - 1];

        this.audioFile = fileToBeSplited;
        String[] fileNameSplits = fileName.split("\\.");
        fileNameWoExtension = fileNameSplits[0];
        String fileExtension = fileNameSplits[1];

        String prefix = fileNameWoExtension + "_";
        String suffix = "." + "mp3";
        this.fL = prefix + "fl" + suffix;
        this.fR = prefix + "fr" + suffix;
        this.fC = prefix + "fc" + suffix;
        this.lFE = prefix + "lfe" + suffix;
        this.sL = prefix + "sl" + suffix;
        this.sR = prefix + "sr" + suffix;

        /*
         * Example Command : ffmpeg -i in.mp3 -filter_complex
         * 'channelsplit=channel_layout=5.1[FL][FR][FC][LFE][SL][SR]'\ -map
         * '[FL]' front_left.mp3 -map '[FR]' front_right.mp3 -map '[FC]'
         * front_center.mp3 \ -map '[LFE]' lfe.mp3 -map '[SL]' side_left.mp3
         * -map '[SR]' side_right.mp3
         */
    }

    public String split() {

        String outputDir = "/tmp/" + fileNameWoExtension + "_out/";
        ExecuteShellCommand executer = new ExecuteShellCommand();

        String output = executer.executeCommand("sudo rm -rf " + outputDir);
        System.out.println(output);
        output = executer.executeCommand("sudo mkdir " + outputDir);
        System.out.println(output);

        this.command = "sudo /usr/local/bin/ffmpeg -i " + audioFile + " -filter_complex " +
                "'channelsplit=channel_layout=5.1[FL][FR][FC][LFE][SL][SR]' " +
                " -map '[FL]' " + outputDir + this.fL + " -map '[FR]' " + outputDir +
                this.fR +
                " -map '[FC]' " + outputDir + this.fC + " -map '[LFE]' " + outputDir
                + this.lFE +
                " -map '[SL]' " + outputDir + this.sL + " -map '[SR]' " + outputDir +
                this.sR + " -y";

        // this.command = "ping -c 3 google.com";
        executer = new ExecuteShellCommand();
        output = executer.executeCommand(this.command);

        return outputDir;
    }
}
