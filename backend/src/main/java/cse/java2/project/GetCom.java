package cse.java2.project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import cse.java2.project.classes.owner;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class GetCom {
    public static void main(String[] args) throws IOException {
        Logger.getLogger("org.apache.http").setLevel(Level.WARN);
        Logger.getLogger("org.apache.http.wire").setLevel(Level.WARN);
        JSONTokener jsonTokener = new JSONTokener(new FileInputStream("src/main/resources/Ouput/questions.json"));
        JSONObject ans = new JSONObject(jsonTokener);
        JSONArray ans_array = ans.getJSONArray("items");
        List<Question> questions = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        for (int i = 0; i < ans_array.length(); i++) {
            HttpUrl url = Objects.requireNonNull(HttpUrl.parse("https://api.stackexchange.com/2.3/questions/"
                            + ans_array.getJSONObject(i).getLong("question_id") + "/comments"))
                    .newBuilder()
                    .addQueryParameter("order", "desc")
                    .addQueryParameter("sort", "creation")
                    .addQueryParameter("site", "stackoverflow")
                    .addQueryParameter("pagesize", "100")
                    .addQueryParameter("key", "kDEglsq4D1FxRSKZoRd24Q((")
                    .addQueryParameter("client_id", "26195")
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                assert response.body() != null;
                String json = response.body().string();
                System.out.println(json);
                ObjectMapper mapper = new ObjectMapper();
                ResponseWrapper responseWrapper = mapper.readValue(json, ResponseWrapper.class);
                List<Question> pageQuestions = responseWrapper.getItems();
                questions.addAll(pageQuestions);
                System.out.println(i);
                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Write the result to a file in JSON format
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> items = new HashMap<>();
        items.put("items", questions);
        FileWriter writer = new FileWriter("src/main/resources/Ouput/comments.json");
        mapper.writerWithDefaultPrettyPrinter().writeValue(writer, items);
        writer.close();
        System.out.println("Data saved as output.json");
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseWrapper {
        private List<Question> items;

        public List<Question> getItems() {
            return items;
        }

        public void setItems(List<Question> items) {
            this.items = items;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Question {
        private owner owner;
        private boolean is_accepted;
        private int score;
        private long last_activity_date;
        private long last_edit_date;
        private long creation_date;
        private long answer_id;
        private long question_id;
        private String content_license;

        public cse.java2.project.classes.owner getOwner() {
            return owner;
        }

        public long getAnswer_id() {
            return answer_id;
        }

        public long getQuestion_id() {
            return question_id;
        }

        public String getContent_license() {
            return content_license;
        }

        public long getLast_activity_date() {
            return last_activity_date;
        }

        public long getCreation_date() {
            return creation_date;
        }

        public boolean isIs_accepted() {
            return is_accepted;
        }

        public long getLast_edit_date() {
            return last_edit_date;
        }

        public int getScore() {
            return score;
        }

        public void setIs_accepted(boolean is_accepted) {
            this.is_accepted = is_accepted;
        }

        public void setQuestion_id(long question_id) {
            this.question_id = question_id;
        }

        public void setAnswer_id(long answer_id) {
            this.answer_id = answer_id;
        }

        public void setLast_activity_date(long last_activity_date) {
            this.last_activity_date = last_activity_date;
        }

        public void setLast_edit_date(long last_edit_date) {
            this.last_edit_date = last_edit_date;
        }

        public void setContent_license(String content_license) {
            this.content_license = content_license;
        }

        public void setCreation_date(long creation_date) {
            this.creation_date = creation_date;
        }

        public void setOwner(cse.java2.project.classes.owner owner) {
            this.owner = owner;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}
