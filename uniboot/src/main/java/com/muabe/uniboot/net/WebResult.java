package com.muabe.uniboot.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muabe.uniboot.util.JwJSONReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markj on 2015-12-04.
 */
public class WebResult extends WebResultAdapter {
    private String[] deps = null;

    public WebResult(){

    }

    public WebResult setDepth(String... deps_jsonKey){
        deps = deps_jsonKey;
        return this;
    }

    public JSONObject getJSON(String... deps_jsonKey) throws JSONException {
        if(deps_jsonKey == null){
            deps_jsonKey = deps;
        }
        if(getBody()==null || getBody().length()==0){
            return null;
        }
        if(deps_jsonKey==null || deps_jsonKey.length==0){
            return new JSONObject(getBody());
        }else{
            JSONObject json = new JSONObject(getBody());
            for(String key:deps_jsonKey){
                if(json.isNull(key)){
                    return null;
                }
                json = json.getJSONObject(key);
            }
            return json;
        }
    }

    public JSONArray getJSONArray(String... deps_jsonKey) throws JSONException {
        if(deps_jsonKey == null){
            deps_jsonKey = deps;
        }

        if(getBody()==null || getBody().length()==0){
            return null;
        }
        if(deps_jsonKey==null || deps_jsonKey.length==0){
            return new JSONArray(getBody());
        }else{
            JSONObject json = new JSONObject(getBody());
            for(int i=0;i<deps_jsonKey.length-1;i++){
                if(json.isNull(deps_jsonKey[i])){
                    return null;
                }
                json = json.optJSONObject(deps_jsonKey[i]);
            }
            if(json.isNull(deps_jsonKey[deps_jsonKey.length-1])){
                return null;
            }

            return json.optJSONArray(deps_jsonKey[deps_jsonKey.length-1]);
        }
    }

    public JwJSONReader.JSONType getJsonType(String... deps_jsonKey) throws JSONException{
        JwJSONReader.JSONType type =  JwJSONReader.getJSONType(getJSON(deps_jsonKey), null);
        return type;
    }

   public <Dto>Dto fromJson(Class<Dto> dtoClass, String... deps) throws JSONException {
        JSONObject jsonObject = getJSON(deps);
        if(jsonObject==null){
            return null;
        }
        Gson gson = JsonWeb.getReponseGson();
        Dto result = gson.fromJson(jsonObject.toString(), dtoClass);
        return result;
    }

    public <Dto>Dto fromJson(Class<Dto> dtoClass, JSONObject json){
        Gson gson = JsonWeb.getReponseGson();
        Dto result = gson.fromJson(json.toString(), dtoClass);
        return result;
    }

    public <Dto>Dto fromJson(TypeToken<?> typeToken, String... deps) throws JSONException {
        JSONObject jsonObject = getJSON(deps);
        if(jsonObject==null){
            return null;
        }
        Gson gson = JsonWeb.getReponseGson();
        Dto result = gson.fromJson(jsonObject.toString(), typeToken.getType());
        return result;
    }

    public <T>List<T> fromJsonList(Class<T> type, String... deps) throws JSONException {
        ArrayList<T> list = new ArrayList<>();
        JSONArray array = getJSONArray(deps);
        if(array!=null) {
            for (int i = 0; i < array.length(); i++) {
                list.add(fromJson(type, array.getJSONObject(i)));
            }
        }
        return list;
    }

    public <Dto>Dto getModel(Class<Dto> dtoClass, String... deps) throws JSONException {
        return fromJson(dtoClass, deps);
    }

    public <Dto>Dto getModel(Class<Dto> dtoClass, JSONObject json){
        Gson gson = JsonWeb.getReponseGson();
        Dto result = gson.fromJson(json.toString(), dtoClass);
        return fromJson(dtoClass, json);
    }

//    public <Dto>Dto getModel(TypeToken<?> typeToken, String... deps) throws JSONException {
//        return fromJson(typeToken, deps);
//    }

    public <T>List<T> getModelList(Class<T> type) throws JSONException {
        return fromJsonList(type);
    }

    public <T>List<T> getModelList(Class<T> type, String... deps) throws JSONException {
        return fromJsonList(type, deps);
    }

    public String getStringData(String name, String... deps) throws JSONException {
        JSONObject jsonObject = getJSON(deps);
        return jsonObject.getString(name);
    }

    public int getIntData(String name, String... deps) throws JSONException {
        JSONObject jsonObject = getJSON(deps);
        return jsonObject.getInt(name);
    }

    public boolean getBooleanData(String name, String... deps) throws JSONException {
        JSONObject jsonObject = getJSON(deps);
        return jsonObject.getBoolean(name);
    }

    public boolean isSuccessful(){
        return response.isSuccessful();
    }

//    public static Gson getCustomGson(){
//        GsonBuilder builder = new GsonBuilder();
//
//        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                return new Date(json.getAsJsonPrimitive().getAsLong());
//            }
//        });
//
//        return builder.create();
//    }


}
