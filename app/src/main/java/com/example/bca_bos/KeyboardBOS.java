package com.example.bca_bos;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class KeyboardBOS extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private static final String TAG = KeyboardBOS.class.getSimpleName();
    private boolean capslock=false;
    private boolean punctuation=false;
    private boolean isInputConnectionExternalBOSKeyboard;

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
    private LinearLayout llOngkir;
    private ImageButton btnOngkirBack;

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
                showCekOngkir();
            }
        });
    }

    private void initiateOngkir(){
        llOngkir = root.findViewById(R.id.ongkirLinearLayout);

        btnOngkirBack = root.findViewById(R.id.ongkirBackButton);
        btnOngkirBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeatureMenu();
            }
        });

    }

    private void showHomeMenu() {
        llFeature.setVisibility(View.GONE);
        llHome.setVisibility(View.VISIBLE);
        changeLayoutStatus(true);
    }

    private void showFeatureMenu() {
        llHome.setVisibility(View.GONE);
        llOngkir.setVisibility(View.GONE);
        llFeature.setVisibility(View.VISIBLE);
        changeLayoutStatus(false);
    }

    private void showCekOngkir() {
        llFeature.setVisibility(View.GONE);
        llOngkir.setVisibility(View.VISIBLE);
        changeLayoutStatus(false);
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

        InputConnection ic = getCurrentInputConnection();
        playClick(i);
        switch (i){
            case KeyboardKey.CAPSLOCK:
                capslock = !capslock;
                setCapslock(capslock);
                break;
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1,0);
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
                    if (Character.isLetter(code) && capslock)
                        code = Character.toUpperCase(code);
                    ic.commitText(String.valueOf(code),1);
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

}
