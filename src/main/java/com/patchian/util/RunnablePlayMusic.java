package com.patchian.util;

public class RunnablePlayMusic extends Thread {
    private ExecuteShellCommand cmd;
    private String filePath;

    public RunnablePlayMusic(ExecuteShellCommand cmd, String filePath) {
        this.cmd = cmd;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        cmd.executeCommand("vlc " + filePath + " --play-and-exit");
    }

}
