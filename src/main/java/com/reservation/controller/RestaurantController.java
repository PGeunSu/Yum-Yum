package com.reservation.controller;

import com.reservation.entity.reservation.Restaurant;
import com.reservation.repository.RestaurantRepository;
import com.reservation.service.RestaurantService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class RestaurantController {

    private final RestaurantService restaurantService;
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Autowired
    private RestaurantRepository restaurantRepository;
    @GetMapping("/api")
    public String index(){
        return "index";
    }

    @Value("${API-KEY.restaurantKey}")
    String restaurantKey;


    //파싱 후 db저장부분
    @PostMapping("/api")
    public String restaurant_save(String date, Model model){
        String result = "";
        String apiUrl = "http://openapi.seoul.go.kr:8088/" + restaurantKey + "/json/TbVwRestaurants/1/5/";
        try {
            URL url = new URL(apiUrl);
            BufferedReader bf;
            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
            JSONArray jsonArray = (JSONArray) jsonObject.get("TbVwRestaurants");
//            Long totalCount=(Long)TbVwRestaurants.get("list_total_count");

//            JSONObject subResult = (JSONObject)TbVwRestaurants.get("RESULT");
//            JSONArray infoArr = (JSONArray) TbVwRestaurants.get("row");

            for(int i=0;i<jsonArray.size();i++){
                JSONObject tmp = (JSONObject)jsonArray.get(i);
                Restaurant restaurant=new Restaurant(
                    (Long)tmp.get("id"),
                    (String)tmp.get("POST_SJ"),
                    (String)tmp.get("ADDRESS"),
                    (String)tmp.get("NEW_ADDRESS"),
                    (String)tmp.get("CMMN_TELNO"),
                    (String) tmp.get("CMMN_USE_TIME"));
                restaurantRepository.save(restaurant);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/findname";
    }

    @PostMapping("/create/restaurant")
    void createRestaurant(@RequestParam @DateTimeFormat
        (iso = DateTimeFormat.ISO.DATE) String POST_SJ,
        @RequestBody String CMMN_USE_TIME){

        restaurantService.createRestaurant(POST_SJ, CMMN_USE_TIME);// 위에서 요청해서 받았던 POST_SJ, CMMN_USE_TIME 넣어주자.
    }
}