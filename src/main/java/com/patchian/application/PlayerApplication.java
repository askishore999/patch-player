package com.patchian.application;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;

import com.patchian.resource.HostServerResource;
import com.patchian.resource.ListenServerResource;

public class PlayerApplication extends Application {

    /*
     * (non-Javadoc)
     * @see org.restlet.Application#createInboundRoot()
     */
    public synchronized Restlet createInboundRoot() {

        System.out.println("Incomming");

        Router router = new Router(getContext());
        router.attach("/listen", ListenServerResource.class);
        router.attach("/play", HostServerResource.class);
        return router;
    }

}
