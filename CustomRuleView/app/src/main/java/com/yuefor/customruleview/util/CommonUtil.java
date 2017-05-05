package com.yuefor.customruleview.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.yuefor.customruleview.CustomApplication;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Matteo
 * @time 2014-3-19
 */
public class CommonUtil {

    private static long lastTime;
    private static Toast mToast;
    private static int errorNum;
    private static int errorMaxNum = 2;





    public static String object2String(Object object) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(object);
            so.flush();
            return new String(Base64.encode(bo.toByteArray(), 0));
        } catch (Exception e) {
            return null;
        }
    }

    public static Object string2Object(String string) {
        try {
            byte b[] = Base64.decode(string, 0);
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            return si.readObject();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 获取整块屏幕实时尺寸
     *
     * @param context
     * @return samples:
     * DisplayMetrics.widthPixels, DisplayMetrics.heightPixels
     * DisplayMetrics.xdpi, DisplayMetrics.ydpi
     */
    public static DisplayMetrics displayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = CustomApplication.getGlobalContext().getResources()
                .getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = CustomApplication.getGlobalContext().getResources()
                .getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2dip(int pxValue) {
        final float scale = CustomApplication.getGlobalContext().getResources()
                .getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getActionBarIconSize() {
        return dip2px(36);
    }

    public static File zoomImageFileByLength(String path, double maxSize) throws FileNotFoundException, OutOfMemoryError {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        bitmap = zoomBitmapByLengh(bitmap, maxSize);
        File file = new File(CustomApplication.getGlobalContext().getCacheDir(),
                String.valueOf(System.currentTimeMillis()));
        OutputStream os = new FileOutputStream(file);
        bitmap.compress(CompressFormat.PNG, 100, os);
        return file;
    }

    public static Bitmap zoomBitmapByLengh(Bitmap bitmap, double maxSize) throws OutOfMemoryError {
        // 图片允许最大空间 单位：KB
        // 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            // 获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            // 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
            // （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitmap = zoomBitmap(bitmap, bitmap.getWidth() / Math.sqrt(i),
                    bitmap.getHeight() / Math.sqrt(i));
        }
        return bitmap;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, double newWidth,
                                    double newHeight) throws OutOfMemoryError {
        // 获取这个图片的宽和高
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height,
                matrix, true);
        return bitmap;
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }









    private static String intToIp(int i) {

        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    private static String getGPRSLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "0.0.0.0";
    }

    public static String getNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        try {
            //http://iframe.ip138.com/ic.asp
            //infoUrl = new URL("http://city.ip138.com/city0.asp");
            infoUrl = new URL("http://iframe.ip138.com/ic.asp");
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");
                inStream.close();
                //从反馈的结果中提取出IP地址
                int start = strber.indexOf("[");
                int end = strber.indexOf("]", start + 1);
                line = strber.substring(start + 1, end);
                return line;
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String readStrFromFile(File file) throws IOException {
        if (file == null || !file.exists())
            return "";
        String str;
        FileInputStream in = new FileInputStream(file);
        // size  为字串的长度 ，这里一次性读完
        int size = in.available();
        byte[] buffer = new byte[size];
        in.read(buffer);
        in.close();
        str = new String(buffer, "UTF-8");
        return str;
    }

    /**
     * 防止多次点击
     */
    public static boolean isFastDouleClick() {
        long currentTime = System.currentTimeMillis();
        if ((Math.abs(currentTime - lastTime)) < 500) {
            lastTime = currentTime;
            return true;
        }
        lastTime = currentTime;
        return false;
    }

    public static boolean isFastDouleClick(int time) {
        long currentTime = System.currentTimeMillis();
        if ((Math.abs(currentTime - lastTime)) < time) {
            lastTime = currentTime;
            return true;
        }
        lastTime = currentTime;
        return false;
    }

    public static List<? extends Serializable> getRandomList(List<? extends Serializable> list) {
        List<Serializable> randomList = new ArrayList<>();
        int[] sortNum = randomCommon(1, list.size() + 1, list.size());
        for (int i : sortNum) {
            randomList.add(list.get(i - 1));
        }
        return randomList;
    }

    /**
     * 随机指定范围内N个不重复的数
     *
     * @param min 指定范围最小值  不能为0
     * @param max 指定范围最大值  无法达到最大值
     * @param n   随机数个数  (n <= max - min)
     * @return 不包含0的数组
     */
    private static int[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
            return true;
        }

        return false;
    }

    //获取手机状态栏高度
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static String getShowTime(long showTime) {
        try {
            return getShowTime(showTime, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 时间规则：
     * 按照时间排序，当天显示具体时间，如12：30 ；
     * 隔天显示昨天；
     * 隔五天内显示星期几，再往上直接显示15/7/30
     *
     * @param type 0 评论item显示时间, 1 推荐好友item显示时间
     */
    public static String getShowTime(long showTime, int type) {
        String[] weekStr = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        long nowTime = System.currentTimeMillis();
        Date showDate = new Date(showTime);
        Date nowDate = new Date(nowTime);
        Calendar showCalendar = Calendar.getInstance();
        Calendar nowCalendar = Calendar.getInstance();
        showCalendar.setTime(showDate);
        nowCalendar.setTime(nowDate);

        int showYear = showCalendar.get(Calendar.YEAR);
        int showMonth = showCalendar.get(Calendar.MONTH) + 1;
        int showDayOfMonth = showCalendar.get(Calendar.DAY_OF_MONTH);
        int showDayOfWeek = showCalendar.get(Calendar.DAY_OF_WEEK) - 1;

        int nowYear = nowCalendar.get(Calendar.YEAR);
        int nowMonth = nowCalendar.get(Calendar.MONTH) + 1;
        int nowDayOfMonth = nowCalendar.get(Calendar.DAY_OF_MONTH);

        if (showYear == nowYear) {
            if (showMonth == nowMonth) {
                if (showDayOfMonth == nowDayOfMonth) {
                    int showHour = showCalendar.get(Calendar.HOUR_OF_DAY);
                    int showMinute = showCalendar.get(Calendar.MINUTE);

                    int nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY);
                    int nowMinute = nowCalendar.get(Calendar.MINUTE);

                    String hourStr;
                    String minuteStr;

                    if (showHour == 0) {
                        hourStr = "00";
                    } else if (showHour < 10) {
                        hourStr = "0" + showHour;
                    } else {
                        hourStr = String.valueOf(showHour);
                    }

                    if (showMinute == 0) {
                        minuteStr = "00";
                    } else if (showMinute < 10) {
                        minuteStr = "0" + showMinute;
                    } else {
                        minuteStr = String.valueOf(showMinute);
                    }
                    String returnTime = "";
                    switch (type) {
                        case 0:
                            returnTime = hourStr + ":" + minuteStr;
                            break;
                        case 1:
                            int durHour = nowHour - showHour;
                            int durMinute = nowMinute - showMinute;
                            if (durHour >= 1) {
                                returnTime = durHour + "小时前";
                            } else {
                                returnTime = Math.abs(durMinute) + durMinute + "分钟前";
                            }
                            break;
                        default:
                            break;
                    }
                    return returnTime;
                } else {
                    //未来的时间 大于一天 直接显示年月日
                    if ((nowTime - showTime) > 0) {
                        int btwDay = Math.abs(nowDayOfMonth - showDayOfMonth);
                        if (btwDay < 2) {
                            return "昨天";
                        } else if (btwDay < 6) {
                            return weekStr[showDayOfWeek];
                        } else {
                            return showYear + "/" + showMonth + "/" + showDayOfMonth;
                        }
                    } else {
                        return showYear + "/" + showMonth + "/" + showDayOfMonth;
                    }
                }
            } else {
                //未来的时间 大于一天 直接显示年月日
                if ((nowTime - showTime) > 0) {
                    int showHour = showCalendar.get(Calendar.HOUR_OF_DAY);
                    int showMinute = showCalendar.get(Calendar.MINUTE);

                    int nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY);
                    int nowMinute = nowCalendar.get(Calendar.MINUTE);

                    long btwTime = Math.abs((nowTime - nowHour * 1000 * 3600 - nowMinute * 1000 * 60)
                            - (showTime - showHour * 1000 * 3600 - showMinute * 1000 * 60));
                    int day = (int) (btwTime / 1000 / 3600 / 24);
                    if (day < 2) {
                        return "昨天";
                    } else if (day < 6) {
                        return weekStr[showDayOfWeek];
                    } else {
                        return showYear + "/" + showMonth + "/" + showDayOfMonth;
                    }
                } else {
                    return showYear + "/" + showMonth + "/" + showDayOfMonth;
                }
            }
        } else {
            return showYear + "/" + showMonth + "/" + showDayOfMonth;
        }
    }

    /**
     * 获取显示的日期，如果是今年则显示某月某日，否则显示某年某月某日
     *
     * @param showTime
     * @return
     */
    public static String getShowDate(long showTime) {
        Calendar showCalendar = Calendar.getInstance();
        showCalendar.setTimeInMillis(showTime);
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTimeInMillis(System.currentTimeMillis());

        String format = "";
        if (showCalendar.get(Calendar.YEAR) == nowCalendar.get(Calendar.YEAR)) {
            format = "M月dd日";
        } else {
            format = "yyyy年M月dd日";
        }
        return new SimpleDateFormat(format).format(new Date(showTime));
    }

    public static String saveLogFile(String logString) {

        StringBuffer sb = new StringBuffer();
        sb.append(logString);
        try {
            String fileName = "event" + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath()
                        + File.separator
                        + CustomApplication.getGlobalContext().getPackageName();
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                StringBuffer result = new StringBuffer();
                InputStreamReader reader = null;
                try {
                    reader = new InputStreamReader(new FileInputStream(path + File.separator + fileName));
                    BufferedReader br = new BufferedReader(reader);
                    String line;
                    while ((line = br.readLine()) != null) {
                        result.append(line + "\n");
                    }
                } catch (Exception e) {
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }

                result = result.append(sb);
                FileOutputStream fos = new FileOutputStream(path + File.separator + fileName);

                fos.write(result.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 缩掉输入法
     */
    public static void dismisInputMethod(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isOpen = imm.isActive();
            if (isOpen) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager == null) {
                    return;
                }
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 弹出输入法
     */
    public static void showInputMethod(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static String getShowPlayNum(int startNum) {
        String playNumStr = "";
        if (startNum < 10000) {
            playNumStr = String.valueOf(startNum + "人");
        } else if (startNum < 99999) {
            int numFirst = startNum / 10000;
            int numSecond = (startNum - 10000 * numFirst) / 1000;
            playNumStr = String.valueOf(numFirst + "." + numSecond + "万人");
        } else if (startNum < 99999999) {
            int num = startNum / 10000;
            playNumStr = String.valueOf(num + "万人");
        } else {
            int numFirst = startNum / 100000000;
            int numSecond = (startNum - 100000000 * numFirst) / 10000000;
            playNumStr = String.valueOf(numFirst + "." + numSecond + "亿人");
        }

        return playNumStr;
    }

    public static String getShowNum(int number) {
        String playNumStr = "";
        if (number < 10000) {
            playNumStr = String.valueOf(number);
        } else if (number < 99999) {
            int numFirst = number / 10000;
            int numSecond = (number - 10000 * numFirst) / 1000;
            playNumStr = String.valueOf(numFirst + "." + numSecond);
        } else if (number < 99999999) {
            int num = number / 10000;
            playNumStr = String.valueOf(num);
        } else {
            int numFirst = number / 100000000;
            int numSecond = (number - 100000000 * numFirst) / 10000000;
            playNumStr = String.valueOf(numFirst + "." + numSecond);
        }

        return playNumStr;
    }

}