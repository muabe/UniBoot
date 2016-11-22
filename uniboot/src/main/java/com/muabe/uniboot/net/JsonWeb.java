package com.muabe.uniboot.net;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.muabe.uniboot.log.Loger;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * Created by codemasta on 2015-09-14.
 */
public class JsonWeb {
    private String urlEncoding = "UTF-8";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("application/octet-stream");
    private OkHttpClient client;
    private HashMap<String, String> param;
    private HashMap<String, String> header;
    private HashMap<String, File> file;
    private String host;
    private String uri;
    private String paramString;
    protected boolean debug = true;
    private Call call;

    public static enum METHOD{
        POST,PUT,DELETE,PATCH
    }


    public JsonWeb(String host) {
        client = new OkHttpClient();
        header = new HashMap<>();
        param = new HashMap<>();
        file = new HashMap<>();
        uri = "";
        paramString = "";
        this.setHost(host);
    }

    public JsonWeb setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }


    private void addHeaderAll(Request.Builder reqestBuilder){
        String[] headerKeys = getHeaderKeys();
        for (String key : headerKeys) {
            if(header.get(key) != null) {
                reqestBuilder.addHeader(key, header.get(key));
            }
        }
    }

    public String getHost() {
        return host;
    }

    protected HashMap<String, String> getHeaderMap(){
        return header;
    }

    protected HashMap<String, File> getFileMap(){
        return file;
    }

    public JsonWeb setTimeOut(int seconds){
        client.setConnectTimeout(seconds, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(seconds, TimeUnit.SECONDS);
        return this;
    }

    protected <ResultType extends ResultAdapter> ResultType getResult(Response response, Class<ResultType> type) throws IOException {
        try {
            Constructor<ResultType> constructor = type.getConstructor();
            ResultType obj = constructor.newInstance();
            obj.response = response;
            obj.bodyString = response.body().string();
            return obj;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(type.getName()+" class does not implement Constructor(Response)", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(type.getName()+" class does not implement Constructor InvocationTargetException", e);
        } catch (InstantiationException e) {
            throw new RuntimeException(type.getName()+" class can not make instance Constructor(Response)", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(type.getName()+" class illegalAccess Constructor(Response)", e);
        }

    }

    public <ResultType extends ResultAdapter>ResultType get(Class<ResultType> resultType) throws IOException, WebException {
        HttpUrl.Builder builder = HttpUrl.parse(getFullUrl()).newBuilder();
        String[] keys = getParamKeys();
        for (String key : keys) {
            builder.addEncodedQueryParameter(key, param.get(key));
        }
        HttpUrl httpUrl = builder.build();

        Request.Builder reqestBuilder = new Request.Builder().url(httpUrl);
        addHeaderAll(reqestBuilder);

        Request request = reqestBuilder.build();
        debugRequest("GET", paramString);

        clearAllParams();
        call = client.newCall(request);
        Response response = call.execute();
        ResultType result = getResult(response, resultType);
        debugResponse(result.getBody(), response);
        unexpectedCode(response, result.getBody());
        return result;
    }

    private <ResultType extends ResultAdapter>ResultType post(Class<ResultType> resultType, METHOD method, String text) throws IOException, WebException {
        Request.Builder reqestBuilder = new Request.Builder();
        addHeaderAll(reqestBuilder);

        RequestBody body = RequestBody.create(JSON, text);
        reqestBuilder.url(getFullUrl());

        Request request = initMethod(method, reqestBuilder, body);
        debugRequest(method.toString(), text);

        clearAllParams();
        call = client.newCall(request);
        Response response = call.execute();
        ResultType result = getResult(response, resultType);
        debugResponse(result.getBody(), response);
        unexpectedCode(response, result.getBody());

        return result;
    }

    private <ResultType extends ResultAdapter>ResultType form(METHOD method, Class<ResultType> resultType) throws WebException, IOException {

        Request.Builder reqestBuilder = new Request.Builder()
                .url(getFullUrl())
                .cacheControl(new CacheControl.Builder().noCache().build());

        addHeaderAll(reqestBuilder);

        FormEncodingBuilder body = new FormEncodingBuilder();
        String[] keys = getParamKeys();
        for (String key : keys) {
            body.add(key, param.get(key));
        }


        Request request = initMethod(method, reqestBuilder, body.build());
        String methodString = method.toString();
        if(method.equals(METHOD.POST)){
            methodString = method.toString()+"(FORM)";
        }
        debugRequest(methodString, paramString);

        clearAllParams();
        call = client.newCall(request);
        Response response = call.execute();
        ResultType result = getResult(response, resultType);
        debugResponse(result.getBody(), response);
        unexpectedCode(response, result.getBody());
        return result;
    }

    public <ResultType extends ResultAdapter>ResultType form(Class<ResultType> resultType) throws WebException, IOException {
        return form(METHOD.POST, resultType);
    }

    public <ResultType extends ResultAdapter>ResultType post(Class<ResultType> resultType, String text) throws IOException, WebException {
        if(text==null){
            return this.form(METHOD.POST, resultType);
        }else{
            return this.post(resultType, METHOD.POST, text);
        }
    }
    public <ResultType extends ResultAdapter>ResultType put(Class<ResultType> resultType, String text) throws WebException, IOException {
        if(text==null){
            return this.form(METHOD.PUT, resultType);
        }else{
            return this.post(resultType, METHOD.PUT, text);
        }
    }
    public <ResultType extends ResultAdapter>ResultType delete(Class<ResultType> resultType, String text) throws WebException, IOException {
        if(text==null){
            return this.form(METHOD.DELETE, resultType);
        }else{
            return this.post(resultType, METHOD.DELETE, text);
        }
    }
    public <ResultType extends ResultAdapter>ResultType patch(Class<ResultType> resultType, String text) throws WebException, IOException {
        if(text==null){
            return this.form(METHOD.PATCH, resultType);
        }else{
            return this.post(resultType, METHOD.PATCH, text);
        }
    }

    public <ResultType extends ResultAdapter>ResultType post(Class<ResultType> resultType) throws IOException, WebException {
        return this.post(resultType, null);
    }
    public <ResultType extends ResultAdapter>ResultType put(Class<ResultType> resultType) throws WebException, IOException {
        return put(resultType, null);
    }
    public <ResultType extends ResultAdapter>ResultType delete(Class<ResultType> resultType) throws WebException, IOException {
        return delete(resultType, null);
    }
    public <ResultType extends ResultAdapter>ResultType patch(Class<ResultType> resultType) throws WebException, IOException {
        return patch(resultType, null);
    }


    public <ResultType extends ResultAdapter>ResultType multipart(METHOD method, Class<ResultType> resultType) throws WebException, IOException {
        if (method == null) {
            method = METHOD.POST;
        }
        Request.Builder reqestBuilder = new Request.Builder()
                .url(getFullUrl())
                .cacheControl(new CacheControl.Builder().noCache().build());

        addHeaderAll(reqestBuilder);

        MultipartBuilder body = new MultipartBuilder()
                .type(MultipartBuilder.FORM);

        String[] keys = getParamKeys();
        for (String key : keys) {
            body.addFormDataPart(key, param.get(key));
        }

        String[] fileKeys = getFileKeys();
        for (String key : fileKeys) {
//            body.addPart(
//                    Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
//                    RequestBody.create(MEDIA_TYPE_IMAGE, file.getView(key)));
            body
                    .addFormDataPart("file", file.get(key).getName(),
                            RequestBody.create(MEDIA_TYPE_IMAGE, file.get(key)));
        }

        Request request = initMethod(method, reqestBuilder, body.build());
        debugRequest("MULTIPART", paramString);

        clearAllParams();
        call = client.newCall(request);
        Response response = call.execute();
        ResultType result = getResult(response, resultType);
        debugResponse(result.getBody(), response);
        unexpectedCode(response, result.getBody());
        return result;
    }
    public <ResultType extends ResultAdapter>ResultType multipart(Class<ResultType> resultType) throws WebException, IOException {
        return this.multipart(null, resultType);
    }

    public Request initMethod(METHOD method, Request.Builder reqestBuilder, RequestBody body) {
        Request request;
        if(METHOD.DELETE == method) {
            request = reqestBuilder.delete(body).build();
        }else if(METHOD.PUT== method){
            request = reqestBuilder.put(body).build();
        }else if(METHOD.PATCH== method){
            request = reqestBuilder.patch(body).build();
        }
        else{
            request = reqestBuilder.post(body).build();
        }

        return request;
    }

    public String[] getHeaderKeys() {
        if (header.size() == 0) {
            return new String[0];
        }
        String keys[] = header.keySet().toArray(new String[0]);
        if (keys == null) {
            return new String[0];
        }
        return keys;
    }

    public static String sha1(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return "0x" + hexString.toString().toUpperCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String sha1(String s, String keyString) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(key);

        byte[] bytes = mac.doFinal(s.getBytes("UTF-8"));
        return Base64.encodeToString(bytes, bytes.length);
    }

    /**
     * 암호화
     * @param key
     * @param message
     * @return
     * @throws Exception
     */
    public static String encryptToAes(String key, String message) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] bytes = cipher.doFinal(message.getBytes("UTF-8"));
        return Base64.encodeToString(bytes, bytes.length);
    }

    /**
     * 복호화
     * @param key
     * @param msg
     * @return
     * @throws Exception
     */
    public static String decryptToAes(String key, String msg) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] bytes = cipher.doFinal(msg.getBytes("UTF-8"));
        return new String(bytes);
    }

    private void unexpectedCode(Response response, String bodyString) throws WebException {
        if (!response.isSuccessful()) {
            Log.e(this.getClass().getSimpleName(), "Server Response Error Unexpected code:" + response.code());
            Log.e(this.getClass().getSimpleName(), response.message());
            throw new WebException(response, bodyString, "Unexpected code " + response);
        }
    }

    public JsonWeb setHost(String host) {
        this.host = host;
        return this;
    }

    public JsonWeb setUri(String uri){
        if(uri!=null) {
            this.uri = uri;
        }
        return this;
    }

    protected String getFullUrl() throws MalformedURLException {
        return new URL(new URL(host), uri).toString()+ paramString;
    }

    public JsonWeb setParamEncoding(String charSet){
        this.urlEncoding = charSet;
        return this;
    }

    public String encoding(String msg){
        try {
            return URLEncoder.encode(msg, urlEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonWeb clearHeader() {
        header.clear();
        return this;
    }

    public JsonWeb addHeader(String key, String value) {
        header.put(key, value);
        return this;
    }

    public JsonWeb removeHeader(String key){
        header.remove(key);
        return this;
    }


    public JsonWeb clearParam() {
        param.clear();
        return this;
    }

    public JsonWeb addParam(String key, String value) {
        param.put(key, value);
        return this;
    }

    public JsonWeb removeParam(String key){
        param.remove(key);
        return this;
    }

    public String[] getParamKeys() {
        if (param.size() == 0) {
            return new String[0];
        }
        String keys[] = param.keySet().toArray(new String[0]);
        if (keys == null) {
            return new String[0];
        }
        return keys;
    }

    public JsonWeb clearFile() {
        file.clear();
        return this;
    }

    public JsonWeb addFile(String key, File value) {
        file.put(key, value);
        return this;
    }

    public JsonWeb removeFile(String key){
        file.remove(key);
        return this;
    }

    public String[] getFileKeys() {
        if (file.size() == 0) {
            return new String[0];
        }
        String keys[] = file.keySet().toArray(new String[0]);
        if (keys == null) {
            return new String[0];
        }
        return keys;
    }

    public JsonWeb addParamString(String paramString){
        if (paramString == null) {
            paramString = "";
        } else {
            paramString = "/?" + paramString;
        }
        this.paramString = paramString;
        return this;
    }

    private void clearAllParams(){
        clearHeader();
        clearParam();
        clearFile();
//        uri = "";
        paramString = "";
    }
    public JsonWeb ignoreVerify() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};

            // SSL 컨텍스트
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // SSL 소켓 생성
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            client.setSslSocketFactory(sslSocketFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }

        client.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        return this;
    }

    public void cancel(){
        if(call!=null && !call.isCanceled()){
            call.cancel();
        }
    }

    public OkHttpClient getOkHttpClient(){
        return this.client;
    }


    public static JsonSerializer<Date> dateSerializer = new JsonSerializer<Date>() {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext
                context) {
            return src == null ? null : new JsonPrimitive(src.getTime());
        }
    };

    public static JsonDeserializer<Date> dateDeserialize = new JsonDeserializer<Date>() {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Date(json.getAsLong());
        }
    };

    public static Gson getReponseGson(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, dateDeserialize);
        return builder.create();
    }

    public static Gson getRequestGson(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, dateSerializer);
        return builder.create();
    }


    private void debugRequest(String method, String text) throws MalformedURLException, UnsupportedEncodingException {
        if (debug) {
            Log.w(this.getClass().getSimpleName(), " ");
            Log.w(this.getClass().getSimpleName(), "----------------------------------------------------------------------------------------------------------");
            Log.d(this.getClass().getSimpleName(), "★ API Call : " + Loger.callLibrary(this.getClass())+" ★");
            Log.i(this.getClass().getSimpleName(), "○ [Request] " + method + " : " + getFullUrl());
            String[] headerKeys = getHeaderKeys();
            if (headerKeys.length > 0) {
                Log.i(this.getClass().getSimpleName(), "  -Header");
                for (String key : headerKeys) {
                    Log.d(this.getClass().getSimpleName(), "    " + key + ":" + header.get(key));
                }
            }

            if (text != null && !"".equals(text)) {
                Log.i(this.getClass().getSimpleName(), "  -Text");
                Log.d(this.getClass().getSimpleName(), "    "+text);
            }

            String[] paramKeys = getParamKeys();
            if (paramKeys.length > 0) {
                Log.i(this.getClass().getSimpleName(), "  -Parameter");
                for (int i = paramKeys.length-1; i>=0; i--) {
                    String key = paramKeys[i];
                    Log.d(this.getClass().getSimpleName(), "    "+key + ":" + param.get(key));
                }
            }

            String[] fileKeys = getFileKeys();
            if (fileKeys.length > 0) {
                Log.i(this.getClass().getSimpleName(), "  -File");
                for (int i = fileKeys.length-1; i>=0; i--) {
                    String key = fileKeys[i];
                    Log.d(this.getClass().getSimpleName(), "    "+key + ":" + file.get(key).getName());
                }
            }
        }
        customRequestLog(method, text);

    }
    protected void customRequestLog(String method, String text) throws MalformedURLException, UnsupportedEncodingException {

    }
    private void debugResponse(String bodyResult, Response response) {
        if (debug) {
            Log.i(this.getClass().getSimpleName(), " ");
            Log.i(this.getClass().getSimpleName(), "● [Response] Code : " + response.code());
            Log.i(this.getClass().getSimpleName(), "  -body");
            Log.d(this.getClass().getSimpleName(), bodyResult.replaceAll("\\r", ""));
            Log.w(this.getClass().getSimpleName(), "----------------------------------------------------------------------------------------------------------");
            Log.w(this.getClass().getSimpleName(), " ");
        }
        customResponseLog(bodyResult, response);
    }

    protected void customResponseLog(String bodyResult, Response response){

    }
}

