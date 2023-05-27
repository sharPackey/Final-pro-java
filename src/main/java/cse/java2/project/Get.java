package cse.java2.project;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

public class Get {

    private static final String API_ENDPOINT = "https://api.stackexchange.com/2.3/questions";
    private static final String SITE = "stackoverflow";
    private static final String TAGS = "java";
    private static final int PAGE_SIZE = 30;

    public static void main(String[] args) {
        Logger.getLogger("org.apache.http").setLevel(Level.WARN);
        Logger.getLogger("org.apache.http.wire").setLevel(Level.WARN);
        List<Question> questions = new ArrayList<>();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            for (int page = 1; page <= 20; page++) {
                String url = API_ENDPOINT+"?page="+page+"&pagesize="+PAGE_SIZE+ "&site=" + SITE + "&tagged=" + TAGS;
                HttpGet request = new HttpGet(url);
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    String json = EntityUtils.toString(response.getEntity());
                    ObjectMapper mapper = new ObjectMapper();
                    ResponseWrapper responseWrapper = mapper.readValue(json, ResponseWrapper.class);
                    List<Question> pageQuestions = responseWrapper.getItems();
                    questions.addAll(pageQuestions);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // Write the result to a file in JSON format
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> items = new HashMap<>();
            items.put("items", questions);
            FileWriter writer = new FileWriter("src/main/resources/Ouput/questions.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(writer, items);
            writer.close();
            System.out.println("Data saved as output.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        private List<String> tags;
        private owner owner;
        private boolean is_answered;
        private int view_count;
        private int answer_count;
        private int score;
        private long last_activity_date;
        private long creation_date;
        private long question_id;
        private String content_license;
        private String link;
        private String title;

        public List<String> getTags() {
            return tags;
        }

        public String getLink() {
            return link;
        }

        public cse.java2.project.classes.owner getOwner() {
            return owner;
        }

        public int getScore() {
            return score;
        }

        public boolean isIs_answered() {
            return is_answered;
        }

        public String getTitle() {
            return title;
        }

        public int getAnswer_count() {
            return answer_count;
        }

        public int getView_count() {
            return view_count;
        }

        public long getCreation_date() {
            return creation_date;
        }

        public long getLast_activity_date() {
            return last_activity_date;
        }

        public long getQuestion_id() {
            return question_id;
        }

        public String getContent_license() {
            return content_license;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public void setOwner(cse.java2.project.classes.owner owner) {
            this.owner = owner;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public void setAnswer_count(int answer_count) {
            this.answer_count = answer_count;
        }

        public void setContent_license(String content_license) {
            this.content_license = content_license;
        }

        public void setCreation_date(long creation_date) {
            this.creation_date = creation_date;
        }

        public void setIs_answered(boolean is_answered) {
            this.is_answered = is_answered;
        }

        public void setLast_activity_date(long last_activity_date) {
            this.last_activity_date = last_activity_date;
        }

        public void setQuestion_id(long question_id) {
            this.question_id = question_id;
        }

        public void setView_count(int view_count) {
            this.view_count = view_count;
        }
    }
}
