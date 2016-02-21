package com.patchian.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Matcher {

    public Map<String, String> match(Set<String> listeners, File channels[]) {
        Map<String, String> mapListnerToChannels = new HashMap<String, String>();
        int i = 0;
        for (String listener : listeners) {
            mapListnerToChannels.put(listener, channels[i].getAbsolutePath());
            i = (i + 1) % channels.length;
        }
        return mapListnerToChannels;
    }
}
