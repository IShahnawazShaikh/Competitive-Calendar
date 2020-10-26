package com.shahnawazshaikh.competitive.view_models;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shahnawazshaikh.competitive.models.ContestBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchData extends ViewModel {

    private MutableLiveData<List<ContestBean>> all=null;
    private MutableLiveData<List<ContestBean>> codeforces=null;
    private MutableLiveData<List<ContestBean>> codechef=null;
    private MutableLiveData<List<ContestBean>> atcoder=null;
    private MutableLiveData<List<ContestBean>> hackerrank=null;
    private MutableLiveData<List<ContestBean>> leetcode=null;
    private MutableLiveData<List<ContestBean>> hackerearth=null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LiveData<List<ContestBean>> getHackerearth(Context context) {
        //if the list is null
        if (hackerearth == null) {
            hackerearth=new MutableLiveData();
            //we will load it asynchronously from server in this method
            // Log.d("DATA", category + subcategory + subject + "");
            getHackerearthData(context);
        }

        //finally we will return the list
        return hackerearth;
    }


    public LiveData<List<ContestBean>> getLeetcode(Context context) {
        //if the list is null
        if (leetcode == null) {
            leetcode=new MutableLiveData();
            //we will load it asynchronously from server in this method
            // Log.d("DATA", category + subcategory + subject + "");
            getLeetcodeData(context);
        }

        //finally we will return the list
        return leetcode;
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    public LiveData<List<ContestBean>> getHackerRank(Context context) {
        //if the list is null
        if (hackerrank == null) {
            hackerrank=new MutableLiveData();
            //we will load it asynchronously from server in this method
            // Log.d("DATA", category + subcategory + subject + "");
            getHackerRankData(context);
        }

        //finally we will return the list
        return hackerrank;
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    public LiveData<List<ContestBean>> getAtcoder(Context context) {
        //if the list is null
        if (atcoder == null) {
            atcoder=new MutableLiveData();
            //we will load it asynchronously from server in this method
            // Log.d("DATA", category + subcategory + subject + "");
            getAtcoderData(context);
        }

        //finally we will return the list
        return atcoder;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public LiveData<List<ContestBean>> getCodechef(Context context) {
        //if the list is null
        if (codechef == null) {
            codechef=new MutableLiveData();
            //we will load it asynchronously from server in this method
            // Log.d("DATA", category + subcategory + subject + "");
            getCodechefData(context);
        }

        //finally we will return the list
        return codechef;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public LiveData<List<ContestBean>> getAll(Context context) {
        //if the list is null
        if (all == null) {
             all=new MutableLiveData();
            //we will load it asynchronously from server in this method
           // Log.d("DATA", category + subcategory + subject + "");
            getDataAll(context);
        }

        //finally we will return the list
        return all;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LiveData<List<ContestBean>> getCodeforces(Context context) {
        //if the list is null
        if (codeforces == null) {
            codeforces=new MutableLiveData();
            //we will load it asynchronously from server in this method
            // Log.d("DATA", category + subcategory + subject + "");
            getCodeforcesData(context);
        }

        //finally we will return the list
        return codeforces;
    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getHackerearthData(Context context) {
        Instant date2=Instant.now();
        String ans=""+date2;
        ans=ans.substring(0,10);
        ans = ans + "T00:00:00";
        String replaceString = ans.replace(":", "%3A");
        StringRequest request = new StringRequest(Request.Method.GET, "https://clist.by:443/api/v1/contest/?start__gt="+replaceString,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("objects");
                            List<ContestBean> allContestBean = new ArrayList<ContestBean>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                JSONObject resource = obj.getJSONObject("resource");
                                //  System.out.println("======================================================");
                                if (resource.optString("name").equals("hackerearth.com")) {

                                    ContestBean object = new ContestBean("" + obj.optString("start"),
                                            "" + obj.optString("end"),
                                            obj.optString("event"),
                                            obj.optString("duration"),
                                            obj.optString("id"),
                                            obj.optString("href"),
                                            resource.optString("icon"),
                                            resource.optString("name"));
                                    allContestBean.add(object);
                                }
                            }

                            // TimeZone utc=TimeZone.getTimeZone("UTC");
                            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            //f.setTimeZone(utc);
                            //GregorianCalendar cal1=new GregorianCalendar(utc);
                            //GregorianCalendar cal2=new GregorianCalendar(utc);

                            Collections.sort(allContestBean, new Comparator<ContestBean>() {
                                @Override
                                public int compare(ContestBean o1, ContestBean o2) {
                                    try {
//                                        start1.setTime(f.parse(o1.getStart()));
//                                        start2.setTime(f.parse(o2.getStart()));
                                        return f.parse(o1.getStart()).compareTo(f.parse(o2.getStart()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return 0;
                                }
                            });
                         //  hackerearth= new MutableLiveData<List<ContestBean>>(allContestBean);
                           hackerearth.setValue(allContestBean);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(getApplicationContext(), "Error: ", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(,"Response errorr", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", " ApiKey imshahnawaz:0145f9cdb0106581e7b89a02820f4f0e9e701456");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }





    public void getLeetcodeData(Context context) {
        Instant date2=Instant.now();
        String ans=""+date2;
        ans=ans.substring(0,10);
        ans = ans + "T00:00:00";
        String replaceString = ans.replace(":", "%3A");
        StringRequest request = new StringRequest(Request.Method.GET, "https://clist.by:443/api/v1/contest/?start__gt="+replaceString,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("objects");
                            List<ContestBean> allContestBean = new ArrayList<ContestBean>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                JSONObject resource = obj.getJSONObject("resource");
                                //  System.out.println("======================================================");
                                if (resource.optString("name").equals("leetcode.com")) {

                                    ContestBean object = new ContestBean("" + obj.optString("start"),
                                            "" + obj.optString("end"),
                                            obj.optString("event"),
                                            obj.optString("duration"),
                                            obj.optString("id"),
                                            obj.optString("href"),
                                            resource.optString("icon"),
                                            resource.optString("name"));
                                    allContestBean.add(object);
                                }
                            }

                            // TimeZone utc=TimeZone.getTimeZone("UTC");
                            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            //f.setTimeZone(utc);
                            //GregorianCalendar cal1=new GregorianCalendar(utc);
                            //GregorianCalendar cal2=new GregorianCalendar(utc);

                            Collections.sort(allContestBean, new Comparator<ContestBean>() {
                                @Override
                                public int compare(ContestBean o1, ContestBean o2) {
                                    try {
//                                        start1.setTime(f.parse(o1.getStart()));
//                                        start2.setTime(f.parse(o2.getStart()));
                                        return f.parse(o1.getStart()).compareTo(f.parse(o2.getStart()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return 0;
                                }
                            });

                            leetcode.setValue(allContestBean);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(getApplicationContext(), "Error: ", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(,"Response errorr", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", " ApiKey imshahnawaz:0145f9cdb0106581e7b89a02820f4f0e9e701456");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getHackerRankData(Context context) {
        Instant date2=Instant.now();
        String ans=""+date2;
        ans=ans.substring(0,10);
        ans = ans + "T00:00:00";
        String replaceString = ans.replace(":", "%3A");


        StringRequest request = new StringRequest(Request.Method.GET, "https://clist.by:443/api/v1/contest/?start__gt="+replaceString,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("objects");
                            List<ContestBean> allContestBean = new ArrayList<ContestBean>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                JSONObject resource = obj.getJSONObject("resource");
                                //  System.out.println("======================================================");
                                if (resource.optString("name").equals("hackerrank.com")) {

                                    ContestBean object = new ContestBean("" + obj.optString("start"),
                                            "" + obj.optString("end"),
                                            obj.optString("event"),
                                            obj.optString("duration"),
                                            obj.optString("id"),
                                            obj.optString("href"),
                                            resource.optString("icon"),
                                            resource.optString("name"));
                                    allContestBean.add(object);
                                }
                            }

                            // TimeZone utc=TimeZone.getTimeZone("UTC");
                            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            //f.setTimeZone(utc);
                            //GregorianCalendar cal1=new GregorianCalendar(utc);
                            //GregorianCalendar cal2=new GregorianCalendar(utc);

                            Collections.sort(allContestBean, new Comparator<ContestBean>() {
                                @Override
                                public int compare(ContestBean o1, ContestBean o2) {
                                    try {
//                                        start1.setTime(f.parse(o1.getStart()));
//                                        start2.setTime(f.parse(o2.getStart()));
                                        return f.parse(o1.getStart()).compareTo(f.parse(o2.getStart()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return 0;
                                }
                            });

                            hackerrank.setValue(allContestBean);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(getApplicationContext(), "Error: ", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(,"Response errorr", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", " ApiKey imshahnawaz:0145f9cdb0106581e7b89a02820f4f0e9e701456");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getAtcoderData(Context context) {
        Instant date2=Instant.now();
        String ans=""+date2;
        ans=ans.substring(0,10);
        ans = ans + "T00:00:00";
        String replaceString = ans.replace(":", "%3A");
        StringRequest request = new StringRequest(Request.Method.GET, "https://clist.by:443/api/v1/contest/?start__gt="+replaceString,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("objects");
                            List<ContestBean> allContestBean = new ArrayList<ContestBean>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                JSONObject resource = obj.getJSONObject("resource");
                                //  System.out.println("======================================================");
                                if (resource.optString("name").equals("atcoder.jp")) {

                                    ContestBean object = new ContestBean("" + obj.optString("start"),
                                            "" + obj.optString("end"),
                                            obj.optString("event"),
                                            obj.optString("duration"),
                                            obj.optString("id"),
                                            obj.optString("href"),
                                            resource.optString("icon"),
                                            resource.optString("name"));
                                    allContestBean.add(object);
                                }
                            }

                            // TimeZone utc=TimeZone.getTimeZone("UTC");
                            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            //f.setTimeZone(utc);
                            //GregorianCalendar cal1=new GregorianCalendar(utc);
                            //GregorianCalendar cal2=new GregorianCalendar(utc);

                            Collections.sort(allContestBean, new Comparator<ContestBean>() {
                                @Override
                                public int compare(ContestBean o1, ContestBean o2) {
                                    try {
//                                        start1.setTime(f.parse(o1.getStart()));
//                                        start2.setTime(f.parse(o2.getStart()));
                                        return f.parse(o1.getStart()).compareTo(f.parse(o2.getStart()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return 0;
                                }
                            });

                            atcoder.setValue(allContestBean);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(getApplicationContext(), "Error: ", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(,"Response errorr", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", " ApiKey imshahnawaz:0145f9cdb0106581e7b89a02820f4f0e9e701456");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getCodechefData(Context context) {
        Instant date2=Instant.now();
        String ans=""+date2;
        ans=ans.substring(0,10);
        ans = ans + "T00:00:00";
        String replaceString = ans.replace(":", "%3A");

        StringRequest request = new StringRequest(Request.Method.GET, "https://clist.by:443/api/v1/contest/?start__gt="+replaceString,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("objects");
                            List<ContestBean> allContestBean = new ArrayList<ContestBean>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                JSONObject resource = obj.getJSONObject("resource");
                                //  System.out.println("======================================================");
                                if (resource.optString("name").equals("codechef.com")) {

                                    ContestBean object = new ContestBean("" + obj.optString("start"),
                                            "" + obj.optString("end"),
                                            obj.optString("event"),
                                            obj.optString("duration"),
                                            obj.optString("id"),
                                            obj.optString("href"),
                                            resource.optString("icon"),
                                            resource.optString("name"));
                                    allContestBean.add(object);
                                }
                            }

                            // TimeZone utc=TimeZone.getTimeZone("UTC");
                            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            //f.setTimeZone(utc);
                            //GregorianCalendar cal1=new GregorianCalendar(utc);
                            //GregorianCalendar cal2=new GregorianCalendar(utc);

                            Collections.sort(allContestBean, new Comparator<ContestBean>() {
                                @Override
                                public int compare(ContestBean o1, ContestBean o2) {
                                    try {
//                                        start1.setTime(f.parse(o1.getStart()));
//                                        start2.setTime(f.parse(o2.getStart()));
                                        return f.parse(o1.getStart()).compareTo(f.parse(o2.getStart()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return 0;
                                }
                            });

                            codechef.setValue(allContestBean);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(getApplicationContext(), "Error: ", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(,"Response errorr", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", " ApiKey imshahnawaz:0145f9cdb0106581e7b89a02820f4f0e9e701456");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getCodeforcesData(Context context) {
        Instant date2=Instant.now();
        String ans=""+date2;
        ans=ans.substring(0,10);
        ans = ans + "T00:00:00";
        String replaceString = ans.replace(":", "%3A");

        StringRequest request = new StringRequest(Request.Method.GET, "https://clist.by:443/api/v1/contest/?start__gt="+replaceString,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("objects");
                            List<ContestBean> allContestBean = new ArrayList<ContestBean>();
//                            TimeZone utc=TimeZone.getTimeZone("UTC");
//                            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                            f.setTimeZone(utc);
//                            GregorianCalendar cal1=new GregorianCalendar(utc);
//                            GregorianCalendar cal2=new GregorianCalendar(utc);


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                JSONObject resource = obj.getJSONObject("resource");
                                //  System.out.println("======================================================");
                                if (resource.optString("name").equals("codeforces.com")) {

                                    ContestBean object = new ContestBean("" + obj.optString("start"),
                                            "" + obj.optString("end"),
                                            obj.optString("event"),
                                            obj.optString("duration"),
                                            obj.optString("id"),
                                            obj.optString("href"),
                                            resource.optString("icon"),
                                            resource.optString("name"));
                                    allContestBean.add(object);
                                }
                            }

//                            TimeZone utc=TimeZone.getTimeZone("UTC");
                            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                            f.setTimeZone(utc);
//                            GregorianCalendar cal1=new GregorianCalendar(utc);
//                            GregorianCalendar cal2=new GregorianCalendar(utc);

                            Collections.sort(allContestBean, new Comparator<ContestBean>() {
                                @Override
                                public int compare(ContestBean o1, ContestBean o2) {
                                    try {
//                                        start1.setTime(f.parse(o1.getStart()));
//                                        start2.setTime(f.parse(o2.getStart()));
                                        return f.parse(o1.getStart()).compareTo(f.parse(o2.getStart()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return 0;
                                }
                            });

                            codeforces.setValue(allContestBean);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(getApplicationContext(), "Error: ", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(,"Response errorr", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", " ApiKey imshahnawaz:0145f9cdb0106581e7b89a02820f4f0e9e701456");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }


@RequiresApi(api = Build.VERSION_CODES.O)
public void getDataAll(Context context) {
    Instant date2=Instant.now();
    String ans=""+date2;
    ans=ans.substring(0,10);
    ans = ans + "T00:00:00";
    String replaceString = ans.replace(":", "%3A");

    StringRequest request = new StringRequest(Request.Method.GET, "https://clist.by:443/api/v1/contest/?start__gt="+replaceString,
            new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("objects");
                        List<ContestBean> allContestBean = new ArrayList<ContestBean>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            JSONObject resource = obj.getJSONObject("resource");
                            //  System.out.println("======================================================");
                            if (resource.optString("name").equals("codeforces.com") ||
                                    resource.optString("name").equals("codechef.com") ||
                                    resource.optString("name").equals("hackerrank.com") ||
                                    resource.optString("name").equals("leetcode.com") ||
                                    resource.optString("name").equals("topcoder.com") ||
                                    resource.optString("name").equals("hackerearth.com") ||
                                    resource.optString("name").equals("atcoder.jp")) {



                             ContestBean object = new ContestBean("" + obj.optString("start"),
                                        "" + obj.optString("end"),
                                        obj.optString("event"),
                                        obj.optString("duration"),
                                        obj.optString("id"),
                                        obj.optString("href"),
                                        resource.optString("icon"),
                                        resource.optString("name"));
                              //  System.out.println("#####################################################"+object.getStart());
                                allContestBean.add(object);

                            }
                        }

                        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                        Collections.sort(allContestBean, new Comparator<ContestBean>() {
                            @Override
                            public int compare(ContestBean o1, ContestBean o2) {
                                try {
                                    return f.parse(o1.getStart()).compareTo(f.parse(o2.getStart()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                return 0;
                            }
                        });


                        all.setValue(allContestBean);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //  Toast.makeText(getApplicationContext(), "Error: ", Toast.LENGTH_SHORT).show();

                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //Toast.makeText(,"Response errorr", Toast.LENGTH_SHORT).show();
        }
    })
    {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", " ApiKey imshahnawaz:0145f9cdb0106581e7b89a02820f4f0e9e701456");
            return headers;
        }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(context);
    requestQueue.add(request);
 }

}