package dngsoftware.acerfid;

import static androidx.core.app.ActivityCompat.requestPermissions;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;


@SuppressLint("GetInstance")
public class Utils {

    public static String[] materialTypes = {
            "ABS",
            "ASA",
            "PETG",
            "PLA",
            "PLA+",
            "PLA Glow",
            "PLA High Speed",
            "PLA Marble",
            "PLA Matte",
            "PLA SE",
            "PLA Silk",
            "TPU"
    };

    public static String[] materialWeights = {
            "1 KG",
            "750 G",
            "600 G",
            "500 G",
            "250 G"
    };

    public static int[] GetTemps(String materialName) {
        switch (materialName) {
            case "ABS":
                return new int[]{220, 250, 90, 100};
            case "ASA":
                return new int[]{240, 280, 90, 100};
            case "PLA":
            case "PLA High Speed":
            case "PLA Glow":
                return new int[]{190, 230, 50, 60};
            case "PLA+":
                return new int[]{210, 230, 45, 60};
            case "PLA Marble":
                return new int[]{200, 230, 50, 60};
            case "PLA Matte":
            case "PLA SE":
                return new int[]{190, 230, 55, 65};
            case "PLA Silk":
                return new int[]{200, 230, 55, 65};
            case "PETG":
                return new int[]{230, 250, 70, 90};
            case "TPU":
                return new int[]{210, 230, 25, 60};
        }
        return new int[]{200, 210, 50, 60};
    }

   public static byte[] GetSku(String materialName) {
        String sku = null;
        byte[] skuData = new byte[20];
        Arrays.fill(skuData, (byte) 0);
        switch (materialName) {
            case "ABS":
                sku = "SHABBK-102";
                break;
            case "PLA High Speed":
                sku = "AHHSBK-103";
                break;
            case "PLA Matte":
                sku = "HYGBK-102";
                break;
            case "PLA Silk":
                sku = "AHSCWH-102";
                break;
            case "TPU":
                sku = "STPBK-101";
                break;
            case "PLA":
                sku = "AHPLBK-101";
                break;
            case "PLA+":
                sku = "AHPLPBK-102";
                break;
        }
        if (sku != null) {
            System.arraycopy(sku.getBytes(), 0, skuData, 0, sku.getBytes().length);
        }
        return skuData;
    }

    public static int GetMaterialLength(String materialWeight) {
        switch (materialWeight) {
            case "1 KG":
                return 330;
            case "750 G":
                return 247;
            case "600 G":
                return 198;
            case "500 G":
                return 165;
            case "250 G":
                return 82;
        }
        return 330;
    }

    public static String GetMaterialWeight(int materialLength) {
        switch (materialLength) {
            case 330:
                return "1 KG";
            case 247:
                return "750 G";
            case 198:
                return "600 G";
            case 165:
                return "500 G";
            case 82:
                return "250 G";
        }
        return "1 KG";
    }

    public static boolean canMfc(Context context) {
        FeatureInfo[] info = context.getPackageManager().getSystemAvailableFeatures();
        for (FeatureInfo i : info) {
            String name = i.name;
            if (name != null && name.equals("com.nxp.mifare")) {
                return true;
            }
        }
        return false;
    }

    public static String bytesToHex(byte[] data, boolean space) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            if (space) {
                sb.append(String.format("%02X ", b));
            }else {
                sb.append(String.format("%02X", b));
            }
        }
        return sb.toString();
    }

    public static int getPixelColor(MotionEvent event, ImageView picker) {
        int viewX = (int) event.getX();
        int viewY = (int) event.getY();
        int viewWidth = picker.getWidth();
        int viewHeight = picker.getHeight();
        Bitmap image = ((BitmapDrawable) picker.getDrawable()).getBitmap();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int imageX = (int) ((float) viewX * ((float) imageWidth / (float) viewWidth));
        int imageY = (int) ((float) viewY * ((float) imageHeight / (float) viewHeight));
        return image.getPixel(imageX, imageY);
    }

    public static void SetPermissions(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED) {
            String[] perms = {Manifest.permission.NFC};
            int permsRequestCode = 200;
            requestPermissions((Activity) context, perms, permsRequestCode);
        }
    }

    public static void playBeep() {
        new Thread(() -> {
            try {
                ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 50);
                toneGenerator.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 300);
                toneGenerator.stopTone();
                toneGenerator.release();
            } catch (Exception ignored) {
            }
        }).start();
    }

    public static byte[] numToBytes(int value) {
        return revArray(new byte[]{(byte) (value >> 8), (byte) value});
    }

    public static int parseNumber(byte[] byteArray) {
        int result = 0;
        for (byte b : revArray(byteArray)) {
            result = (result << 8) | (b & 0xFF);
        }
       return result;
    }

    public static byte[] subArray(byte[] source, int startIndex, int length) {
        if (source == null) {
            return null;
        }
        int sourceLength = source.length;
        if (startIndex < 0 || startIndex >= sourceLength || length <= 0) {
            return new byte[0]; // Return empty byte array for invalid input
        }
        int endIndex = Math.min(startIndex + length, sourceLength);
        int actualLength = endIndex - startIndex;
        byte[] result = new byte[actualLength];
        System.arraycopy(source, startIndex, result, 0, actualLength);
        return result;
    }

    public static byte[] revArray(byte[] array) {
        if (array == null || array.length <= 1) {
            return array;
        }
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            byte temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
        return array;
    }

    public static byte[] parseColor(final String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            try {
                byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                        + Character.digit(hexString.charAt(i + 1), 16));
            } catch (Exception e) {
                return new byte[] {(byte)0xFF, (byte)0xFF, (byte)0x00, (byte)0x00};
            }
        }
        return revArray(byteArray);
    }

    public static String parseColor(byte[] byteArray) {
        try {
            StringBuilder hexString = new StringBuilder();
            for (byte b : revArray(byteArray)) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            return "0000FF";
        }
    }

    public static float dp2Px(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static String GetSetting(Context context, String sKey, String sDefault) {
        SharedPreferences sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sharedPref.getString(sKey, sDefault);
    }

    public static boolean GetSetting(Context context, String sKey, boolean bDefault) {
        SharedPreferences sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sharedPref.getBoolean(sKey, bDefault);
    }

    public static int GetSetting(Context context, String sKey, int iDefault) {
        SharedPreferences sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sharedPref.getInt(sKey, iDefault);
    }

    public static long GetSetting(Context context, String sKey, long lDefault) {
        SharedPreferences sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sharedPref.getLong(sKey, lDefault);
    }

    public static void SaveSetting(Context context, String sKey, String sValue) {
        SharedPreferences sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(sKey, sValue);
        editor.apply();
    }

    public static void SaveSetting(Context context, String sKey, boolean bValue) {
        SharedPreferences sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(sKey, bValue);
        editor.apply();
    }

    public static void SaveSetting(Context context, String sKey, int iValue) {
        SharedPreferences sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(sKey, iValue);
        editor.apply();
    }

    public static void SaveSetting(Context context, String sKey, long lValue) {
        SharedPreferences sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(sKey, lValue);
        editor.apply();
    }
}