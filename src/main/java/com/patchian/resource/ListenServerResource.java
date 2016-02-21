package com.patchian.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.patchian.util.ExecuteShellCommand;

public class ListenServerResource extends ServerResource {

    private static final String TMP_PART_FILE = "/tmp/current_%s.mp3";

    /**
     * @return
     */
    @Get
    public Representation doGet() {
        System.out.println("LISTENSERVERRESOURCE GET");

        Form requestParams = getRequest().getResourceRef().getQueryAsForm();
        String audioChannel = requestParams.getValues("audio_channel");
        System.out.println("------" + audioChannel);
        playSong(audioChannel);

        // String strAudioDelay = requestParams.getValues("audio_delay");
        // long audioDelay = 0;
        // if (strAudioDelay != null) {
        // audioDelay = Long.parseLong(strAudioDelay);
        // }
        // if (audioName != null && !audioName.isEmpty()) {
        // SoundPlayer player = new SoundPlayer();
        // String path = String.format(TMP_PART_FILE);
        // player.play(path);
        // }
        JSONObject resp = new JSONObject();
        resp.put("success", true);
        System.out.println(resp.toString());
        return new StringRepresentation(resp.toString(), MediaType.APPLICATION_JSON);
    }

    /**
     * Accpets Song Channel and Saves in Local Directory.
     * @param entity
     * @return
     * @throws Exception
     */
    @Post
    public Representation accept(Representation entity) throws Exception {
        System.out.println("LISTEN SERVER RESOURCE POST");
        if (entity != null) {
            if (MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(), true)) {
                // 1/ Create a factory for disk-based file items
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(1000240);

                // 2/ Create a new file upload handler based on the Restlet
                // FileUpload extension that will parse Restlet requests and
                // generates FileItems.
                RestletFileUpload upload = new RestletFileUpload(factory);

                // 3/ Request is parsed by the handler which generates a
                // list of FileItems
                FileItemIterator fileIterator = upload.getItemIterator(entity);
                System.out.println("Parsing Multipart SERVER RESOURCE");
                // Process only the uploaded item called "fileToUpload"
                // and return back
                boolean found = false;
                FileItemStream finput = null;
                String audioName = "";
                String audioChannel = "";

                Form form = getRequest().getResourceRef().getQueryAsForm();
                audioChannel = form.getValues("audio_channel");
                while (fileIterator.hasNext() && !found) {
                    FileItemStream fi = fileIterator.next();
                    System.out.println(fi.getFieldName());

                    if (fi.getFieldName().equals("upload_file")) {
                        found = true;
                        InputStream is = fi.openStream();
                        String path = String.format(TMP_PART_FILE, audioChannel);
                        OutputStream outstream = new FileOutputStream(new File(path));
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = is.read(buffer)) > 0) {
                            outstream.write(buffer, 0, len);
                        }
                        outstream.close();
                    }
                    System.out.println("------" + audioChannel);
                }
            } else {
                setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            }
        }

        System.out.println("DONE LISTEN SERVER RESOURCE");
        JSONObject resp = new JSONObject();
        resp.put("success", true);
        System.out.println(resp.toString());
        return new StringRepresentation(resp.toString(), MediaType.APPLICATION_JSON);
    }

    public void playSong(String audioChannel) {
        ExecuteShellCommand executer = new ExecuteShellCommand();
        System.out.println("test1");
        String tmpPartFile = String.format(TMP_PART_FILE, audioChannel);
        executer.executeCommand("sudo chmod 777 " + tmpPartFile);
        System.out.println("test2");
        executer.executeCommand("vlc " + tmpPartFile + " --play-and-exit");
        System.out.println("test3");
    }
}
