package cse.java2.project.controller;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cse.java2.project.Get;
import cse.java2.project.GetAnswer;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class DemoController {

    /**
     * This method is called when the user requests the root URL ("/") or "/demo".
     * In this demo, you can visit localhost:9090 or localhost:9090/demo to see the result.
     *
     * @return the name of the view to be rendered
     * You can find the static HTML file in src/main/resources/templates/demo.html
     */
    @GetMapping({"/", "/demo"})
    @ResponseBody
    public Map<Integer, Integer> demo() {
        Map<Integer, Integer> a = new TreeMap<>();
        a.put(1, 2);
        return a;
    }

    @GetMapping({"/", "/noAnsRate"})
    @ResponseBody
    public double noAnsRate() {
        try (InputStream i = new FileInputStream("src/main/resources/Ouput/questions.json")) {
            JSONTokener jsonTokener = new JSONTokener(i);
            JSONObject qq = new JSONObject(jsonTokener);
            JSONArray qarray = qq.getJSONArray("items");
            System.out.println(qarray);
            double unanswered = 0.0;
            for (int j = 0; j < qarray.length(); j++) {
                boolean isAnswered = qarray.getJSONObject(j).getBoolean("is_answered");
                if (!isAnswered) {
                    unanswered++;
                }
            }
            return (double) unanswered / qarray.length() * 100;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @GetMapping({"/", "/MaxAvgAns"})
    @ResponseBody
    public Map<String, Double> MaxAvgAns() {
        try (InputStream i = new FileInputStream("src/main/resources/Ouput/questions.json")) {
            JSONTokener jsonTokener = new JSONTokener(i);
            JSONObject qq = new JSONObject(jsonTokener);
            JSONArray qarray = qq.getJSONArray("items");
            System.out.println(qarray);
            int count = 0;
            int max = 0;
            for (int j = 0; j < qarray.length(); j++) {
                count += qarray.getJSONObject(j).getInt("answer_count");
                if (max < qarray.getJSONObject(j).getInt("answer_count")) {
                    max = qarray.getJSONObject(j).getInt("answer_count");
                }
            }
            Map<String, Double> map = new TreeMap<>();
            map.put("avg", (double) count / qarray.length());
            map.put("max", (double) max);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping({"/", "/ans_dis"})
    @ResponseBody
    public Map<Integer, Integer> ans_dis() {
        try (InputStream i = new FileInputStream("src/main/resources/Ouput/questions.json")) {
            JSONTokener jsonTokener = new JSONTokener(i);
            JSONObject qq = new JSONObject(jsonTokener);
            JSONArray qarray = qq.getJSONArray("items");

            Map<Integer, Integer> map = new TreeMap<>();
            for (int j = 0; j < qarray.length(); j++) {
                int mm = qarray.getJSONObject(j).getInt("answer_count");
                if (map.containsKey(mm)) {
                    int now = map.get(mm);
                    map.replace(mm, now + 1);
                } else {
                    map.put(mm, 1);
                }
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping({"/", "/AcAns"})
    @ResponseBody
    public double AcAns() {
        try (InputStream i = new FileInputStream("src/main/resources/Ouput/answers.json")) {
            JSONTokener jsonTokener = new JSONTokener(i);
            JSONObject qq = new JSONObject(jsonTokener);
            JSONArray qarray = qq.getJSONArray("items");
            double unanswered = 0.0;
            for (int j = 0; j < qarray.length(); j++) {
                boolean isAcc = qarray.getJSONObject(j).getBoolean("is_accepted");
                if (isAcc) {
                    unanswered++;
                }
            }
            return (double) unanswered / qarray.length() * 100;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @GetMapping({"/", "/Time_dist"})
    @ResponseBody
    public Map<Integer, Integer> Time_dist() {
        Map<Integer, Integer> map = new TreeMap<>();
        try (InputStream i = new FileInputStream("src/main/resources/Ouput/answers.json")) {
            JSONTokener jsonTokener = new JSONTokener(i);
            JSONObject ans = new JSONObject(jsonTokener);
            JSONArray ans_array = ans.getJSONArray("items");

            JSONTokener jsonTokener2 = new JSONTokener(new FileInputStream("src/main/resources/Ouput/questions.json"));
            JSONObject que = new JSONObject(jsonTokener2);
            JSONArray que_array = que.getJSONArray("items");

            for (int j = 0; j < ans_array.length(); j++) {
                long question = ans_array.getJSONObject(j).getLong("question_id");
                long date_que = 0;
                for (int k = 0; k < que_array.length(); k++) {
                    if (que_array.getJSONObject(k).getLong("question_id") == question) {
                        date_que = que_array.getJSONObject(k).getLong("creation_date");
                        break;
                    }
                }
                long date_ans = ans_array.getJSONObject(j).getLong("creation_date");
                int delta = (int) (date_ans - date_que);
                if (map.containsKey(delta)) {
                    int now = map.get(delta);
                    map.replace(delta, now + 1);
                } else {
                    map.put(delta, 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
