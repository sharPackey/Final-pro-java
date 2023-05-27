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
import java.util.*;

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

    @GetMapping({"/", "/Percen"})
    @ResponseBody
    public Double Percen() {
        double count = 0.0;
        try (InputStream i = new FileInputStream("src/main/resources/Ouput/answers.json")) {
            JSONTokener jsonTokener = new JSONTokener(i);
            JSONObject ans = new JSONObject(jsonTokener);
            JSONArray ans_array = ans.getJSONArray("items");

            JSONTokener jsonTokener2 = new JSONTokener(new FileInputStream("src/main/resources/Ouput/questions.json"));
            JSONObject que = new JSONObject(jsonTokener2);
            JSONArray que_array = que.getJSONArray("items");

            for (int j = 0; j < que_array.length(); j++) {
                JSONObject tmp = que_array.getJSONObject(j);
                if (!tmp.getBoolean("is_answered") || tmp.getInt("answer_count") <= 1) continue;
                long question = tmp.getLong("question_id");
                int tt = 0;
                for (int k = 0; k < ans_array.length(); k++) {
                    JSONObject jj = ans_array.getJSONObject(k);
                    if (jj.getLong("question_id") == question && jj.getBoolean("is_accepted")) {
                        tt = ans_array.getJSONObject(k).getInt("up_vote_count");
                        break;
                    }
                }
                for (int k = 0; k < ans_array.length(); k++) {
                    JSONObject jj = ans_array.getJSONObject(k);
                    if (!jj.getBoolean("is_accepted") && jj.getLong("question_id") == question && jj.getInt("up_vote_count") > tt) {
                        count++;
                    }
                }
            }
            return count / ans_array.length() * 100.0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @GetMapping({"/", "/most_tags"})
    @ResponseBody
    public Map<String, Integer> Mtages() {
        Map<String, Integer> map = new LinkedHashMap<>();
        try {

            JSONTokener jsonTokener2 = new JSONTokener(new FileInputStream("src/main/resources/Ouput/questions.json"));
            JSONObject que = new JSONObject(jsonTokener2);
            JSONArray que_array = que.getJSONArray("items");

            for (int j = 0; j < que_array.length(); j++) {
                JSONObject tmp = que_array.getJSONObject(j);
                JSONArray tags = tmp.getJSONArray("tags");
                for (int k = 0; k < tags.length(); k++) {
                    String temp = tags.getString(k);
                    if (Objects.equals(temp, "java")) continue;
                    if (map.containsKey(temp)) {
                        map.put(temp, map.get(temp) + 1);
                    } else {
                        map.put(temp, 1);
                    }
                }
            }
            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());

            // 使用 Collections.sort() 方法按值排序
            entryList.sort(new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                    return entry2.getValue().compareTo(entry1.getValue());
                }
            });
            map.clear();
            for (Map.Entry<String, Integer> entry : entryList) {
                map.put(entry.getKey(), entry.getValue());
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping({"/", "/vote_tags"})
    @ResponseBody
    public Map<String, Integer> Vtages() {
        Map<String, Integer> map = new LinkedHashMap<>();
        try {

            JSONTokener jsonTokener2 = new JSONTokener(new FileInputStream("src/main/resources/Ouput/questions.json"));
            JSONObject que = new JSONObject(jsonTokener2);
            JSONArray que_array = que.getJSONArray("items");

            for (int j = 0; j < que_array.length(); j++) {
                JSONObject tmp = que_array.getJSONObject(j);
                JSONArray tags = tmp.getJSONArray("tags");
                StringBuilder tg = new StringBuilder();
                for (int k = 0; k < tags.length(); k++) {
                    String temp = tags.getString(k);
                    tg.append(temp);
                    if (k != tags.length() - 1) tg.append("+");
                }
                String tag = tg.toString();
                int vote = tmp.getInt("up_vote_count");
                if (vote == 0) continue;
                if (map.containsKey(tag)) {
                    map.put(tag, map.get(tag) + vote);
                } else {
                    map.put(tag, vote);
                }
            }
            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
            entryList.sort(new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                    return entry2.getValue().compareTo(entry1.getValue());
                }
            });
            map.clear();
            for (Map.Entry<String, Integer> entry : entryList) {
                map.put(entry.getKey(), entry.getValue());
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping({"/", "/view_tags"})
    @ResponseBody
    public Map<String, Integer> Viewtages() {
        Map<String, Integer> map = new LinkedHashMap<>();
        try {

            JSONTokener jsonTokener2 = new JSONTokener(new FileInputStream("src/main/resources/Ouput/questions.json"));
            JSONObject que = new JSONObject(jsonTokener2);
            JSONArray que_array = que.getJSONArray("items");

            for (int j = 0; j < que_array.length(); j++) {
                JSONObject tmp = que_array.getJSONObject(j);
                JSONArray tags = tmp.getJSONArray("tags");
                StringBuilder tg = new StringBuilder();
                for (int k = 0; k < tags.length(); k++) {
                    String temp = tags.getString(k);
                    tg.append(temp);
                    if (k != tags.length() - 1) tg.append("+");
                }
                String tag = tg.toString();
                int vote = tmp.getInt("view_count");
                if (vote == 0) continue;
                if (map.containsKey(tag)) {
                    map.put(tag, map.get(tag) + vote);
                } else {
                    map.put(tag, vote);
                }
            }
            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
            entryList.sort(new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                    return entry2.getValue().compareTo(entry1.getValue());
                }
            });
            map.clear();
            for (Map.Entry<String, Integer> entry : entryList) {
                map.put(entry.getKey(), entry.getValue());
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping({"/", "/Threads"})
    @ResponseBody
    public Map<String, Integer> Threads() {
        Map<String, Integer> map = new TreeMap<>();
        for (int i = 0; i < 26; i++) {
            char a = (char) ('a' + i);
            String tmp = String.valueOf(a);
            map.put(tmp, 0);
        }
        map.put("Others", 0);
        try {

            JSONTokener jsonTokener2 = new JSONTokener(new FileInputStream("src/main/resources/Ouput/questions.json"));
            JSONObject que = new JSONObject(jsonTokener2);
            JSONArray que_array = que.getJSONArray("items");

            JSONTokener jsonTokener = new JSONTokener(new FileInputStream("src/main/resources/Ouput/answers.json"));
            JSONObject ans = new JSONObject(jsonTokener);
            JSONArray ans_array = ans.getJSONArray("items");

            JSONTokener jsonTokener3 = new JSONTokener(new FileInputStream("src/main/resources/Ouput/comments.json"));
            JSONObject com = new JSONObject(jsonTokener3);
            JSONArray com_array = com.getJSONArray("items");
            List<Integer> number = new ArrayList<>();
            for (int j = 0; j < que_array.length(); j++) {
                JSONObject tmp = que_array.getJSONObject(j);
                int nn = tmp.getJSONObject("owner").getInt("user_id");
                if (number.contains(nn)) continue;
                else {
                    number.add(nn);
                }
                String p = String.valueOf(tmp.getJSONObject("owner").getString("display_name").toCharArray()[0]).toLowerCase();
                if (map.containsKey(p)) {
                    map.put(p, map.get(p) + 1);
                } else {
                    map.put("Others", map.get("Others") + 1);
                }
            }
            for (int i = 0; i < ans_array.length(); i++) {
                JSONObject tmp_ans = ans_array.getJSONObject(i);
                int num = tmp_ans.getJSONObject("owner").getInt("user_id");
                if (number.contains(num)) continue;
                else {
                    number.add(num);
                }
                String p = String.valueOf(tmp_ans.getJSONObject("owner").getString("display_name").toCharArray()[0]).toLowerCase();
                p = p.toLowerCase();
                if (map.containsKey(p)) {
                    map.put(p, map.get(p) + 1);
                } else {
                    map.put("Others", map.get("Others") + 1);
                }
            }
            for (int i = 0; i < com_array.length(); i++) {
                JSONObject tmp_com = com_array.getJSONObject(i);
                int num = tmp_com.getJSONObject("owner").getInt("user_id");
                if (number.contains(num)) continue;
                else {
                    number.add(num);
                }
                String p = String.valueOf(tmp_com.getJSONObject("owner").getString("display_name").toCharArray()[0]).toLowerCase();

                if (map.containsKey(p)) {
                    map.put(p, map.get(p) + 1);
                } else {
                    map.put("Others", map.get("Others") + 1);
                }
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping({"/", "/Threads_ans"})
    @ResponseBody
    public Map<String, Integer> Threads_ans() {
        Map<String, Integer> map = new TreeMap<>();
        for (int i = 0; i < 26; i++) {
            char a = (char) ('a' + i);
            String tmp = String.valueOf(a);
            map.put(tmp, 0);
        }
        map.put("Others", 0);
        try {


            JSONTokener jsonTokener = new JSONTokener(new FileInputStream("src/main/resources/Ouput/answers.json"));
            JSONObject ans = new JSONObject(jsonTokener);
            JSONArray ans_array = ans.getJSONArray("items");

            List<Integer> number = new ArrayList<>();
            for (int i = 0; i < ans_array.length(); i++) {
                JSONObject tmp_ans = ans_array.getJSONObject(i);
                int num = tmp_ans.getJSONObject("owner").getInt("user_id");
                if (number.contains(num)) continue;
                else {
                    number.add(num);
                }
                String p = String.valueOf(tmp_ans.getJSONObject("owner").getString("display_name").toCharArray()[0]).toLowerCase();
                p = p.toLowerCase();
                if (map.containsKey(p)) {
                    map.put(p, map.get(p) + 1);
                } else {
                    map.put("Others", map.get("Others") + 1);
                }
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping({"/", "/Threads_com"})
    @ResponseBody
    public Map<String, Integer> Threads_com() {
        Map<String, Integer> map = new TreeMap<>();
        for (int i = 0; i < 26; i++) {
            char a = (char) ('a' + i);
            String tmp = String.valueOf(a);
            map.put(tmp, 0);
        }
        map.put("Others", 0);
        try {
            JSONTokener jsonTokener3 = new JSONTokener(new FileInputStream("src/main/resources/Ouput/comments.json"));
            JSONObject com = new JSONObject(jsonTokener3);
            JSONArray com_array = com.getJSONArray("items");
            List<Integer> number = new ArrayList<>();
            for (int i = 0; i < com_array.length(); i++) {
                JSONObject tmp_com = com_array.getJSONObject(i);
                int num = tmp_com.getJSONObject("owner").getInt("user_id");
                if (number.contains(num)) continue;
                else {
                    number.add(num);
                }
                String p = String.valueOf(tmp_com.getJSONObject("owner").getString("display_name").toCharArray()[0]).toLowerCase();

                if (map.containsKey(p)) {
                    map.put(p, map.get(p) + 1);
                } else {
                    map.put("Others", map.get("Others") + 1);
                }
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping({"/", "/Threads_most"})
    @ResponseBody
    public Map<Integer, Integer> Threads_most() {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        try {

            JSONTokener jsonTokener2 = new JSONTokener(new FileInputStream("src/main/resources/Ouput/questions.json"));
            JSONObject que = new JSONObject(jsonTokener2);
            JSONArray que_array = que.getJSONArray("items");

            JSONTokener jsonTokener = new JSONTokener(new FileInputStream("src/main/resources/Ouput/answers.json"));
            JSONObject ans = new JSONObject(jsonTokener);
            JSONArray ans_array = ans.getJSONArray("items");

            JSONTokener jsonTokener3 = new JSONTokener(new FileInputStream("src/main/resources/Ouput/comments.json"));
            JSONObject com = new JSONObject(jsonTokener3);
            JSONArray com_array = com.getJSONArray("items");

            List<Integer> number = new ArrayList<>();
            for (int j = 0; j < que_array.length(); j++) {
                JSONObject tmp = que_array.getJSONObject(j);
                int nn = tmp.getJSONObject("owner").getInt("user_id");
                if(nn==-1)continue;
                if (map.containsKey(nn)) {
                    map.put(nn, map.get(nn) + 1);
                } else {
                    map.put(nn,  1);
                }
            }
            for (int i = 0; i < ans_array.length(); i++) {
                JSONObject tmp_ans = ans_array.getJSONObject(i);
                int nn = tmp_ans.getJSONObject("owner").getInt("user_id");
                if(nn==-1)continue;
                if (map.containsKey(nn)) {
                    map.put(nn, map.get(nn) + 1);
                } else {
                    map.put(nn,  1);
                }
            }
            for (int i = 0; i < com_array.length(); i++) {
                JSONObject tmp_com = com_array.getJSONObject(i);
                int nn = tmp_com.getJSONObject("owner").getInt("user_id");
                if(nn==-1)continue;
                if (map.containsKey(nn)) {
                    map.put(nn, map.get(nn) + 1);
                } else {
                    map.put(nn,  1);
                }
            }
            List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(map.entrySet());
            entryList.sort(new Comparator<Map.Entry<Integer, Integer>>() {
                public int compare(Map.Entry<Integer, Integer> entry1, Map.Entry<Integer, Integer> entry2) {
                    return entry2.getValue().compareTo(entry1.getValue());
                }
            });
            map.clear();
            for (Map.Entry<Integer, Integer> entry : entryList) {
                map.put(entry.getKey(), entry.getValue());
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
