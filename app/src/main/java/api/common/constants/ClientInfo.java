package api.common.constants;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 终端信息
 */
public class ClientInfo {
    public final static int NONET = 0;
    public final static int MOBILE_3G = 1;
    public final static int MOBILE_2G = 2;
    public final static int WIFI = 3;
    // 中国大陆三大运营商imei
    private static final String CHA_IMSI = "46003";
    private static final String CMCC_IMSI_1 = "46000";
    private static final String CMCC_IMSI_2 = "46002";
    private static final String CHU_IMSI = "46001";

    // 中国大陆三大运营商 provider
    private static final String CMCC = "中国移动";
    private static final String CHU = "中国联通";
    private static final String CHA = "中国电信";

    // 未知内容
    public static final String UNKNOWN = "unknown";

    private static ClientInfo instance;

    // 包名
    public String packageName = null;
    // 安卓系统版本号
    public String androidVer = null;
    // 版本名
    public String apkVerName = null;
    // 版本号
    public int apkVerCode = 1;
    // cpu型号
    public String cpu = null;
    // 厂商
    public String manufacturer = null;
    // 机型
    public String productModel = null;
    // imei
    public String imei = null;
    // imsi
    public String imsi = null;
    // uuid
    public UUID deviceUuids;
    // 运营提供商
    public String provider = null;
    // 网络状态， 网络状态会不停变化，故设置成static，需实时更新
    public byte networkType;

    // ram大小
    public int ramSize = 0;
    // rom大小
    public int romSize = 0;
    // 屏幕大小
    public String screenSize = null;
    public int screenWidth = 0;
    public int screenHeight = 0;
    // 屏幕的dpi
    public short dpi = 0;
    // mac地址
    public String mac = null;
    // 基带版本
    public String baseband = "unknow";
    // 内核版本
    public String kernel;
    // rom 版本
    public String romVer;

    // 终端编号
    public String terminalCode;

    private ClientInfo(Context context) {
        packageName = context.getPackageName();

        androidVer = Build.VERSION.RELEASE;

        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0);
            apkVerName = packageInfo.versionName;
            apkVerCode = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
        }
        cpu = getCpuInfo();
        manufacturer = Build.MANUFACTURER;// 手机厂商
        productModel = Build.MODEL;// 手机型号

        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
        imsi = telephonyManager.getSubscriberId();

        if (TextUtils.isEmpty(imei)) {
            imei = UNKNOWN;
        }
        if (TextUtils.isEmpty(imsi)) {
            imsi = UNKNOWN;
        }
        String tmSerial = "" + telephonyManager.getSimSerialNumber();
        String androidId = ""
                + android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        deviceUuids = new UUID(androidId.hashCode(),
                ((long) imei.hashCode() << 32) | tmSerial.hashCode());
        provider = getProvider(imsi);

        resetNetWorkType(context);

        ramSize = getTotalMemory(context);

        romSize = (int) ((getTotalInternalMemorySize() / 1024) / 1024);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (width > height) {
            screenWidth = height;
            screenHeight = width;
        } else {
            screenWidth = width;
            screenHeight = height;
        }
        screenSize = screenWidth + "*" + screenHeight;
        dpi = (short) dm.densityDpi;

        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        mac = wifiInfo.getMacAddress();
        if (mac == null) {
            mac = UNKNOWN;
        }
        baseband = getBaseBandVersion();
        kernel = getKernelVersion();
        romVer = Build.DISPLAY;
    }

    private String getBaseBandVersion() {
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[]{String.class,
                    String.class});
            Object result = m.invoke(invoker, new Object[]{
                    "gsm.version.baseband", "unknow"});
            return (String) result;
        } catch (Exception e) {
        }
        return "unknow";
    }

    private String getKernelVersion() {
        String kernelVersion = "";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("/proc/version");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return kernelVersion;
        }
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream), 8 * 1024);
        String info = "";
        String line = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                info += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            if (info != "") {
                final String keyword = "version ";
                int index = info.indexOf(keyword);
                line = info.substring(index + keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return kernelVersion;
    }

    /**
     * 取Rom Size
     *
     * @return
     */
    private long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    // 获取IMSI号的供应商
    private String getProvider(String imsi) {

        String provider = UNKNOWN; // 当前sim卡运营商 //3为未知的 或者没有sim卡的比如平板
        if (imsi != null) {
            if (imsi.startsWith(CMCC_IMSI_1) || imsi.startsWith(CMCC_IMSI_2)) {// 中国移动
                provider = CMCC;
            } else if (imsi.startsWith(CHU_IMSI)) {// 中国联通
                provider = CHU;
            } else if (imsi.startsWith(CHA_IMSI)) {// 中国电信
                provider = CHA;
            }
        }
        return provider;
    }

    // 获取手机总内存
    private int getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        int initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            if (localBufferedReader != null) {
                str2 = localBufferedReader.readLine();
                if (str2 != null) {
                    arrayOfString = str2.split("\\s+");
                    initial_memory = Integer.valueOf(arrayOfString[1])
                            .intValue();// KB
                    localBufferedReader.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return initial_memory / 1024; // MB
    }

    /**
     * 可用内存
     *
     * @param context
     * @return
     */
    public int getAvailMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存

        // return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        return (int) (mi.availMem / (1024 * 1024));
    }

    private static int check2GOr3GNet(Context context) {

        int mobileNetType = NONET;
        if (null == context) {
            return mobileNetType;
        }
        TelephonyManager telMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        int netWorkType = telMgr.getNetworkType();
        switch (netWorkType) {
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                // case TelephonyManager.NETWORK_TYPE_EVDO_B:
                mobileNetType = MOBILE_3G;
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            case TelephonyManager.NETWORK_TYPE_IDEN:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
                mobileNetType = MOBILE_2G;
                break;
            default:
                mobileNetType = MOBILE_3G;
                break;
        }

        return mobileNetType;

    }

    // 获取当前网络状态
    public int resetNetWorkType(Context context) {
        int netType = NONET;
        networkType = NONET;

        if (null == context) {
            return netType;
        }
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || (networkInfo.getState() != State.CONNECTED)) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            netType = check2GOr3GNet(context);
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = WIFI;
        } else {
            boolean b = ConnectivityManager.isNetworkTypeValid(nType);
            if (b) {
                netType = MOBILE_3G; // 联通3G就跑这里
            }
        }
        networkType = (byte) netType;
        return netType;
    }

    /**
     * 取cpu 信息
     *
     * @return
     */
    private String getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};
        String[] arrayOfString;
        String ret = null;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            if (null != str2) {
                arrayOfString = str2.split("\\s+");
                for (int i = 2; i < arrayOfString.length; i++) {
                    cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
                }
            }

            str2 = localBufferedReader.readLine();
            if (null != str2) {
                arrayOfString = str2.split("\\s+");
                cpuInfo[1] += arrayOfString[2];
            }

            localBufferedReader.close();
        } catch (IOException e) {
        }
        ret = cpuInfo[0];
        return ret;
    }

    public static ClientInfo getInstance(Context context) {
        if (null == instance) {
            instance = new ClientInfo(context);
        }
        return instance;
    }

    //获取屏幕比例
    public static float getScreenRatio(Context context) {
        instance=getInstance(context);
        float ratio = (float) instance.screenWidth / (float)instance.screenHeight;
        return ratio;
    }


}
