package com.example.testuuid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Utils
{

    public Utils()
    {
    }

    @SuppressLint("NewApi")
	public static final String generateUUID(Context context)
    {
        String s = ((TelephonyManager)context.getSystemService("phone")).getDeviceId();
        if(s == null)
            s = "";
        String s1 = android.provider.Settings.Secure.getString(context.getContentResolver(), "android_id");
        if(s1 == null)
            s1 = "";
        String s2;
        String s3;
        WifiInfo wifiinfo;
        String s4;
        if(android.os.Build.VERSION.SDK_INT >= 9)
        {
            s2 = Build.SERIAL;
            if(s2 == null)
                s2 = "";
        } else
        {
            s2 = getDeviceSerial();
        }
        s3 = "";
        wifiinfo = ((WifiManager)context.getSystemService("wifi")).getConnectionInfo();
        if(wifiinfo != null)
        {
            s3 = wifiinfo.getMacAddress();
            if(s3 == null)
                s3 = "";
        }
        try
        {
            s4 = getMD5String((new StringBuilder()).append(s).append(s1).append(s2).append(s3).toString());
        }
        catch(NoSuchAlgorithmException nosuchalgorithmexception)
        {
            nosuchalgorithmexception.printStackTrace();
            return null;
        }
        return s4;
    }

    public static final String getDeviceSerial()
    {
        String s;
        try
        {
            Method method = Class.forName("android.os.Build").getDeclaredMethod("getString", new Class[] {
                Class.forName("java.lang.String")
            });
            if(!method.isAccessible())
                method.setAccessible(true);
            s = (String)method.invoke(new Build(), new Object[] {
                "ro.serialno"
            });
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            classnotfoundexception.printStackTrace();
            return "";
        }
        catch(NoSuchMethodException nosuchmethodexception)
        {
            nosuchmethodexception.printStackTrace();
            return "";
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            invocationtargetexception.printStackTrace();
            return "";
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            illegalaccessexception.printStackTrace();
            return "";
        }
        return s;
    }

    private static final String getMD5String(String s)
        throws NoSuchAlgorithmException
    {
        byte abyte0[] = MessageDigest.getInstance("SHA-1").digest(s.getBytes());
        Formatter formatter = new Formatter();
        int i = abyte0.length;
        for(int j = 0; j < i; j++)
        {
            byte byte0 = abyte0[j];
            Object aobj[] = new Object[1];
            aobj[0] = Byte.valueOf(byte0);
            formatter.format("%02x", aobj);
        }

        return formatter.toString();
    }
}