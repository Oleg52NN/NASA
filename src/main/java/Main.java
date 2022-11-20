import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.URL;

public class Main {
    public static final String REMOTE_SERVICE_URI = "https://api.nasa.gov/planetary/apod?api_key=1QeYY3bioG4xkOAjKYTPmw9krCUjocGOJljs6Hlg";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("My test service")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        CloseableHttpResponse response = httpClient.execute(request);
        Post post = mapper.readValue(
                response.getEntity().getContent(),
                new TypeReference<>() {

                }
        );

        StringBuilder typeFile = new StringBuilder();
        for (int i = post.getUrl().length() - 5; i < post.getUrl().length(); i++) {
            typeFile.append(post.getUrl().charAt(i));
        }

        String fileName = post.getTitle() + "." + typeFile;
        copyFile(post.getUrl(), fileName);
     System.out.println(post);
    }

    private static void copyFile(String nameSource, String fileName) throws IOException {

        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        File myFile = new File(fileName);
        try {
            if (myFile.createNewFile())
                System.out.println("Файл был создан");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            inputStream = new BufferedInputStream(new URL(nameSource).openStream());
            outputStream = new BufferedOutputStream(new FileOutputStream(myFile));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }
}
