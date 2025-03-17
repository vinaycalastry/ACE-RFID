package dngsoftware.acerfid;

import static java.lang.String.format;
import static dngsoftware.acerfid.Utils.GetMaterialLength;
import static dngsoftware.acerfid.Utils.GetMaterialWeight;
import static dngsoftware.acerfid.Utils.GetTemps;
import static dngsoftware.acerfid.Utils.SetPermissions;
import static dngsoftware.acerfid.Utils.bytesToHex;
import static dngsoftware.acerfid.Utils.canMfc;
import static dngsoftware.acerfid.Utils.dp2Px;
import static dngsoftware.acerfid.Utils.getPixelColor;
import static dngsoftware.acerfid.Utils.materialWeights;
import static dngsoftware.acerfid.Utils.numToBytes;
import static dngsoftware.acerfid.Utils.materialTypes;
import static dngsoftware.acerfid.Utils.GetSetting;
import static dngsoftware.acerfid.Utils.SaveSetting;
import static dngsoftware.acerfid.Utils.parseColor;
import static dngsoftware.acerfid.Utils.parseNumber;
import static dngsoftware.acerfid.Utils.playBeep;
import static dngsoftware.acerfid.Utils.subArray;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.materialswitch.MaterialSwitch;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    nAdapter nfcReader = null;
    Tag currentTag = null;
    ArrayAdapter<String> madapter, sadapter;
    String MaterialName, MaterialWeight = "1 KG", MaterialColor = "0000FF";
    Dialog pickerDialog;
    View colorView;
    TextView tagID, infoText;
    int SelectedSize;
    MaterialSwitch autoread;
    Spinner material, spoolsize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spoolsize = findViewById(R.id.spoolsize);
        material = findViewById(R.id.material);
        colorView = findViewById(R.id.colorview);
        Spinner colorspin = findViewById(R.id.colorspin);
        autoread = findViewById(R.id.autoread);
        tagID = findViewById(R.id.tagid);
        Button rbtn = findViewById(R.id.readbutton);
        Button wbtn = findViewById(R.id.writebutton);
        infoText = findViewById(R.id.infotext);

        colorView.setOnClickListener(view -> openPicker());
        colorView.setBackgroundColor(Color.argb(255, 0, 0, 255));
        rbtn.setOnClickListener(view -> readTag(currentTag));
        wbtn.setOnClickListener(view -> writeTag(currentTag));

        SetPermissions(this);
        if (!canMfc(this)) {
            Toast.makeText(getApplicationContext(), R.string.this_device_does_not_support_mifare_ultralight_tags, Toast.LENGTH_SHORT).show();
        }

        nfcReader = new nAdapter(this);

        sadapter = new ArrayAdapter<>(this, R.layout.spinner_item, materialWeights);
        spoolsize.setAdapter(sadapter);
        spoolsize.setSelection(SelectedSize);
        spoolsize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SelectedSize = spoolsize.getSelectedItemPosition();
                MaterialWeight = sadapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        madapter = new ArrayAdapter<>(this, R.layout.spinner_item, materialTypes);
        material.setAdapter(madapter);
        material.setSelection(3);
        material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                MaterialName = madapter.getItem(position);
                assert MaterialName != null;
                infoText.setText(String.format(Locale.getDefault(),getString(R.string.info_temps),
                        GetTemps(MaterialName)[0], GetTemps(MaterialName)[1], GetTemps(MaterialName)[2], GetTemps(MaterialName)[3]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        colorspin.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    openPicker();
                    break;
                case MotionEvent.ACTION_UP:
                    v.performClick();
                    break;
                default:
                    break;
            }
            return false;
        });

        autoread.setChecked(GetSetting(this, "autoread", false));
        autoread.setOnCheckedChangeListener((buttonView, isChecked) -> SaveSetting(this, "autoread", isChecked));

        ReadTagUID(getIntent());
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            nfcReader.enableForeground();
            if (!nfcReader.getNfc().isEnabled()) {
                Toast.makeText(getApplicationContext(), R.string.please_activate_nfc, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        } catch (Exception ignored) {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            nfcReader.disableForeground();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        ReadTagUID(intent);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (pickerDialog != null && pickerDialog.isShowing()) {
            pickerDialog.dismiss();
            openPicker();
        }

    }

    void ReadTagUID(Intent intent) {
        if (intent != null) {
            try {
                if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
                    currentTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                    assert currentTag != null;
                    Toast.makeText(getApplicationContext(), getString(R.string.tag_found) + bytesToHex(currentTag.getId(), false), Toast.LENGTH_SHORT).show();
                    tagID.setText(bytesToHex(currentTag.getId(), true));
                    if (GetSetting(this, "autoread", false)) {
                        readTag(currentTag);
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    private void readTag(Tag tag) {
        if (tag == null) {
            Toast.makeText(getApplicationContext(), R.string.no_nfc_tag_found, Toast.LENGTH_SHORT).show();
            return;
        }
        MifareUltralight ultralight = MifareUltralight.get(tag);
        if (ultralight != null) {
            try {
                ultralight.connect();
                byte[] data = new byte[144];
                ByteBuffer buff = ByteBuffer.wrap(data);
                for (int page = 4; page <= 36; page += 4) {
                    byte[] pageData = ultralight.readPages(page);
                    if (pageData != null) {
                        buff.put(pageData);
                    }
                }
                if (buff.array()[0] != (byte)0x00) {
                    MaterialName = new String(subArray(buff.array(), 44, 16), StandardCharsets.UTF_8 ).trim();
                    material.setSelection(madapter.getPosition(MaterialName));
                    MaterialColor = parseColor(subArray(buff.array(), 65, 3));
                    colorView.setBackgroundColor(Color.parseColor("#" + MaterialColor));
                    // String sku = new String(subArray(buff.array(), 4, 16), StandardCharsets.UTF_8 ).trim();
                    // String Brand = new String(subArray(buff.array(), 24, 16), StandardCharsets.UTF_8).trim();
                    int extMin = parseNumber(subArray(buff.array(),80,2));
                    int extMax = parseNumber(subArray(buff.array(),82,2));
                    int bedMin = parseNumber(subArray(buff.array(),100,2));
                    int bedMax = parseNumber(subArray(buff.array(),102,2));
                    infoText.setText(String.format(Locale.getDefault(),getString(R.string.info_temps), extMin, extMax, bedMin, bedMax));
                    // int diameter = parseNumber(subArray(buff.array(),104,2));
                    MaterialWeight = GetMaterialWeight(parseNumber(subArray(buff.array(),106,2)));
                    spoolsize.setSelection(sadapter.getPosition(MaterialWeight));
                    Toast.makeText(getApplicationContext(), R.string.data_read_from_tag, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.unknown_or_empty_tag, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ignored) {
                Toast.makeText(getApplicationContext(), R.string.error_reading_tag, Toast.LENGTH_SHORT).show();
            } finally {
                try {
                    ultralight.close();
                } catch (Exception ignored) {}
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.invalid_tag_type, Toast.LENGTH_SHORT).show();
        }
    }

    private void writeTag(Tag tag) {
        if (tag == null) {
            Toast.makeText(getApplicationContext(), R.string.no_nfc_tag_found, Toast.LENGTH_SHORT).show();
            return;
        }

        MifareUltralight ultralight = MifareUltralight.get(tag);
        if (ultralight != null) {
            try {
                ultralight.connect();
                ultralight.writePage(4, new byte[]{(byte) 0x7B, (byte) 0x00, (byte) 0x65, (byte) 0x00}); //mb / data len?
                ultralight.writePage(5, new byte[] {(byte)0x41, (byte)0x48, (byte)0x50, (byte)0x4C});    //sku
                ultralight.writePage(6, new byte[] {(byte)0x4C, (byte)0x42, (byte)0x2D, (byte)0x31});    //sku
                ultralight.writePage(7, new byte[] {(byte)0x30, (byte)0x33, (byte)0x00, (byte)0x00});    //sku
                ultralight.writePage(8, new byte[] {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00});    //sku
                ultralight.writePage(10, new byte[] {(byte)0x41, (byte)0x43, (byte)0x00, (byte)0x00});   //brand
                ultralight.writePage(11, new byte[] {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00});   //brand
                ultralight.writePage(12, new byte[] {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00});   //brand
                ultralight.writePage(13, new byte[] {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00});   //brand
                byte[] matData = new byte[16];
                Arrays.fill(matData, (byte) 0);
                System.arraycopy(MaterialName.getBytes(), 0, matData, 0, MaterialName.getBytes().length);
                ultralight.writePage(15,subArray(matData,0,4));   //type
                ultralight.writePage(16,subArray(matData,4,4));   //type
                ultralight.writePage(17,subArray(matData,8,4));   //type
                ultralight.writePage(18,subArray(matData,12,4));  //type
                ultralight.writePage(20, parseColor(MaterialColor + "FF")); //color
                byte[] extTemp = new byte[4];
                System.arraycopy(numToBytes(GetTemps(MaterialName)[0]), 0, extTemp, 0, 2); //min
                System.arraycopy(numToBytes(GetTemps(MaterialName)[1]), 0, extTemp, 2, 2); //max
                ultralight.writePage(24, extTemp);
                byte[] bedTemp = new byte[4];
                System.arraycopy(numToBytes(GetTemps(MaterialName)[2]), 0, bedTemp, 0, 2); //min
                System.arraycopy(numToBytes(GetTemps(MaterialName)[3]), 0, bedTemp, 2, 2); //max
                ultralight.writePage(29, bedTemp);
                byte[] filData = new byte[4];
                System.arraycopy(numToBytes(175), 0, filData, 0, 2); //diameter
                System.arraycopy(numToBytes(GetMaterialLength(MaterialWeight)), 0, filData, 2, 2); //length
                ultralight.writePage(30, filData);
                ultralight.writePage(31, new byte[] {(byte)0xE8, (byte)0x03, (byte)0x00, (byte)0x00}); //?
                playBeep();
                Toast.makeText(getApplicationContext(), R.string.data_written_to_tag, Toast.LENGTH_SHORT).show();
            } catch (Exception ignored) {
                Toast.makeText(getApplicationContext(), R.string.error_writing_to_tag, Toast.LENGTH_SHORT).show();
            } finally {
                try {
                    ultralight.close();
                } catch (Exception ignored) {}
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.invalid_tag_type, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    void openPicker() {
        try {
            pickerDialog = new Dialog(this, R.style.Theme_AceRFID);
            pickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pickerDialog.setContentView(R.layout.picker_dialog);
            pickerDialog.setCanceledOnTouchOutside(false);
            pickerDialog.setTitle(R.string.pick_color);
            final Button btnCls = pickerDialog.findViewById(R.id.btncls);
            EditText colorTxt = pickerDialog.findViewById(R.id.txtcolor);
            View dcolorView = pickerDialog.findViewById(R.id.dcolorview);
            ImageView picker = pickerDialog.findViewById(R.id.picker);
            dcolorView.setBackgroundColor(Color.parseColor("#" + MaterialColor));
            colorTxt.setText(MaterialColor);
            btnCls.setOnClickListener(v -> {
                if (colorTxt.getText().toString().length() == 6) {
                    try {
                        MaterialColor = colorTxt.getText().toString();
                        colorView.setBackgroundColor(Color.parseColor("#" + MaterialColor));
                        dcolorView.setBackgroundColor(Color.parseColor("#" + MaterialColor));
                    } catch (Exception ignored) {
                    }
                }
                pickerDialog.dismiss();
            });
            colorTxt.setOnEditorActionListener((v, actionId, event) -> {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(colorTxt.getWindowToken(), 0);
                if (colorTxt.getText().toString().length() == 6) {
                    try {
                        MaterialColor = colorTxt.getText().toString();
                        colorView.setBackgroundColor(Color.parseColor("#" + MaterialColor));
                        dcolorView.setBackgroundColor(Color.parseColor("#" + MaterialColor));

                    } catch (Exception ignored) {}
                }
                return true;
            });
            picker.setOnTouchListener((v, event) -> {
                final int currPixel = getPixelColor(event, picker);
                if (currPixel != 0) {
                    MaterialColor = format("%02x%02x%02x", Color.red(currPixel), Color.green(currPixel), Color.blue(currPixel)).toUpperCase();
                    colorView.setBackgroundColor(Color.argb(255, Color.red(currPixel), Color.green(currPixel), Color.blue(currPixel)));
                    dcolorView.setBackgroundColor(Color.argb(255, Color.red(currPixel), Color.green(currPixel), Color.blue(currPixel)));
                    pickerDialog.dismiss();
                }
                return false;
            });
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float scrWwidth = displayMetrics.widthPixels;
            if (scrWwidth > dp2Px(this, 500)) scrWwidth = dp2Px(this, 500);
            SeekBar seekBarFont = pickerDialog.findViewById(R.id.seekbar_font);
            LinearGradient test = new LinearGradient(50.f, 0.f, scrWwidth - 250, 0.0f,
                    new int[]{0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF, 0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF}, null, Shader.TileMode.CLAMP);
            ShapeDrawable shape = new ShapeDrawable(new RectShape());
            shape.getPaint().setShader(test);
            seekBarFont.setProgressDrawable(shape);
            seekBarFont.setMax(256 * 7 - 1);
            seekBarFont.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        int r = 0;
                        int g = 0;
                        int b = 0;

                        if (progress < 256) {
                            b = progress;
                        } else if (progress < 256 * 2) {
                            g = progress % 256;
                            b = 256 - progress % 256;
                        } else if (progress < 256 * 3) {
                            g = 255;
                            b = progress % 256;
                        } else if (progress < 256 * 4) {
                            r = progress % 256;
                            g = 256 - progress % 256;
                            b = 256 - progress % 256;
                        } else if (progress < 256 * 5) {
                            r = 255;
                            b = progress % 256;
                        } else if (progress < 256 * 6) {
                            r = 255;
                            g = progress % 256;
                            b = 256 - progress % 256;
                        } else if (progress < 256 * 7) {
                            r = 255;
                            g = 255;
                            b = progress % 256;
                        }
                        MaterialColor = format("%02x%02x%02x", r, g, b).toUpperCase();
                        colorTxt.setText(MaterialColor);
                        colorView.setBackgroundColor(Color.argb(255, r, g, b));
                        dcolorView.setBackgroundColor(Color.argb(255, r, g, b));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            pickerDialog.show();
        } catch (Exception ignored) {}
    }

}