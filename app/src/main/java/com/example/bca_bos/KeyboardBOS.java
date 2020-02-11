package com.example.bca_bos;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class KeyboardBOS extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private static final String TAG = KeyboardBOS.class.getSimpleName();
    private boolean capslock=false;
    private boolean punctuation=false;
    private boolean isInputConnectionExternalBOSKeyboard;
    String focusedEditText="";
    StringBuilder typedCharacters = new StringBuilder();

    private KeyboardView kv;
    private Keyboard keyboardQwerty, keyboardPunctuation;
    private View root;

    //HOME
    private LinearLayout llHome;
    private ImageButton btnBOS;

    //FEATURE
    private LinearLayout llFeature;
    private ImageButton btnFeatureBack;
    private Button btnCekOngkir, btnTemplateChat, btnRekapPesanan;

    //ONGKIR
    private LinearLayout llOngkir, llBerat, llAsal, llTujuan, llCekOngkir;
    private ImageButton btnOngkirBack, btnBeratCheck, btnAsalCheck, btnTujuanCheck;
    private EditText etBerat, etAsal, etTujuan;

    @Override
    public View onCreateInputView() {

        isInputConnectionExternalBOSKeyboard = true;
        root = getLayoutInflater().inflate(R.layout.keyboard, null);
        initiateKeyboardView();

        try{
            initiateMenu();
            initiateFeature();
            initiateOngkir();
        } catch (Exception ex){
            Log.i(TAG, ex.toString());
        }

        return root;
    }

    private void initiateKeyboardView() {
        keyboardQwerty = new Keyboard(this, R.xml.qwerty);
        keyboardPunctuation = new Keyboard(this, R.xml.punctuation);
        kv = root.findViewById(R.id.keyboard);
        kv.setKeyboard(keyboardQwerty);
        kv.setOnKeyboardActionListener(this);
        kv.setPreviewEnabled(false);
    }

    private void initiateMenu() {
        llHome = root.findViewById(R.id.homeLinearLayout);

        btnBOS = root.findViewById(R.id.bosButton);
        btnBOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeatureMenu();
            }
        });

    }

    private void initiateFeature() {
        llFeature = root.findViewById(R.id.featureLinearLayout);

        btnFeatureBack = root.findViewById(R.id.featureBackButton);
        btnFeatureBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHomeMenu();
            }
        });

        btnCekOngkir = root.findViewById(R.id.ongkirButton);
        btnCekOngkir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOngkir();
            }
        });
    }

    private void initiateOngkir(){
        llOngkir = root.findViewById(R.id.ongkirLinearLayout);
        llBerat = root.findViewById(R.id.beratLinearLayout);
        llAsal = root.findViewById(R.id.asalLinearLayout);
        llTujuan = root.findViewById(R.id.tujuanLinearLayout);
        llCekOngkir = root.findViewById(R.id.cekOngkirLinearLayout);

        etBerat = root.findViewById(R.id.beratEditText);
        etBerat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    focusedEditText = "etBerat";
                    typedCharacters.setLength(0);
                }
            }
        });
        etBerat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showBeratMenu();
                return false;
            }
        });
        btnBeratCheck = root.findViewById(R.id.beratCheckButton);
        btnBeratCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAsalMenu();
            }
        });

        etAsal = root.findViewById(R.id.asalEditText);
        etAsal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    focusedEditText = "etAsal";
                    typedCharacters.setLength(0);
                }
            }
        });
        etAsal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showAsalMenu();
                return false;
            }
        });
        btnAsalCheck = root.findViewById(R.id.asalCheckButton);
        btnAsalCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTujuanMenu();
            }
        });

        etTujuan = root.findViewById(R.id.tujuanEditText);
        etTujuan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    focusedEditText = "etTujuan";
                    typedCharacters.setLength(0);
                }
            }
        });
        etTujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTujuanMenu();
            }
        });
        btnTujuanCheck = root.findViewById(R.id.tujuanCheckButton);
        btnTujuanCheck.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showOngkir();
                return false;
            }
        });

        btnOngkirBack = root.findViewById(R.id.ongkirBackButton);
        btnOngkirBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeatureMenu();
            }
        });

    }

    private void showHomeMenu() {
        showAllMenu();
        llFeature.setVisibility(View.GONE);
        llOngkir.setVisibility(View.GONE);
        changeLayoutStatus(true);
    }

    private void showFeatureMenu() {
        showAllMenu();
        llHome.setVisibility(View.GONE);
        llOngkir.setVisibility(View.GONE);
        changeLayoutStatus(true);
    }

    private void showOngkir() {
        showAllMenu();
        kv.setVisibility(View.GONE);
        llHome.setVisibility(View.GONE);
        llFeature.setVisibility(View.GONE);
        changeLayoutStatus(false);
    }

    private void showBeratMenu() {
        showAllMenu();
        llHome.setVisibility(View.GONE);
        llFeature.setVisibility(View.GONE);
        llAsal.setVisibility(View.GONE);
        llTujuan.setVisibility(View.GONE);
        llCekOngkir.setVisibility(View.GONE);
        changeLayoutStatus(false);
    }

    private void showAsalMenu() {
        showAllMenu();
        llHome.setVisibility(View.GONE);
        llFeature.setVisibility(View.GONE);
        llBerat.setVisibility(View.GONE);
        llTujuan.setVisibility(View.GONE);
        llCekOngkir.setVisibility(View.GONE);
    }

    private void showTujuanMenu() {
        showAllMenu();
        llHome.setVisibility(View.GONE);
        llFeature.setVisibility(View.GONE);
        llBerat.setVisibility(View.GONE);
        llAsal.setVisibility(View.GONE);
        llCekOngkir.setVisibility(View.GONE);
    }

    private void showAllMenu(){
        kv.setVisibility(View.VISIBLE);
        llHome.setVisibility(View.VISIBLE);
        llFeature.setVisibility(View.VISIBLE);
        llBerat.setVisibility(View.VISIBLE);
        llAsal.setVisibility(View.VISIBLE);
        llTujuan.setVisibility(View.VISIBLE);
        llCekOngkir.setVisibility(View.VISIBLE);
        llOngkir.setVisibility(View.VISIBLE);
    }

    private void changeLayoutStatus(Boolean homeStatus){
        this.isInputConnectionExternalBOSKeyboard = homeStatus;
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int i, int[] ints) {
        final InputConnection ic = getCurrentInputConnection();  //getCurrentInputConnection digunakan untuk mendapatkan koneksi ke bidang input aplikasi lain.
        CharSequence selectedText = ic.getSelectedText(0);
        playClick(i);
        switch (i){
            case KeyboardKey.CAPSLOCK:
                capslock = !capslock;
                setCapslock(capslock);
                break;
            case Keyboard.KEYCODE_DELETE:
                deleteKeyPressed(ic, selectedText);
            break;
            case Keyboard.KEYCODE_SHIFT:
                capslock = !capslock;
                setCapslock(capslock);
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case KeyboardKey.CHANGE_KEYBOARD:
                changeKeyboardLayout();
                break;
            case KeyboardKey.PUNCTUATION:
                punctuation = !punctuation;
                changeToPunctuationKeyboardLayout(punctuation);
                break;
                default:
                    char code = (char) i;
                    if (Character.isLetter(code) && capslock){
                        code = Character.toUpperCase(code);
                    }
                    commitTextToBOSKeyboardEditText(String.valueOf(code));
        }

    }

    private void playClick(int i) {

        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch (i){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
                default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }

    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    private void setCapslock(boolean isCapslock){
        kv.setShifted(isCapslock);
        kv.invalidateAllKeys();
    }

    private void changeToPunctuationKeyboardLayout(boolean isPunctuation){
        if(!isPunctuation) {
            kv.setKeyboard(keyboardPunctuation);
        } else {
            kv.setKeyboard(keyboardQwerty);
        }
        kv.invalidateAllKeys();
    }

    private void changeKeyboardLayout(){
        InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        imeManager.showInputMethodPicker();
    }

    private void commitTextToBOSKeyboardEditText(String character){
        typedCharacters.append(character);

        switch (focusedEditText) {
            case "etBerat":
                etBerat.setText(typedCharacters);
                etBerat.setSelection(etBerat.getText().length());
                break;
            case "etAsal":
                etAsal.setText(typedCharacters);
                etAsal.setSelection(etAsal.getText().length());
                break;
            case "etTujuan":
                etTujuan.setText(typedCharacters);
                etTujuan.setSelection(etTujuan.getText().length());
                break;
            default:
                InputConnection inputConnection = getCurrentInputConnection();
                inputConnection.commitText(character, 1);
                break;
        }

    }

    private void deleteKeyPressed(InputConnection ic, CharSequence selectedText){
        if(isInputConnectionExternalBOSKeyboard){
            if(TextUtils.isEmpty(selectedText)) {
                ic.deleteSurroundingText(1, 0);
            } else {
                ic.commitText("", 1);
            }
        } else if(!isInputConnectionExternalBOSKeyboard){
            switch (focusedEditText) {
                case "etBerat":
                    int etBeratLength = etBerat.getText().length();
                    if (etBeratLength > 0) {
                        etBerat.getText().delete(etBeratLength - 1, etBeratLength);
                        if(typedCharacters.length()>0){
                            typedCharacters.deleteCharAt(etBeratLength - 1);
                        }
                    }
                    etBerat.setSelection(etBerat.getText().length());
                    break;
                case "etAsal":
                    int etAsalLength = etAsal.getText().length();
                    if (etAsalLength > 0) {
                        etAsal.getText().delete(etAsalLength - 1, etAsalLength);
                        if(typedCharacters.length()>0){
                            typedCharacters.deleteCharAt(etAsalLength - 1);
                        }
                    }
                    etAsal.setSelection(etAsal.getText().length());
                    break;
                case "etTujuan":
                    int etTujuanLength = etTujuan.getText().length();
                    if (etTujuanLength > 0) {
                        etTujuan.getText().delete(etTujuanLength - 1, etTujuanLength);
                        if(typedCharacters.length()>0){
                            typedCharacters.deleteCharAt(etTujuanLength - 1);
                        }
                    }
                    etTujuan.setSelection(etTujuan.getText().length());
                    break;

            }
        }
    }

}
