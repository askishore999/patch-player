package com.patchian.test.Listen;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.ext.html.FormData;
import org.restlet.ext.html.FormDataSet;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import com.patchian.util.ExecuteShellCommand;
import com.patchian.util.SoundPlayer;

public class TestListen {
    private static final String TMP_PART_FILE = "/home/skishore/5chn.mp4";
    private static final String LISTEN_URL = "http://%s:8080/patch-player-0.0.1-SNAPSHOT/listen";
    private static final String AUDIO_PATH = "/tmp/%s_%s";

    // @Ignore
    // @Test
    // public void testListenResource() {
    // System.out.println("Into Post CALL");
    // System.out.println("HostServerResource Post");
    // String audioPath = "/tmp/5chn.mp4";
    // String audioName = "5chn";
    //
    // // Splitting Part Goes Here.
    // IFFMpeg ffMpeg = new FFMpegCMD(audioPath);
    // String outputDir = ffMpeg.split();
    //
    // File outputFolder = new File(outputDir);
    // // Matching Part Goes Here.
    // Matcher matcher = new Matcher();
    // Set<String> listRequests = new HashSet<String>();
    // listRequests.add("127.0.0.1");
    // Map<String, String> mapListnerToChannel = matcher.match(listRequests,
    // outputFolder.listFiles());
    //
    // // File Transfer Happens here.
    // for (String listenerClient : listRequests) {
    //
    // String url = String.format(LISTEN_URL, listenerClient);
    // ClientResource client = new ClientResource(url);
    // System.out.println("CLIENT:" + client.toString());
    //
    // File file = new File(mapListnerToChannel.get(listenerClient));
    //
    // FileRepresentation fileEntity = new FileRepresentation(file,
    // MediaType.MULTIPART_FORM_DATA);
    //
    // FormDataSet formDataSet = new FormDataSet();
    //
    // FormData fData = new FormData("upload_file", fileEntity);
    //
    // FormData fAudioName = new FormData("audio_name", audioName);
    // FormData fAudioChannel = new FormData("audio_channel",
    // mapListnerToChannel.get(listenerClient));
    //
    // formDataSet.getEntries().add(fData);
    // formDataSet.getEntries().add(fAudioName);
    // formDataSet.getEntries().add(fAudioChannel);
    // formDataSet.setMultipart(true);
    //
    // Representation result = client.post(formDataSet,
    // MediaType.MULTIPART_FORM_DATA);
    // System.out.println("RESPONSE:" + result);
    // }
    // }

    @Ignore
    @Test
    public void testListenResource2() throws FileUploadException, IOException {

        File file = new File("/tmp/abc.csv");
        FileRepresentation fileEntity = new FileRepresentation(file, MediaType.MULTIPART_FORM_DATA);
        ClientResource client = new ClientResource("http://localhost:8080/patch-player-0.0.1-SNAPSHOT/listen");
        FormDataSet form = new FormDataSet();
        FormData fd = new FormData("upload_file", fileEntity);
        form.getEntries().add(fd);
        form.setMultipart(true);

        Representation entity = form;

        System.out.println("Into LISTEN SERVER RESOURCE" + entity.getMediaType());
        Representation result = null;
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
                System.out.println("Into LISTEN SERVER RESOURCE" + fileIterator.hasNext() + fileIterator.toString());
                // Process only the uploaded item called "fileToUpload"
                // and return back
                boolean found = false;
                while (fileIterator.hasNext() && !found) {
                    FileItemStream fi = fileIterator.next();
                    if (fi.getFieldName().equals("upload_file")) {
                        found = true;
                        // consume the stream immediately, otherwise the stream
                        // will be closed.
                        StringBuilder sb = new StringBuilder("media type: ");
                        sb.append(fi.getContentType()).append("\n");
                        sb.append("file name : ");
                        sb.append(fi.getName()).append("\n");
                        BufferedReader br = new BufferedReader(new InputStreamReader(fi.openStream()));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        sb.append("\n");
                        result = new StringRepresentation(sb.toString(), MediaType.TEXT_PLAIN);
                        System.out.println(result);
                    }
                }
            } else {
                // POST request with no entity.
            }

            System.out.println("DONE LISTEN SERVER RESOURCE" + result);

        }
    }

    @Ignore
    @Test
    public void testPlay() {
        ExecuteShellCommand executer = new ExecuteShellCommand();
        executer.executeCommand("vlc " + TMP_PART_FILE);
    }
}
