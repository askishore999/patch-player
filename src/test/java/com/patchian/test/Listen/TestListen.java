package com.patchian.test.Listen;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.json.JSONObject;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.ext.html.FormData;
import org.restlet.ext.html.FormDataSet;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import com.patchian.util.SoundPlayer;

public class TestListen {
    private static final String TMP_PART_FILE = "/tmp/side_left.wav";

    @Test
    public void testListenResource() {
        System.out.println("Into Post CALL");
        File file = new File("/tmp/side_left.wav");
        FileRepresentation fileEntity = new FileRepresentation(file, MediaType.MULTIPART_FORM_DATA);
        ClientResource client = new ClientResource("http://localhost:8080/patch-player-0.0.1-SNAPSHOT/listen");
        FormDataSet form = new FormDataSet();
        FormData fd = new FormData("upload_file", fileEntity);
        form.getEntries().add(fd);
        form.setMultipart(true);
        client.post(form, MediaType.MULTIPART_FORM_DATA);
        System.out.println(client.toString());
        JSONObject resp = new JSONObject();
        resp.put("success", true);
    }

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

    @Test
    public void testPlay() {
        SoundPlayer player = new SoundPlayer();
        player.play(TMP_PART_FILE);
    }
}
