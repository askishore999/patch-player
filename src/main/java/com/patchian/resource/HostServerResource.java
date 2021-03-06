package com.patchian.resource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.fileupload.FileUploadException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.ext.html.FormData;
import org.restlet.ext.html.FormDataSet;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.patchian.util.FFMpegCMD;
import com.patchian.util.Matcher;
import com.patchian.util.RunnablePlay;

public class HostServerResource extends ServerResource {

    private static final String LISTEN_URL = "http://%s:8080/patch-player-0.0.1-SNAPSHOT/listen";
    public static Set<String> listRequests = new HashSet<String>();
    public static Map<String, String> listMap = new HashMap<String, String>();
    public int channels;
    public Map<String, String> mapListnerToChannel = new HashMap<String, String>();

    /**
     * Get a request to play a Song.
     * @param entity
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws ParseException
     * @throws FileUploadException
     */
    @Post("json")
    public Representation doPost(Representation entity) throws InterruptedException {

        System.out.println("HostServerResource Post");
        Form form = getRequest().getResourceRef().getQueryAsForm();
        String audioPath = form.getValues("audio_path");
        String audioName = form.getValues("audio_name");

        // Splitting Part Goes Here.
        FFMpegCMD ffMpeg = new FFMpegCMD(audioPath);
        String outputDir = ffMpeg.split();

        File outputFolder = new File(outputDir);
        // Matching Part Goes Here.
        Matcher matcher = new Matcher();
        System.out.println(outputFolder.list()[0]);
        System.out.println(listRequests.toString());
        Map<String, String> mapChannleToListener = matcher.match(listRequests, outputFolder.listFiles(), listMap);

        // File Transfer Happens here.
        for (Entry<String, String> listenerChannelClient : mapChannleToListener.entrySet()) {

            String listenerClient = listenerChannelClient.getValue();
            String url = String.format(LISTEN_URL, listenerClient);
            ClientResource client = new ClientResource(url);
            System.out.println("CLIENT:" + client.toString());

            File file = new File(listenerChannelClient.getKey());

            FileRepresentation fileEntity = new FileRepresentation(file, MediaType.MULTIPART_FORM_DATA);

            FormDataSet formDataSet = new FormDataSet();

            FormData fData = new FormData("upload_file", fileEntity);

            FormData fAudioName = new FormData("audio_name", audioName);

            formDataSet.getEntries().add(fData);
            formDataSet.setMultipart(true);

            String position =
                    listenerChannelClient.getKey().substring(listenerChannelClient.getKey().lastIndexOf("_") + 1,
                            listenerChannelClient.getKey().lastIndexOf(".mp3"));

            client.setQueryValue("audio_channel", position);
            Representation result = client.post(formDataSet, MediaType.MULTIPART_FORM_DATA);
            System.out.println("RESPONSE:" + result);
        }

        Thread.sleep(1);

        System.out.println("Mappings:" + mapChannleToListener.toString());
        // Calls the Play Function.
        for (Entry<String, String> listenerChannelClient : mapChannleToListener.entrySet()) {

            String listenerClient = listenerChannelClient.getValue();
            String url = String.format(LISTEN_URL, listenerClient);
            ClientResource client = new ClientResource(url);
            client.setQueryValue("audio_name", audioName);

            String position =
                    listenerChannelClient.getKey().substring(listenerChannelClient.getKey().lastIndexOf("_") + 1,
                            listenerChannelClient.getKey().lastIndexOf(".mp3"));

            client.setQueryValue("audio_channel", position);
            client.setQueryValue("audio_delay", "0");
            Thread th = new Thread(new RunnablePlay(client));
            th.start();
        }
        JSONObject resp = new JSONObject();
        resp.put("success", true);
        System.out.println(resp.toString());
        return new StringRepresentation(resp.toString(), MediaType.APPLICATION_JSON);
    }

    /**
     * Gets a request to Connect to the Server from Listener.
     * @return
     */
    @Get
    public Representation doGet() {
        System.out.println("HOSTSERVERRESOURCE GET");
        String ipAddress = getRequest().getClientInfo().getAddress();
        Form form = getRequest().getResourceRef().getQueryAsForm();
        String position = form.getValues("position");
        listRequests.add(ipAddress);
        if (position != null) {
            listMap.put(position, ipAddress);
            System.out.println("prioritizing: " + position + " to: " + ipAddress);
        }
        JSONObject resp = new JSONObject();
        resp.put("success", true);
        return new StringRepresentation(resp.toString(), MediaType.APPLICATION_JSON);
    }

}
