package com.dr.screen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateActivity extends Activity {

    private String sd_path = "storage/emulated/0/Screen";
    private String api_url = "http://10.69.5.195:8088/iepg/getScreenSaver?queryType=210";
    private String[] urls;
    private int count;

    private String[] test = new String[]{
            "http://10.69.5.195:8062/oms-pic/special/common/188/2100011472.jpg",
            "http://10.69.5.195:8062/oms-pic/special/common/176/2100011479.jpg",
            "http://10.69.5.195:8062/oms-pic/special/common/125/2100011478.jpg",
            "http://10.69.5.195:8062/oms-pic/special/common/124/2100011477.jpg",
            "http://10.69.5.195:8062/oms-pic/special/common/067/2100011475.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int m;
                downloader_json(api_url,sd_path);
                parse_json();
                /*get_url();*/
                for(m = 0; m < urls.length; m++){
                    if(urls[m] != null){
                        downloader_pic(urls[m],sd_path,m);
                    }
                }
                dialog();
                /*for(m = 0; m < urls.length; m++){
                    if(urls[m] != null){
                        DownLoaderHelp downLoaderHelp = new DownLoaderHelp(handler);
                        downLoaderHelp.downLoadFile(urls[m]);
                    }
                }*/
                Intent startIntent = new Intent(UpdateActivity.this, ScreenActivity.class);
                startActivity(startIntent);
            }
        }).start();

    }


    private void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setMessage("壁纸更新完成");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }





    public void downloader_pic(String image_url, String path, int i) {
        URL url;
        File file = new File(path+"/"+i+".jpg");
        try {
            url = new URL(image_url);
            InputStream inputStream = url.openStream();
            inputStream.close();
            inputStream = url.openStream();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            int hasRead;
            while((hasRead = inputStream.read()) != -1){
                fileOutputStream.write(hasRead);
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloader_json(String api_url, String path) {
        URL url;
        File file = new File(path+"/Json");
        if(!file.exists()){
            file.mkdir();
        }
        try {
            url = new URL(api_url);
            InputStream inputStream = url.openStream();
            inputStream.close();
            inputStream = url.openStream();
            FileOutputStream fileOutputStream = new FileOutputStream(file+"/Json.txt");
            int hasRead;
            while((hasRead = inputStream.read()) != -1){
                fileOutputStream.write(hasRead);
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parse_json() {
        File mFile = new File(sd_path+"/Json/Json.txt");
        String mContent = "";
        int i;
        if(mFile.exists()){
            try{
                InputStream mInputStream = new FileInputStream(mFile);
                InputStreamReader mInputReader = new InputStreamReader(mInputStream);
                BufferedReader mBufferReader = new BufferedReader(mInputReader);
                String mLine;
                while ((mLine = mBufferReader.readLine()) != null){
                    mContent += mLine;
                }
                mInputStream.close();
                try{
                    /*//TEST
                    File file1 = new File(sd_path+"/Json/test.txt");
                    PrintStream ps = new PrintStream(new FileOutputStream(file1));*/

                    JSONTokener mJT = new JSONTokener(mContent);
                    JSONObject mJo = (JSONObject)mJT.nextValue();
                    JSONArray _mSaverList = mJo.getJSONArray("screenSaverList");
                    urls = new String[_mSaverList.length()];


                    for(i = 0; i < _mSaverList.length(); i++) {
                        JSONObject mJoo = _mSaverList.getJSONObject(i);
                        Object _mJa = mJoo.get("posterList");
                        if (_mJa instanceof JSONArray) {
                            JSONArray mJa = (JSONArray) _mJa;
                            JSONObject mJooo = mJa.getJSONObject(0);
                            urls[i] = mJooo.getString("LocalPath");
                            /*ps.append(mJooo.getString("LocalPath"));*/
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    public void get_url() {
        File file = new File(sd_path+"/Url");
        String mContent = "";
        int i;
        if(!file.exists()){
            file.mkdir();
        }
        try{
            URL url = new URL(api_url);
            InputStream inputStream = url.openStream();
            InputStreamReader mInputReader = new InputStreamReader(inputStream);
            BufferedReader mBufferReader = new BufferedReader(mInputReader);
            String mLine;
            while ((mLine = mBufferReader.readLine()) != null){
                mContent += mLine;
            }
            inputStream.close();
            try{
                JSONTokener mJT = new JSONTokener(mContent);
                JSONObject mJo = (JSONObject)mJT.nextValue();
                JSONArray _mSaverList = mJo.getJSONArray("screenSaverList");
                urls = new String[_mSaverList.length()];
                for(i = 0; i < _mSaverList.length(); i++) {
                    JSONObject mJoo = _mSaverList.getJSONObject(i);
                    Object _mJa = mJoo.get("posterList");
                    if (_mJa instanceof JSONArray) {
                        JSONArray mJa = (JSONArray) _mJa;
                        JSONObject mJooo = mJa.getJSONObject(0);
                        urls[i] = mJooo.getString("LocalPath");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
