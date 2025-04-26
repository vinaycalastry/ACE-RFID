package dngsoftware.acerfid

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.ReaderCallback
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room.databaseBuilder
import dngsoftware.acerfid.Utils.GetBrand
import dngsoftware.acerfid.Utils.GetDefaultTemps
import dngsoftware.acerfid.Utils.GetMaterialLength
import dngsoftware.acerfid.Utils.GetMaterialWeight
import dngsoftware.acerfid.Utils.GetSetting
import dngsoftware.acerfid.Utils.GetSku
import dngsoftware.acerfid.Utils.GetTemps
import dngsoftware.acerfid.Utils.SaveSetting
import dngsoftware.acerfid.Utils.SetPermissions
import dngsoftware.acerfid.Utils.arrayContains
import dngsoftware.acerfid.Utils.bytesToHex
import dngsoftware.acerfid.Utils.dp2Px
import dngsoftware.acerfid.Utils.filamentTypes
import dngsoftware.acerfid.Utils.filamentVendors
import dngsoftware.acerfid.Utils.getAllMaterials
import dngsoftware.acerfid.Utils.getPixelColor
import dngsoftware.acerfid.Utils.materialWeights
import dngsoftware.acerfid.Utils.numToBytes
import dngsoftware.acerfid.Utils.openUrl
import dngsoftware.acerfid.Utils.parseColor
import dngsoftware.acerfid.Utils.parseNumber
import dngsoftware.acerfid.Utils.playBeep
import dngsoftware.acerfid.Utils.populateDatabase
import dngsoftware.acerfid.Utils.setTypeByItem
import dngsoftware.acerfid.Utils.setVendorByItem
import dngsoftware.acerfid.Utils.subArray
import dngsoftware.acerfid.databinding.ActivityMainBinding
import dngsoftware.acerfid.databinding.AddDialogBinding
import dngsoftware.acerfid.databinding.PickerDialogBinding
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.Arrays
import java.util.Locale
import java.util.Objects
import kotlin.math.min
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity(), ReaderCallback {
    private var filamentDao: FilamentDao? = null
    private var nfcAdapter: NfcAdapter? = null
    var currentTag: Tag? = null
    var madapter: ArrayAdapter<String>? = null
    var sadapter: ArrayAdapter<String>? = null
    var MaterialName: String? = null
    var MaterialWeight: String? = "1 KG"
    var MaterialColor: String? = "0000FF"
    var pickerDialog: Dialog? = null
    var addDialog: Dialog? = null
    var SelectedSize: Int = 0
    var userSelect: Boolean = false
    private var main: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val res = applicationContext.resources
        val locale = Locale("en")
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        res.updateConfiguration(config, res.displayMetrics)

        main = ActivityMainBinding.inflate(layoutInflater)
        val rv: View = main!!.root
        setContentView(rv)
        main!!.addbutton.visibility = View.INVISIBLE
        main!!.editbutton.visibility = View.INVISIBLE
        main!!.deletebutton.visibility = View.INVISIBLE


        main!!.colorview.setOnClickListener { view -> openPicker() }
        main!!.colorview.setBackgroundColor(Color.argb(255, 0, 0, 255))
        main!!.readbutton.setOnClickListener { view -> readTag(currentTag) }
        main!!.writebutton.setOnClickListener { view -> writeTag(currentTag) }

        // main.addbutton.setOnClickListener(view -> openAddDialog(false));
        //  main.editbutton.setOnClickListener(view -> openAddDialog(true));
        main!!.deletebutton.setOnClickListener { view ->
            val builder =
                AlertDialog.Builder(this)
            builder.setTitle(R.string.delete_filament)
            builder.setMessage(MaterialName)
            builder.setPositiveButton(R.string.delete) { dialog, which ->
                if (filamentDao!!.getFilamentByName(MaterialName) != null) {
                    filamentDao!!.deleteItem(filamentDao!!.getFilamentByName(MaterialName)!!)
                    loadMaterials(false)
                    dialog.dismiss()
                }
            }
            builder.setNegativeButton(R.string.cancel) { dialog, which -> dialog.dismiss() }
            val alert = builder.create()
            alert.show()
        }

        val rdb = databaseBuilder(
            context = this, // Make sure 'this' is a Context (e.g., Activity, Application)
            klass = FilamentDB::class.java,
            name = "filament_database"
        )
            .fallbackToDestructiveMigration(false)
            .allowMainThreadQueries() // Be cautious using this in production
            .build()

        filamentDao = rdb.filamentDao()

        if (filamentDao!!.itemCount == 0) {
            populateDatabase(filamentDao!!)
        }

        SetPermissions(this)

        try {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            if (nfcAdapter != null && nfcAdapter!!.isEnabled) {
                val options = Bundle()
                options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)
                nfcAdapter!!.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A, options)
            } else {
                Toast.makeText(applicationContext, R.string.please_activate_nfc, Toast.LENGTH_LONG)
                    .show()
                startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                finish()
            }
        } catch (ignored: Exception) {
        }

        sadapter = ArrayAdapter<String>(this, R.layout.spinner_item, materialWeights)
        main!!.spoolsize.adapter = sadapter
        main!!.spoolsize.setSelection(SelectedSize)
        main!!.spoolsize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                SelectedSize = main!!.spoolsize.selectedItemPosition
                MaterialWeight = sadapter!!.getItem(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }

        loadMaterials(false)

        main!!.colorspin.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> openPicker()
                MotionEvent.ACTION_UP -> v.performClick()
                else -> {}
            }
            false
        }

        main!!.autoread.isChecked = GetSetting(this, "autoread", false)
        main!!.autoread.setOnCheckedChangeListener { buttonView, isChecked ->
            SaveSetting(
                this,
                "autoread",
                isChecked
            )
        }

        ReadTagUID(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (nfcAdapter != null && nfcAdapter!!.isEnabled) {
            try {
                nfcAdapter!!.disableReaderMode(this)
            } catch (ignored: Exception) {
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (pickerDialog != null && pickerDialog!!.isShowing) {
            pickerDialog!!.dismiss()
            openPicker()
        }
    }

    override fun onTagDiscovered(tag: Tag) {
        try {
            currentTag = tag
            runOnUiThread {
                Toast.makeText(
                    applicationContext, getString(R.string.tag_found) + bytesToHex(
                        currentTag!!.id, false
                    ), Toast.LENGTH_SHORT
                ).show()
                main!!.tagid.text = bytesToHex(currentTag!!.id, true)
                if (GetSetting(this, "autoread", false)) {
                    readTag(currentTag)
                }
            }
        } catch (ignored: Exception) {
        }
    }

    fun loadMaterials(select: Boolean) {
        madapter = ArrayAdapter<String>(this, R.layout.spinner_item, getAllMaterials(filamentDao!!)!!)
        main!!.material.adapter = madapter
        main!!.material.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                MaterialName = madapter!!.getItem(position)
                checkNotNull(MaterialName)
                main!!.infotext.text =
                    java.lang.String.format(
                        Locale.getDefault(),
                        getString(R.string.info_temps),
                        GetTemps(filamentDao!!, MaterialName).get(0),
                        GetTemps(filamentDao!!, MaterialName).get(1),
                        GetTemps(filamentDao!!, MaterialName).get(2),
                        GetTemps(filamentDao!!, MaterialName).get(3)
                    )

                if (position <= 11) {
                    //  main.editbutton.setVisibility(View.INVISIBLE);
                    main!!.deletebutton.visibility = View.INVISIBLE
                } else {
                    // main.editbutton.setVisibility(View.VISIBLE);
                    main!!.deletebutton.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
        if (select) {
            main!!.material.setSelection(madapter!!.getPosition(MaterialName))
        } else {
            main!!.material.setSelection(3)
        }
    }

    fun ReadTagUID(intent: Intent?) {
        if (intent != null) {
            try {
                if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
                    currentTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
                    checkNotNull(currentTag)
                    Toast.makeText(
                        applicationContext, getString(R.string.tag_found) + bytesToHex(
                            currentTag!!.id, false
                        ), Toast.LENGTH_SHORT
                    ).show()
                    main!!.tagid.setText(bytesToHex(currentTag!!.id, true))
                    if (GetSetting(this, "autoread", false)) {
                        readTag(currentTag)
                    }
                }
            } catch (ignored: Exception) {
            }
        }
    }

    private fun readTag(tag: Tag?) {
        if (tag == null) {
            Toast.makeText(applicationContext, R.string.no_nfc_tag_found, Toast.LENGTH_SHORT).show()
            return
        }
        val nfcA = NfcA.get(tag)
        if (nfcA != null) {
            try {
                nfcA.connect()
                val data = ByteArray(144)
                val buff = ByteBuffer.wrap(data)
                var page = 4
                while (page <= 36) {
                    val pageData = nfcA.transceive(byteArrayOf(0x30.toByte(), page.toByte()))
                    if (pageData != null) {
                        buff.put(pageData)
                    }
                    page += 4
                }
                if (buff.array()[0] != 0x00.toByte()) {
                    userSelect = true
                    MaterialName = String(
                        subArray(buff.array(), 44, 16)!!,
                        StandardCharsets.UTF_8
                    ).trim { it <= ' ' }
                    main!!.material.setSelection(madapter!!.getPosition(MaterialName))
                    MaterialColor = parseColor(subArray(buff.array(), 65, 3))
                    main!!.colorview.setBackgroundColor("#$MaterialColor".toColorInt())
                    // String sku = new String(subArray(buff.array(), 4, 16), StandardCharsets.UTF_8 ).trim();
                    // String Brand = new String(subArray(buff.array(), 24, 16), StandardCharsets.UTF_8).trim();
                    val extMin: Int = parseNumber(subArray(buff.array(), 80, 2))
                    val extMax: Int = parseNumber(subArray(buff.array(), 82, 2))
                    val bedMin: Int = parseNumber(subArray(buff.array(), 100, 2))
                    val bedMax: Int = parseNumber(subArray(buff.array(), 102, 2))
                    main!!.infotext.text = String.format(
                        Locale.getDefault(),
                        getString(R.string.info_temps),
                        extMin,
                        extMax,
                        bedMin,
                        bedMax
                    )
                    // int diameter = parseNumber(subArray(buff.array(),104,2));
                    MaterialWeight = GetMaterialWeight(parseNumber(subArray(buff.array(), 106, 2)))
                    main!!.spoolsize.setSelection(sadapter!!.getPosition(MaterialWeight))
                    Toast.makeText(
                        applicationContext,
                        R.string.data_read_from_tag,
                        Toast.LENGTH_SHORT
                    ).show()
                    userSelect = false
                } else {
                    Toast.makeText(
                        applicationContext,
                        R.string.unknown_or_empty_tag,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (ignored: Exception) {
                Toast.makeText(applicationContext, R.string.error_reading_tag, Toast.LENGTH_SHORT)
                    .show()
            } finally {
                try {
                    nfcA.close()
                } catch (ignored: Exception) {
                }
            }
        } else {
            Toast.makeText(applicationContext, R.string.invalid_tag_type, Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(Exception::class)
    private fun nfcAWritePage(nfcA: NfcA, page: Int, data: ByteArray) {
        val cmd = ByteArray(6)
        cmd[0] = 0xA2.toByte()
        cmd[1] = page.toByte()
        System.arraycopy(data, 0, cmd, 2, data.size)
        nfcA.transceive(cmd)
    }

    private fun writeTag(tag: Tag?) {
        if (tag == null) {
            Toast.makeText(applicationContext, R.string.no_nfc_tag_found, Toast.LENGTH_SHORT).show()
            return
        }
        val nfcA = NfcA.get(tag)
        if (nfcA != null) {
            try {
                nfcA.connect()
                nfcAWritePage(nfcA, 4, byteArrayOf(123, 0, 101, 0))
                for (i in 0..4) { //sku
                    subArray(GetSku(filamentDao!!, MaterialName), i * 4, 4)?.let {
                        nfcAWritePage(nfcA, 5 + i,
                            it
                        )
                    }
                }
                for (i in 0..4) { //brand
                    subArray(GetBrand(filamentDao!!, MaterialName), i * 4, 4)?.let {
                        nfcAWritePage(nfcA, 10 + i,
                            it
                        )
                    }
                }
                val matData = ByteArray(20)
                Arrays.fill(matData, 0.toByte())
                System.arraycopy(
                    MaterialName!!.toByteArray(),
                    0,
                    matData,
                    0,
                    min(20.0, MaterialName!!.length.toDouble()).toInt()
                )
                nfcAWritePage(nfcA, 15, subArray(matData, 0, 4)!!) //type
                nfcAWritePage(nfcA, 16, subArray(matData, 4, 4)!!) //type
                nfcAWritePage(nfcA, 17, subArray(matData, 8, 4)!!) //type
                nfcAWritePage(nfcA, 18, subArray(matData, 12, 4)!!) //type
                nfcAWritePage(nfcA, 20, parseColor(MaterialColor + "FF")!!) //color
                //ultralight.writePage(23, new byte[] {50, 0, 100, 0});   //more temps?
                val extTemp = ByteArray(4)
                numToBytes(GetTemps(filamentDao!!, MaterialName).get(0))?.let {
                    System.arraycopy(
                        it,
                        0,
                        extTemp,
                        0,
                        2
                    )
                } //min
                numToBytes(GetTemps(filamentDao!!, MaterialName).get(1))?.let {
                    System.arraycopy(
                        it,
                        0,
                        extTemp,
                        2,
                        2
                    )
                } //max
                nfcAWritePage(nfcA, 24, extTemp)
                val bedTemp = ByteArray(4)
                numToBytes(GetTemps(filamentDao!!, MaterialName).get(2))?.let {
                    System.arraycopy(
                        it,
                        0,
                        bedTemp,
                        0,
                        2
                    )
                } //min
                numToBytes(GetTemps(filamentDao!!, MaterialName).get(3))?.let {
                    System.arraycopy(
                        it,
                        0,
                        bedTemp,
                        2,
                        2
                    )
                } //max
                nfcAWritePage(nfcA, 29, bedTemp)
                val filData = ByteArray(4)
                numToBytes(175)?.let { System.arraycopy(it, 0, filData, 0, 2) } //diameter
                numToBytes(GetMaterialLength(MaterialWeight!!))?.let {
                    System.arraycopy(
                        it,
                        0,
                        filData,
                        2,
                        2
                    )
                } //length
                nfcAWritePage(nfcA, 30, filData)
                nfcAWritePage(nfcA, 31, byteArrayOf(232.toByte(), 3, 0, 0)) //?
                playBeep()
                Toast.makeText(applicationContext, R.string.data_written_to_tag, Toast.LENGTH_SHORT)
                    .show()
            } catch (ignored: Exception) {
                Toast.makeText(
                    applicationContext,
                    R.string.error_writing_to_tag,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                try {
                    nfcA.close()
                } catch (ignored: Exception) {
                }
            }
        } else {
            Toast.makeText(applicationContext, R.string.invalid_tag_type, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun openPicker() {
        try {
            pickerDialog = Dialog(this, R.style.Theme_AceRFID)
            pickerDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            pickerDialog!!.setCanceledOnTouchOutside(false)
            pickerDialog!!.setTitle(R.string.pick_color)
            val dl = PickerDialogBinding.inflate(layoutInflater)
            val rv: View = dl.root
            pickerDialog!!.setContentView(rv)

            dl.dcolorview.setBackgroundColor("#$MaterialColor".toColorInt())
            dl.txtcolor.setText(MaterialColor)
            dl.btncls.setOnClickListener { v ->
                if (dl.txtcolor.text.toString().length == 6) {
                    try {
                        MaterialColor = dl.txtcolor.text.toString()
                        main!!.colorview.setBackgroundColor("#$MaterialColor".toColorInt())
                        dl.dcolorview.setBackgroundColor("#$MaterialColor".toColorInt())
                    } catch (ignored: Exception) {
                    }
                }
                pickerDialog!!.dismiss()
            }
            dl.txtcolor.setOnEditorActionListener { v, actionId, event ->
                val imm =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(dl.txtcolor.windowToken, 0)
                if (dl.txtcolor.text.toString().length == 6) {
                    try {
                        MaterialColor = dl.txtcolor.text.toString()
                        main!!.colorview.setBackgroundColor("#$MaterialColor".toColorInt())
                        dl.dcolorview.setBackgroundColor("#$MaterialColor".toColorInt())
                    } catch (ignored: Exception) {
                    }
                }
                true
            }
            dl.picker.setOnTouchListener { v, event ->
                val currPixel: Int = getPixelColor(event, dl.picker)
                if (currPixel != 0) {
                    MaterialColor = String.format(
                        "%02x%02x%02x",
                        Color.red(currPixel),
                        Color.green(currPixel),
                        Color.blue(currPixel)
                    ).uppercase(Locale.getDefault())
                    main!!.colorview.setBackgroundColor(
                        Color.argb(
                            255,
                            Color.red(currPixel),
                            Color.green(currPixel),
                            Color.blue(currPixel)
                        )
                    )
                    dl.dcolorview.setBackgroundColor(
                        Color.argb(
                            255,
                            Color.red(currPixel),
                            Color.green(currPixel),
                            Color.blue(currPixel)
                        )
                    )
                    pickerDialog!!.dismiss()
                }
                false
            }
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            var scrWwidth = displayMetrics.widthPixels.toFloat()
            if (scrWwidth > dp2Px(this, 500f)) scrWwidth = dp2Px(this, 500f)

            val test = LinearGradient(
                50f, 0f, scrWwidth - 250, 0.0f,
                intArrayOf(
                    -0x1000000,
                    -0xffff01,
                    -0xff0100,
                    -0xff0001,
                    -0x10000,
                    -0xff01,
                    -0x100,
                    -0x1
                ), null, Shader.TileMode.CLAMP
            )
            val shape = ShapeDrawable(RectShape())
            shape.paint.setShader(test)
            dl.seekbarFont.progressDrawable = shape
            dl.seekbarFont.max = 256 * 7 - 1
            dl.seekbarFont.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        var r = 0
                        var g = 0
                        var b = 0

                        if (progress < 256) {
                            b = progress
                        } else if (progress < 256 * 2) {
                            g = progress % 256
                            b = 256 - progress % 256
                        } else if (progress < 256 * 3) {
                            g = 255
                            b = progress % 256
                        } else if (progress < 256 * 4) {
                            r = progress % 256
                            g = 256 - progress % 256
                            b = 256 - progress % 256
                        } else if (progress < 256 * 5) {
                            r = 255
                            b = progress % 256
                        } else if (progress < 256 * 6) {
                            r = 255
                            g = progress % 256
                            b = 256 - progress % 256
                        } else if (progress < 256 * 7) {
                            r = 255
                            g = 255
                            b = progress % 256
                        }
                        MaterialColor =
                            String.format("%02x%02x%02x", r, g, b).uppercase(Locale.getDefault())
                        dl.txtcolor.setText(MaterialColor)
                        main!!.colorview.setBackgroundColor(Color.argb(255, r, g, b))
                        dl.dcolorview.setBackgroundColor(Color.argb(255, r, g, b))
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                }
            })
            pickerDialog!!.show()
        } catch (ignored: Exception) {
        }
    }

    fun openAddDialog(edit: Boolean) {
        try {
            addDialog = Dialog(this, R.style.Theme_AceRFID)
            addDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            addDialog!!.setCanceledOnTouchOutside(false)
            addDialog!!.setTitle(R.string.add_filament)
            val dl = AddDialogBinding.inflate(layoutInflater)
            val rv: View = dl.root
            addDialog!!.setContentView(rv)
            dl.btncls.setOnClickListener { v -> addDialog!!.dismiss() }
            dl.btnhlp.setOnClickListener { v ->
                openUrl(
                    this,
                    getString(R.string.helpurl)
                )
            }

            dl.chkvendor.setOnClickListener { v ->
                if (dl.chkvendor.isChecked) {
                    dl.vendor.visibility = View.INVISIBLE
                    dl.txtvendor.visibility = View.VISIBLE
                } else {
                    dl.vendor.visibility = View.VISIBLE
                    dl.txtvendor.visibility = View.INVISIBLE
                }
            }

            if (edit) {
                dl.btnsave.setText(R.string.save)
                dl.lbltitle.setText(R.string.edit_filament)
            } else {
                dl.btnsave.setText(R.string.add)
                dl.lbltitle.setText(R.string.add_filament)
            }

            dl.btnsave.setOnClickListener { v ->
                if (dl.txtserial.text.toString().isEmpty() || dl.txtextmin.text.toString()
                        .isEmpty() || dl.txtextmax.text.toString()
                        .isEmpty() || dl.txtbedmin.text.toString()
                        .isEmpty() || dl.txtbedmax.text.toString().isEmpty()
                ) {
                    Toast.makeText(
                        applicationContext,
                        R.string.fill_all_fields,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                if (dl.chkvendor.isChecked && dl.txtvendor.text.toString().isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        R.string.fill_all_fields,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                var vendor = dl.vendor.selectedItem.toString()
                if (dl.chkvendor.isChecked) {
                    vendor = dl.txtvendor.text.toString().trim()
                }
                if (edit) {
                    updateFilament(
                        vendor,
                        dl.type.selectedItem.toString(),
                        dl.txtserial.text.toString(),
                        dl.txtextmin.text.toString(),
                        dl.txtextmax.text.toString(),
                        dl.txtbedmin.text.toString(),
                        dl.txtbedmax.text.toString()
                    )
                } else {
                    addFilament(
                        vendor,
                        dl.type.selectedItem.toString(),
                        dl.txtserial.text.toString(),
                        dl.txtextmin.text.toString(),
                        dl.txtextmax.text.toString(),
                        dl.txtbedmin.text.toString(),
                        dl.txtbedmax.text.toString()
                    )
                }
                addDialog!!.dismiss()
            }

            val vadapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.spinner_item, filamentVendors)
            dl.vendor.adapter = vadapter

            val tadapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.spinner_item, filamentTypes)
            dl.type.adapter = tadapter

            dl.type.setOnTouchListener { v, event ->
                userSelect = true
                v.performClick()
                false
            }

            dl.type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    if (userSelect) {
                        val tmpType = Objects.requireNonNull(tadapter.getItem(position))!!
                        val temps: IntArray = GetDefaultTemps(tmpType)
                        dl.txtextmin.setText(temps[0].toString())
                        dl.txtextmax.setText(temps[1].toString())
                        dl.txtbedmin.setText(temps[2].toString())
                        dl.txtbedmax.setText(temps[3].toString())
                        userSelect = false
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    userSelect = false
                }
            }

            if (edit) {
                setTypeByItem(dl.type, tadapter, MaterialName!!)
                try {
                    if (!arrayContains(
                            filamentVendors,
                            MaterialName!!.split((dl.type.selectedItem.toString() + " ").toRegex())
                                .dropLastWhile { it.isEmpty() }
                                .toTypedArray()[0].trim { it <= ' ' })
                    ) {
                        dl.chkvendor.isChecked = true
                        dl.txtvendor.visibility = View.VISIBLE
                        dl.vendor.visibility = View.INVISIBLE
                        dl.txtvendor.setText(
                            MaterialName!!.split((dl.type.selectedItem.toString() + " ").toRegex())
                                .dropLastWhile { it.isEmpty() }
                                .toTypedArray()[0].trim { it <= ' ' })
                    } else {
                        dl.chkvendor.isChecked = false
                        dl.txtvendor.visibility = View.INVISIBLE
                        dl.vendor.visibility = View.VISIBLE
                        setVendorByItem(dl.vendor, vadapter, MaterialName!!)
                    }
                } catch (ignored: Exception) {
                    dl.chkvendor.isChecked = false
                    dl.txtvendor.visibility = View.INVISIBLE
                    dl.vendor.visibility = View.VISIBLE
                    dl.vendor.setSelection(0)
                }
                try {
                    dl.txtserial.setText(
                        MaterialName!!.split((dl.type.selectedItem.toString() + " ").toRegex())
                            .dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    )
                } catch (ignored: ArrayIndexOutOfBoundsException) {
                    dl.txtserial.setText("")
                }
                val temps: IntArray = GetTemps(filamentDao!!, MaterialName)
                dl.txtextmin.setText(temps[0].toString())
                dl.txtextmax.setText(temps[1].toString())
                dl.txtbedmin.setText(temps[2].toString())
                dl.txtbedmax.setText(temps[3].toString())
            } else {
                dl.vendor.setSelection(0)
                dl.type.setSelection(7)
                val temps: IntArray = GetDefaultTemps("PLA")
                dl.txtextmin.setText(temps[0].toString())
                dl.txtextmax.setText(temps[1].toString())
                dl.txtbedmin.setText(temps[2].toString())
                dl.txtbedmax.setText(temps[3].toString())
            }

            addDialog!!.show()
        } catch (ignored: Exception) {
        }
    }

    fun addFilament(
        tmpVendor: String,
        tmpType: String?,
        tmpSerial: String,
        tmpExtMin: String,
        tmpExtMax: String?,
        tmpBedMin: String?,
        tmpBedMax: String?
    ) {
        val filament = Filament()
        filament.position = filamentDao!!.itemCount
        filament.filamentID = ""
        filament.filamentName = String.format(
            "%s %s %s",
            tmpVendor.trim { it <= ' ' },
            tmpType,
            tmpSerial.trim { it <= ' ' })
        filament.filamentVendor = ""
        filament.filamentParam =
            String.format("%s|%s|%s|%s", tmpExtMin, tmpExtMax, tmpBedMin, tmpBedMax)
        filamentDao!!.addItem(filament)
        loadMaterials(false)
    }

    fun updateFilament(
        tmpVendor: String,
        tmpType: String?,
        tmpSerial: String,
        tmpExtMin: String,
        tmpExtMax: String?,
        tmpBedMin: String?,
        tmpBedMax: String?
    ) {
        val currentFilament = filamentDao!!.getFilamentByName(MaterialName)
        val tmpPosition = currentFilament!!.position
        filamentDao!!.deleteItem(currentFilament)
        MaterialName = String.format(
            "%s %s %s",
            tmpVendor.trim { it <= ' ' },
            tmpType,
            tmpSerial.trim { it <= ' ' })
        val filament = Filament()
        filament.position = tmpPosition
        filament.filamentID = ""
        filament.filamentName = MaterialName
        filament.filamentVendor = ""
        filament.filamentParam =
            String.format("%s|%s|%s|%s", tmpExtMin, tmpExtMax, tmpBedMin, tmpBedMax)
        filamentDao!!.addItem(filament)
        loadMaterials(true)
    }
}