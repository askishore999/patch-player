package com.patchian.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Matcher {

    public Map<String, String> match(Set<String> listeners, File channels[], Map<String, String> listMap) {
        Map<String, String> mapChannelsToListener = new HashMap<String, String>();
        List<String> listListener = new ArrayList<String>(listeners);

        List<String> tempListIps = new ArrayList<String>(listListener);

        List<File> listChannels = Arrays.asList(channels);

        for (File channel : channels) {
            String position = getPosition(channel.getName());
            System.out.println("position: " + position);

            String ipAddress = listMap.get(position);

            if (ipAddress != null && !ipAddress.isEmpty()) {
                System.out.println("Adding: " + listMap.get(position) + " to channel: " + channel.getAbsolutePath());
                mapChannelsToListener.put(channel.getAbsolutePath(), listMap.get(position));
                tempListIps.remove(tempListIps.indexOf(position));
                listChannels.remove(channel);
            }
        }

        int i = 0;
        for (File channel : listChannels) {

            String name = getPosition(channel.getName());
            System.out.println("name: " + name);

            if (tempListIps.size() > 0) {
                System.out.println("Adding: " + tempListIps.get(i) + " to channel: " + channel.getAbsolutePath());
                mapChannelsToListener.put(channel.getAbsolutePath(), tempListIps.get(i));
                tempListIps.remove(i);
            } else {
                System.out.println("Adding: " + listListener.get(i) + " to channel: " + channel.getAbsolutePath());
                mapChannelsToListener.put(channel.getAbsolutePath(), listListener.get(i));
                i = (i + 1) % listListener.size();
            }
        }
        return mapChannelsToListener;
    }

    private String getPosition(String str) {
        return str.substring(str.lastIndexOf("_") + 1, str.lastIndexOf(".mp3"));
    }
}
