package dngsoftware.acerfid

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Arrays
import java.util.Objects
import kotlin.math.min
import androidx.core.graphics.get
import androidx.core.net.toUri
import androidx.core.content.edit

@SuppressLint("GetInstance")
object Utils {
    fun populateDatabase(db: FilamentDao) {
        try {
            db.addItems(Constants.defaultFilamentList)
        } catch (ignored: Exception) {
        }
    }

    fun getAllMaterials(db: FilamentDao): Array<String?>? {
        val items = db.allItems
        val materials = items?.let { arrayOfNulls<String>(it.size) }
        if (items != null) {
            for (i in items.indices) {
                materials?.set(i, items[i]?.filamentName)
            }
        }
        return materials
    }

    fun GetTemps(db: FilamentDao, materialName: String?): FilamentTemperature {
        return db.getFilamentByName(materialName)?.filamentParam!!.filamentTemperatures!!
    }

    fun GetSku(db: FilamentDao, materialName: String?): ByteArray {
        val skuData = ByteArray(20)
        Arrays.fill(skuData, 0.toByte())
        val item = db.getFilamentByName(materialName)
        val sku = item?.filamentID
        if (sku != null && !sku.isEmpty()) {
            System.arraycopy(sku.toByteArray(), 0, skuData, 0, sku.toByteArray().size)
        }
        return skuData
    }

    fun GetBrand(db: FilamentDao, materialName: String?): ByteArray {
        val brandData = ByteArray(20)
        Arrays.fill(brandData, 0.toByte())
        val item = db.getFilamentByName(materialName)
        val brand = item?.filamentVendor
        if (brand != null && !brand.isEmpty()) {
            System.arraycopy(brand.toByteArray(), 0, brandData, 0, brand.toByteArray().size)
        }
        return brandData
    }

    fun bytesToHex(data: ByteArray, space: Boolean): String {
        val sb = StringBuilder()
        for (b in data) {
            if (space) {
                sb.append(String.format("%02X ", b))
            } else {
                sb.append(String.format("%02X", b))
            }
        }
        return sb.toString()
    }

    fun getPixelColor(event: MotionEvent, picker: ImageView): Int {
        val viewX = event.x.toInt()
        val viewY = event.y.toInt()
        val viewWidth = picker.width
        val viewHeight = picker.height
        val image = (picker.drawable as BitmapDrawable).bitmap
        val imageWidth = image.width
        val imageHeight = image.height
        val imageX = (viewX.toFloat() * (imageWidth.toFloat() / viewWidth.toFloat())).toInt()
        val imageY = (viewY.toFloat() * (imageHeight.toFloat() / viewHeight.toFloat())).toInt()
        return image[imageX, imageY]
    }

    fun SetPermissions(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.NFC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val perms = arrayOf(Manifest.permission.NFC)
            val permsRequestCode = 200
            ActivityCompat.requestPermissions(context as Activity, perms, permsRequestCode)
        }
    }

    fun playBeep() {
        Thread {
            try {
                val toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 50)
                toneGenerator.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 300)
                toneGenerator.stopTone()
                toneGenerator.release()
            } catch (ignored: Exception) {
            }
        }.start()
    }

    fun numToBytes(value: Int): ByteArray? {
        return revArray(byteArrayOf((value shr 8).toByte(), value.toByte()))
    }

    fun parseNumber(byteArray: ByteArray?): Int {
        var result = 0
        for (b in revArray(byteArray)!!) {
            result = (result shl 8) or (b.toInt() and 0xFF)
        }
        return result
    }

    fun subArray(source: ByteArray?, startIndex: Int, length: Int): ByteArray? {
        if (source == null) {
            return null
        }
        val sourceLength = source.size
        if (startIndex < 0 || startIndex >= sourceLength || length <= 0) {
            return ByteArray(0)
        }
        val endIndex = min((startIndex + length).toDouble(), sourceLength.toDouble()).toInt()
        val actualLength = endIndex - startIndex
        val result = ByteArray(actualLength)
        System.arraycopy(source, startIndex, result, 0, actualLength)
        return result
    }

    fun revArray(array: ByteArray?): ByteArray? {
        if (array == null || array.size <= 1) {
            return array
        }
        var left = 0
        var right = array.size - 1
        while (left < right) {
            val temp = array[left]
            array[left] = array[right]
            array[right] = temp
            left++
            right--
        }
        return array
    }

    fun listContains(list: List<String>?, string: String?): Boolean {
        if (list == null || string == null) {
            return false
        }
        for (s in list) {
            if (s.contains(string.trim { it <= ' ' })) {
                return true
            }
        }
        return false
    }

    fun parseColor(hexString: String): ByteArray? {
        val length = hexString.length
        val byteArray = ByteArray(length / 2)
        var i = 0
        while (i < length) {
            try {
                byteArray[i / 2] = ((hexString[i].digitToIntOrNull(16)
                    ?: (-1 shl 4)) + hexString[i + 1].digitToIntOrNull(16)!!).toByte()
            } catch (e: Exception) {
                return byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0x00.toByte(), 0x00.toByte())
            }
            i += 2
        }
        return revArray(byteArray)
    }

    fun parseColor(byteArray: ByteArray?): String {
        try {
            val hexString = StringBuilder()
            for (b in revArray(byteArray)!!) {
                hexString.append(String.format("%02x", b))
            }
            return hexString.toString()
        } catch (e: Exception) {
            return "0000FF"
        }
    }

    fun openUrl(context: Context, url: String?) {
        try {
            val webpage = url?.toUri()
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            context.startActivity(intent)
        } catch (ignored: Exception) {
        }
    }

    fun dp2Px(context: Context, dipValue: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }

    fun GetSetting(context: Context, sKey: String?, sDefault: String?): String? {
        val sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        return sharedPref.getString(sKey, sDefault)
    }

    fun GetSetting(context: Context, sKey: String?, bDefault: Boolean): Boolean {
        val sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        return sharedPref.getBoolean(sKey, bDefault)
    }

    fun GetSetting(context: Context, sKey: String?, iDefault: Int): Int {
        val sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        return sharedPref.getInt(sKey, iDefault)
    }

    fun GetSetting(context: Context, sKey: String?, lDefault: Long): Long {
        val sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        return sharedPref.getLong(sKey, lDefault)
    }

    fun SaveSetting(context: Context, sKey: String?, sValue: String?) {
        val sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        sharedPref.edit() {
            putString(sKey, sValue)
        }
    }

    fun SaveSetting(context: Context, sKey: String?, bValue: Boolean) {
        val sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        sharedPref.edit() {
            putBoolean(sKey, bValue)
        }
    }

    fun SaveSetting(context: Context, sKey: String?, iValue: Int) {
        val sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        sharedPref.edit() {
            putInt(sKey, iValue)
        }
    }

    fun SaveSetting(context: Context, sKey: String?, lValue: Long) {
        val sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        sharedPref.edit() {
            putLong(sKey, lValue)
        }
    }

    fun setVendorByItem(spinner: Spinner, adapter: ArrayAdapter<String>, itemName: String) {
        for (i in 0..<adapter.count) {
            if (Objects.requireNonNull(adapter.getItem(i))?.let { itemName.startsWith(it) } == true) {
                spinner.setSelection(i)
                return
            }
        }
    }

    fun setTypeByItem(spinner: Spinner, adapter: ArrayAdapter<String>, itemName: String) {
        for (i in 0..<adapter.count) {
            if (itemName.contains(" " + Objects.requireNonNull(adapter.getItem(i)) + " ")) {
                spinner.setSelection(i)
                return
            }
        }
    }
}
