package ru.ip_fateev.lavka.Inventory;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.ip_fateev.lavka.App;

class ApiProductListGetResult {
    public Boolean valid = false;

    public Boolean result = false;
    public List<Long> id_list = new ArrayList<>();

    public ApiProductListGetResult(JSONObject root) {
        fromJson(root);
    }

    public void fromJson(JSONObject root) {
        try {
            result = root.getBoolean("result");
            JSONArray id_list_tmp = root.getJSONArray("id_list");
            for (int i = 0; i < id_list_tmp.length(); i++) {
                id_list.add(id_list_tmp.getLong(i));
            }

            valid = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

class ApiProductGetResult {
    public Boolean valid = false;

    public Boolean result = false;
    public Long product_id;
    public String name = "";
    public String barcode = "";
    public Double price;

    public ApiProductGetResult(JSONObject root) {
        fromJson(root);
    }

    public void fromJson(JSONObject root) {
        try {
            result = root.getBoolean("result");
            product_id = root.getLong("product_id");
            name = root.getString("name");
            barcode = root.getString("barcode");
            price = root.getDouble("price");

            valid = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadService extends IntentService {

    private static final String ACTION_DOWNLOAD = "ru.ip_fateev.my_cash.Inventory.action.DOWNLOAD";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "ru.ip_fateev.my_cash.Inventory.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "ru.ip_fateev.my_cash.Inventory.extra.PARAM2";

    private Handler mHandler = null;
    private long startTime;

    private static final String TAG = "DownloadService";

    private static final String API_URL = "https://ip-fateev.ru/api";
    private static final String API_URL_PRODUCT_LIST = API_URL + "/product/list";
    private static final String API_URL_PRODUCT_GET = API_URL + "/product/";

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mHandler = new Handler();
        startTime = SystemClock.elapsedRealtime();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        long workTime = getElapsedTime();
        Log.d(TAG, "onDestroy");
    }

    private long getElapsedTime() {
        long currentTime = SystemClock.elapsedRealtime();
        long elapsedTime = currentTime - startTime;

        return elapsedTime;
    }
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionDownload(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionDownload(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDownload(String param1, String param2) {
        Log.d(TAG, "Work start");
        /*mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DownloadService.this, "Обновление списка товаров.", Toast.LENGTH_LONG).show();
            }
        });*/

        //String urlString = "https://jsonplaceholder.typicode.com/todos/1";
        //String urlString = "http://172.16.0.138:5000/api/test";
        String urlString = "https://ip-fateev.ru/api/product/list";

        String response = "";

        LocalData inventory = App.Companion.getInstance().getInventory();

        Pair<Boolean, JSONObject> getProductListResult = syncRequestGetJson(API_URL_PRODUCT_LIST);

        if (getProductListResult.first) {
            ApiProductListGetResult res = new ApiProductListGetResult(getProductListResult.second);

            if (res.valid && res.result) {
                List<Product> productList = inventory.getProductList();
                List<Long> productIdList = new ArrayList<>();

                for (int i = 0; i < productList.size(); i++) {
                    productIdList.add(productList.get(i).id);
                }

                // ЧТО ДОБАВИТЬ вычитаем из полученного списка, то что у нас есть
                List<Long> forAdd = new ArrayList<>(res.id_list);
                forAdd.removeAll(productIdList);

                // ЧТО УДАЛИТЬ вычитаем из нашего списка то, что получили
                List<Long> forRemove = new ArrayList<>(productIdList);
                forRemove.removeAll(res.id_list);

                // ЧТО СРАВНИТЬ вычитаем из списка, который получили, то что удаляем и то что удаляем
                List<Long> forCompare = new ArrayList<>(res.id_list);
                forCompare.removeAll(forAdd);
                forCompare.removeAll(forRemove);

                // скачиваем себе, то что нужно добавить
                for (int i = 0; i < forAdd.size(); i++) {
                    Long id = forAdd.get(i);
                    Pair<Boolean, JSONObject> getProductResult = syncRequestGetJson(API_URL_PRODUCT_GET + id.toString());
                    if (getProductResult.first) {
                        ApiProductGetResult product = new ApiProductGetResult(getProductResult.second);

                        if (product.valid && product.result) {

                            Product newProduct = new Product();
                            newProduct.id = product.product_id;
                            newProduct.name = product.name;
                            newProduct.barcode = product.barcode;
                            newProduct.price = product.price;

                            inventory.InsertProduct(newProduct);
                        }
                    }
                }
            }
            Log.d(TAG, "Response is:\n " + res);
        }

        Log.d(TAG, "Work complete");
    }

    private Pair<Boolean, String> syncRequestGet(String urlString) {
        Boolean result = false;
        String response = "";

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setUseCaches(false);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                InputStream stream = urlConnection.getErrorStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                String s = "";
                while ((s = bufferedReader.readLine()) != null) {
                    response += s;
                }
                bufferedReader.close();
            } else {
                InputStream stream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                String s = "";
                while ((s = bufferedReader.readLine()) != null) {
                    response += s;
                }
                bufferedReader.close();
                result = true;
            }
            urlConnection.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return new Pair<>(result, response);
    }

    private Pair<Boolean, JSONObject> syncRequestGetJson(String urlString) {
        Boolean result = false;
        JSONObject response = null;

        Pair<Boolean, String> res = syncRequestGet(urlString);

        if (res.first) {
            try {
                response = new JSONObject(res.second);
                result = true;

                Log.d(TAG, "Get JSON Response is:\n " + response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.d(TAG, "Get JSON Response is:\n " + res.second);
        }

        return new Pair<>(result, response);
    }

    private void asyncRequest() {
        String urlString = "https://jsonplaceholder.typicode.com/todos/1";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response is:\n" + response);
                        long workTime = getElapsedTime();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DownloadService.this, "Response is:\n" + response + "\nelapsed " + workTime + " ms", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DownloadService.this, "That didn't work!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}