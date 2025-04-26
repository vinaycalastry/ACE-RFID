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
    var materialWeights: Array<String> = arrayOf(
        "1 KG",
        "750 G",
        "600 G",
        "500 G",
        "250 G"
    )

    fun GetMaterialLength(materialWeight: String): Int {
        when (materialWeight) {
            "1 KG" -> return 330
            "750 G" -> return 247
            "600 G" -> return 198
            "500 G" -> return 165
            "250 G" -> return 82
        }
        return 330
    }

    fun GetMaterialWeight(materialLength: Int): String {
        when (materialLength) {
            330 -> return "1 KG"
            247 -> return "750 G"
            198 -> return "600 G"
            165 -> return "500 G"
            82 -> return "250 G"
        }
        return "1 KG"
    }

    fun populateDatabase(db: FilamentDao) {
        try {
            var filament = Filament()
            filament.position = 0
            filament.filamentID = "SHABBK-102"
            filament.filamentName = "ABS"
            filament.filamentVendor = "AC"
            filament.filamentParam = "220|250|90|100"
            db.addItem(filament)

            filament = Filament()
            filament.position = db.itemCount
            filament.filamentID = ""
            filament.filamentName = "ASA"
            filament.filamentVendor = ""
            filament.filamentParam = "240|280|90|100"
            db.addItem(filament)

            filament = Filament()
            filament.position = db.itemCount
            filament.filamentID = ""
            filament.filamentName = "PETG"
            filament.filamentVendor = ""
            filament.filamentParam = "230|250|70|90"
            db.addItem(filament)

            filament = Filament()
            filament.position = db.itemCount
            filament.filamentID = "AHPLBK-101"
            filament.filamentName = "PLA"
            filament.filamentVendor = "AC"
            filament.filamentParam = "190|230|50|60"
            db.addItem(filament)

            filament = Filament()
            filament.position = db.itemCount
            filament.filamentID = "AHPLPBK-102"
            filament.filamentName = "PLA+"
            filament.filamentVendor = "AC"
            filament.filamentParam = "210|230|45|60"
            db.addItem(filament)

            filament = Filament()
            filament.position = db.itemCount
            filament.filamentID = ""
            filament.filamentName = "PLA Glow"
            filament.filamentVendor = ""
            filament.filamentParam = "190|230|50|60"
            db.addItem(filament)

            filament = Filament()
            filament.position = db.itemCount
            filament.filamentID = "AHHSBK-103"
            filament.filamentName = "PLA High Speed"
            filament.filamentVendor = "AC"
            filament.filamentParam = "190|230|50|60"
            db.addItem(filament)

            filament = Filament()
            filament.position = db.itemCount
            filament.filamentID = ""
            filament.filamentName = "PLA Marble"
            filament.filamentVendor = ""
            filament.filamentParam = "200|230|50|60"
            db.addItem(filament)

            filament = Filament()
            filament.position = db.itemCount
            filament.filamentID = "HYGBK-102"
            filament.filamentName = "PLA Matte"
            filament.filamentVendor = "AC"
            filament.filamentParam = "190|230|55|65"
            db.addItem(filament)

            filament = Filament()
            filament.position = db.itemCount
            filament.filamentID = ""
            filament.filamentName = "PLA SE"
            filament.filamentVendor = ""
            filament.filamentParam = "190|230|55|65"
            db.addItem(filament)

            filament = Filament()
            filament.position = db.itemCount
            filament.filamentID = "AHSCWH-102"
            filament.filamentName = "PLA Silk"
            filament.filamentVendor = "AC"
            filament.filamentParam = "200|230|55|65"
            db.addItem(filament)

            filament = Filament()
            filament.position = db.itemCount
            filament.filamentID = "STPBK-101"
            filament.filamentName = "TPU"
            filament.filamentVendor = "AC"
            filament.filamentParam = "210|230|25|60"
            db.addItem(filament)
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

    fun GetTemps(db: FilamentDao, materialName: String?): IntArray {
        val item = db.getFilamentByName(materialName)
        val temps = item?.filamentParam!!.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val tempArray = IntArray(temps.size)
        for (i in temps.indices) {
            try {
                tempArray[i] = temps[i].trim { it <= ' ' }.toInt()
            } catch (ignored: Exception) {
                return intArrayOf(200, 210, 50, 60)
            }
        }
        return tempArray
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

    fun arrayContains(array: Array<String>?, string: String?): Boolean {
        if (array == null || string == null) {
            return false
        }
        for (s in array) {
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

    var filamentVendors: Array<String> = arrayOf(
        "3Dgenius",
        "3DJake",
        "3DXTECH",
        "3D BEST-Q",
        "3D Hero",
        "3D-Fuel",
        "Aceaddity",
        "AddNorth",
        "Amazon Basics",
        "AMOLEN",
        "Ankermake",
        "Anycubic",
        "Atomic",
        "AzureFilm",
        "BASF",
        "Bblife",
        "BCN3D",
        "Beyond Plastic",
        "California Filament",
        "Capricorn",
        "CC3D",
        "colorFabb",
        "Comgrow",
        "Cookiecad",
        "Creality",
        "CERPRiSE",
        "Das Filament",
        "DO3D",
        "DOW",
        "DSM",
        "Duramic",
        "ELEGOO",
        "Eryone",
        "Essentium",
        "eSUN",
        "Extrudr",
        "Fiberforce",
        "Fiberlogy",
        "FilaCube",
        "Filamentive",
        "Fillamentum",
        "FLASHFORGE",
        "Formfutura",
        "Francofil",
        "FilamentOne",
        "Fil X",
        "GEEETECH",
        "Giantarm",
        "Gizmo Dorks",
        "GreenGate3D",
        "HATCHBOX",
        "Hello3D",
        "IC3D",
        "IEMAI",
        "IIID Max",
        "INLAND",
        "iProspect",
        "iSANMATE",
        "Justmaker",
        "Keene Village Plastics",
        "Kexcelled",
        "LDO",
        "MakerBot",
        "MatterHackers",
        "MIKA3D",
        "NinjaTek",
        "Nobufil",
        "Novamaker",
        "OVERTURE",
        "OVVNYXE",
        "Polymaker",
        "Priline",
        "Printed Solid",
        "Protopasta",
        "Prusament",
        "Push Plastic",
        "R3D",
        "Re-pet3D",
        "Recreus",
        "Regen",
        "Sain SMART",
        "SliceWorx",
        "Snapmaker",
        "SnoLabs",
        "Spectrum",
        "SUNLU",
        "TTYT3D",
        "Tianse",
        "UltiMaker",
        "Valment",
        "Verbatim",
        "VO3D",
        "Voxelab",
        "VOXELPLA",
        "YOOPAI",
        "Yousu",
        "Ziro",
        "Zyltech"
    )

    var filamentTypes: Array<String> = arrayOf(
        "ABS",
        "ASA",
        "HIPS",
        "PA",
        "PA-CF",
        "PC",
        "PETG",
        "PLA",
        "PLA-CF",
        "PVA",
        "PP",
        "TPU"
    )

    fun GetDefaultTemps(materialType: String): IntArray {
        when (materialType) {
            "ABS" -> return intArrayOf(220, 250, 90, 100)
            "ASA" -> return intArrayOf(240, 280, 90, 100)
            "HIPS" -> return intArrayOf(230, 245, 80, 100)
            "PA" -> return intArrayOf(220, 250, 70, 90)
            "PA-CF" -> return intArrayOf(260, 280, 80, 100)
            "PC" -> return intArrayOf(260, 300, 100, 110)
            "PETG" -> return intArrayOf(230, 250, 70, 90)
            "PLA" -> return intArrayOf(190, 230, 50, 60)
            "PLA-CF" -> return intArrayOf(210, 240, 45, 65)
            "PVA" -> return intArrayOf(215, 225, 45, 60)
            "PP" -> return intArrayOf(225, 245, 80, 105)
            "TPU" -> return intArrayOf(210, 230, 25, 60)
        }
        return intArrayOf(185, 300, 45, 110)
    }
}