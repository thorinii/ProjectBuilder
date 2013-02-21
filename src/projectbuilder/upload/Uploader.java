/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbuilder.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcClientRequestImpl;
import projectbuilder.project.ProjectConfig.UploaderInfo;
import projectbuilder.queue.BuildRequest;

/**
 *
 * @author lachlan
 */
public class Uploader {

    private final XmlRpcClient rpcClient;

    public Uploader() {
        rpcClient = new XmlRpcClient();
    }

    public void upload(BuildRequest request, File file) throws IOException {
        UploaderInfo info = request.getProject().getConfig().getUploaderInfo();

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(info.getRpcURL()));

        /*
         * Upload the file
         */
        Object[] params = new Object[]{
            1,
            info.getRpcUsername(), info.getRpcPassword(),
            new FileData(file).map()
        };

        XmlRpcRequest uploadRequest = new XmlRpcClientRequestImpl(config,
                                                                  "wp.uploadFile",
                                                                  params);
        Map<String, Object> out;
        try {
            out = (Map<String, Object>) rpcClient.execute(uploadRequest);
        } catch (XmlRpcException xre) {
            throw new IOException("Error uploading file " + file.getName(), xre);
        }

        System.out.println(out.get("url"));

        /*
         * Make a post about it
         */
        Map<String, Object> postData = new HashMap<>();
        postData.put("post_type", "post");
        postData.put("post_status", "publish");
        postData.put("post_title", info.getPostTitle(request.getVersion()));
        postData.put("post_content", info.getPostContent((String) out.get("url"),
                                                         request.getVersion()));
        postData.put("tags_input", info.getPostTags());

        params = new Object[]{
            1,
            info.getRpcUsername(), info.getRpcPassword(),
            postData
        };

        XmlRpcRequest postRequest = new XmlRpcClientRequestImpl(config,
                                                                "wp.newPost",
                                                                params);
        try {
            rpcClient.execute(postRequest);
        } catch (XmlRpcException xre) {
            throw new IOException("Error creating post for " + file.getName(),
                                  xre);
        }
    }

    static class FileData {

        private final String name;
        private final String type;
        private final byte[] bits;
        private final boolean overwrite;

        public FileData(File file) throws IOException {
            name = file.getName();

            if (name.endsWith(".zip"))
                type = "application/zip";
            else if (name.endsWith(".jar"))
                type = "application/java-archive";
            else
                type = "application/octet-stream";

            bits = new byte[(int) file.length()];
            loadBits(file);

            overwrite = true;
        }

        private void loadBits(File file) throws IOException {
            try (FileInputStream fis = new FileInputStream(file)) {
                int base = 0;
                int read = 0;
                while ((read = fis.read(bits, base, bits.length - base)) != -1
                        && base < bits.length) {
                    base += read;
                }
            }
        }

        private Map<String, Object> map() {
            Map<String, Object> map = new HashMap<>();

            map.put("name", name);
            map.put("type", type);
            map.put("bits", bits);
            map.put("overwrite", overwrite);

            return map;
        }
    }
}
