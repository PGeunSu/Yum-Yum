package com.reservation.service;

import com.reservation.repository.RestaurantRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    @Value("${API-KEY.restaurantKey}")
    String restaurantKey;
    private final RestaurantRepository restaurantRepository;



    public void createRestaurant(String POST_SJ, String CMMN_USE_TIME) {
        System.out.println(getRestaurantString());
    }

    //Api호출부분
    private String getRestaurantString() {
        String apiUrl = "http://openapi.seoul.go.kr:8088/" + restaurantKey + "/TbVwRestaurants/1/5/";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();//apiUrl을 http 형식으로 연결
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            //200okay이런게 정상코드
            BufferedReader br;
            if (responseCode == 200) {//200이라면, 제대로 응답
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream()));
            }
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            return response.toString();

        } catch (Exception e) {
            return "failed to get response";
        }
    }

//    private Map<String, Object> parseRestaurant(String jsonString){
//        JSONParser jsonParser = new JSONParser();
//        JSONObject jsonObject;
//
//        try{
//            jsonObject = (JSONObject) jsonParser.parse(jsonString);
//        }catch(ParseException e){
//            throw new RuntimeException(e);
//        }
//
//        Map<String, Object> resultMap = new HashMap<>();
//
//        JSONObject mainData = (JSONObject)jsonObject.get("POST_SJ");
//        resultMap.put("temp", mainData.get("temp"));
//        JSONArray RestaurantArray = (JSONArray) jsonObject.get("CMMN_USE_TIME");
//        JSONObject RestaurantData = (JSONObject) RestaurantArray.get(0);
//
//        resultMap.put("POST_SJ", RestaurantData.get("POST_SJ"));
//        resultMap.put("CMMN_USE_TIME", RestaurantData.get("CMMN_USE_TIME"));
//
//        return resultMap;
//    }

}