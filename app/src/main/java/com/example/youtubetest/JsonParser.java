package com.example.youtubetest;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.UnsupportedEncodingException;
        import java.util.zip.GZIPInputStream;

        import org.apache.http.Header;
        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.json.JSONObject;

        import android.util.Log;

public class JsonParser {

    public String url="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet%2CcontentDetails&maxResults=30&playlistId=PLuwvwpFd3r9VCVeEymYcRqmoPVFzJjhZJ&fields=items(contentDetails%2Cetag%2Cid%2Csnippet)%2CnextPageToken&key=AIzaSyDeXBBkoNTU__uehxTmJNMcrQ_T-v7DVu0&access_token=";

//You can change the playlistId
//You can change the maxResults from 30 to 50 only videos can be fetched at a time
//For more detail go to this link https://developers.google.com/youtube/v3/docs/playlistItems/list#try-it

    private static StringBuilder sb;
    private JSONObject jObj;

    public JsonParser() {
        // TODO Auto-generated constructor stub
    }

    public JSONObject getJsonFromYoutube(String url){
        DefaultHttpClient httpclient = new DefaultHttpClient();
        Log.e("url",url);
        HttpGet getRequest = new HttpGet(url);
        getRequest.setHeader("Accept", "application/json");
        // Use GZIP encoding
        getRequest.setHeader("Accept-Encoding", "gzip"); //
        try {
            HttpResponse response = (HttpResponse) httpclient
                    .execute(getRequest);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                if (contentEncoding != null
                        && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                    instream = new GZIPInputStream(instream);
                }
                // convert content stream to a String
                String result = readStream(instream);
                Log.i("JSON", result);
                instream.close();
                jObj = new JSONObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jObj;
    }
    private static String readStream(InputStream is) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

            sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return sb.toString();
    }
}


