package cn.weli.mediaplayer.utils;

import android.accounts.NetworkErrorException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtil {

    public static final String APP_CODE = "APPCODE e12c71de8dae4bc18d99d02c2fd3357e";

    /**
     * 获取指定url的信息流
     * @param url
     * @return
     */
    public static String get(String url) {
        HttpURLConnection coon = null;

        try {
            //创建一个URL对象
            URL mUrl = new URL(url);
            //调用URL的openConnection()方法，获取HttpURLConnection对象
            coon = (HttpURLConnection) mUrl.openConnection();
            //设置请求方式
            coon.setRequestMethod("GET");
            coon.setDoInput(true);
            //向header添加信息
            coon.setRequestProperty("Authorization", APP_CODE);
            coon.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            //设置读取超时5秒
            coon.setReadTimeout(5000);

            //设置链接超时为10秒
            coon.setConnectTimeout(10000);

            int resCode = coon.getResponseCode();
            if (resCode == 200) {
                InputStream is = coon.getInputStream();
                String response = getStringFromInputStream(is);
                return response;
            } else {
                throw new NetworkErrorException("response status is " + resCode);
            }

        } catch (IOException | NetworkErrorException e) {
            e.printStackTrace();
        } finally {
            if (coon != null) {
                coon.disconnect();
            }
        }

        return null;
    }

    /**
     * 获取网络图片
     * @param url
     * @return
     */
    public static Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    /**
     * 读取输入流中的数据
     * @param is
     * @return
     * @throws IOException
     */
    private static String getStringFromInputStream(InputStream is) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String str = os.toString();
        return str;

    }

}
