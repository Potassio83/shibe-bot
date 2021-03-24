package dev.potassio.shibebot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;


public class shibe_bot extends ListenerAdapter {

    public static JDA jda;

    public static void main(String[] arguments) throws Exception {
        File file = new File("token.txt");

        if (file.createNewFile()){
            System.out.println("Token file has been created \nremember to insert token!");
            System.exit(0);
        }else{
            System.out.println("Token file already exists");
        }

        BufferedReader br = new BufferedReader(new FileReader(file));

        String token;
        while ((token = br.readLine()) != null) {
            jda = JDABuilder.createDefault(token).build();
            jda.addEventListener(new shibe_bot());
        }
    }
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().equalsIgnoreCase("!shibe")) {
                JSONObject json = null;
                try {
                    json = readJsonFromUrl("https://shibe.online/api/shibes?count=1");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                e.getChannel().sendMessage(json.getJSONArray("shibes").toList().get(0).toString()).queue();
            }
        }
    private  String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1)
            sb.append((char)cp);
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        URLConnection connection = (new URL(url)).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.connect();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String jsonText = "{\"shibes\":" + readAll(rd) + "}";
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            connection.getInputStream().close();
        }
    }
}
