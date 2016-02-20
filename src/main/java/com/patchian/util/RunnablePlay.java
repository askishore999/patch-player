package com.patchian.util;

import org.restlet.resource.ClientResource;

public class RunnablePlay implements Runnable {

    private ClientResource client;

    public RunnablePlay(ClientResource client) {
        super();
        this.client = client;
    }

    @Override
    public void run() {
        client.get();
    }

}
