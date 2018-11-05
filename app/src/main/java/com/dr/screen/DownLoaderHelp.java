package com.dr.screen;

import android.os.Handler;
import android.os.Message;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DownLoaderHelp {
    //创建线程池
    private Executor threadPool = Executors.newFixedThreadPool(3);
    private Handler handler;
    private String sd_path = "storage/emulated/0/Screen";


    public DownLoaderHelp(Handler handler){
        this.handler = handler;
    }

    static class downRunnable implements Runnable{
        private String url;
        private String filename;
        private long start;
        private long end;
        private Handler handler;

        public downRunnable(String url, String filename, long start, long end, Handler handler) {
            this.url = url;
            this.filename = filename;
            this.start = start;
            this.end = end;
            this.handler = handler;
        }

        @Override
        public void run() {
            HttpURLConnection con = null;
            try{
                URL urls = new URL(url);
                con = (HttpURLConnection) urls.openConnection();
                con.setRequestMethod("GET");
                con.setReadTimeout(5000);
                con.setRequestProperty("Range","bytes="+start+"-"+end);

                RandomAccessFile accessFile = new RandomAccessFile(new File(filename),"rwd");
                accessFile.seek(start);

                InputStream is = con.getInputStream();
                byte bt[] = new byte[4 * 1024];
                int len = 0;
                while((len = is.read(bt)) != -1){
                    accessFile.write(bt,0,len);
                }
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                if(accessFile != null){
                    accessFile.close();
                }
                if(is != null){
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (con != null){
                    con.disconnect();
                }
            }
        }
    }

    public void downLoadFile(String purl){
        HttpURLConnection con = null;
        try{
            URL url = new URL(purl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(5000);

            long length = con.getContentLength();
            long block = length / 3;

            String fileName = getFileName(purl);
            File downLoadFile = new File(sd_path,fileName);

            for (int i = 0; i < 3; i++) {
                long start = i * block;
                long end = (i + 1) * block - 1;
                if (i == 2) {
                    end = length;
                }

                downRunnable runnable = new downRunnable(purl, downLoadFile.getAbsolutePath(), start, end, handler);
                threadPool.execute(runnable);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (con != null){
                con.disconnect();
            }
        }
    }
    private String getFileName(String url){
        return url.substring(url.lastIndexOf("/"+1));
    }
}
