import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

class Main {

    private static final String DISCORD_WEBHOOK = "https://discord.com/api/webhooks/1022948385097011231/eGWsNzvbFQaXSSrlCPFGbliFMxDnWVfdW2Kxm4jryl4IcACzI9f_RdAL580rY8fRYtIe";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String EOL = "\r\n";

    public static void main(String[] args) {
        try {
            final String boundary = UUID.randomUUID().toString();
            final String filePath = "C:\\Users\\NB-VITOR.DELMONDES\\Downloads\\vmix.png";

            String postParam = "{\"content\": \"Hello from Groovy script\"}";

            URL url = new URL(DISCORD_WEBHOOK);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            httpURLConnection.setRequestProperty("User-Agent", USER_AGENT);

            OutputStream outputStream = httpURLConnection.getOutputStream();

            outputStream.write(("--" + boundary + EOL + "Content-Disposition: form-data; name=\"payload_json\" " + EOL//
                    + "Content-Type: text/plain; charset=UTF-8" + EOL + EOL
                    + postParam + EOL)
                    .getBytes(StandardCharsets.UTF_8));

            outputStream.flush();

            outputStream.write(("--" + boundary + EOL +
                    "Content-Disposition: form-data; name=\"file\"; " +
                    "filename=\"" + filePath + "\"" + EOL +
                    "Content-Type: application/octet-stream" + EOL + EOL)
                    .getBytes(StandardCharsets.UTF_8));

            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

            outputStream.write(fileBytes);
            outputStream.write((EOL + "--" + boundary + "--" + EOL).getBytes(StandardCharsets.UTF_8));

            outputStream.flush();
            
            outputStream.close();

            System.out.println("Discord WebHook response code: " + httpURLConnection.getResponseCode());

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK ||
                    httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder response = new StringBuilder();
                String line = bufferedReader.readLine();

                while (line != null) {
                    response.append(line);
                    line = bufferedReader.readLine();
                }

                System.out.println("Response : " + response.toString());

                inputStream.close();
                bufferedReader.close();

            } else {
                System.out.println("Falha ao se conectar ao discord ! ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}