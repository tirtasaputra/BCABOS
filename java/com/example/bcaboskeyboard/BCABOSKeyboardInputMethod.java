package com.example.bcaboskeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BCABOSKeyboardInputMethod extends InputMethodService implements KeyboardView.OnKeyboardActionListener, View.OnClickListener {

    private final static int KEYCODE_CHANGE_NUMBER_SYMBOL = -7;

    private View g_viewparent;
    private KeyboardView g_keyboardview;

    private Keyboard g_keyboard_alphabet;
    private Keyboard g_keyboard_symbol1;
    private Keyboard g_keyboard_symbol2;

    private boolean IS_CAPS = false;
    private boolean IS_ALPHABET = true;
    private boolean IS_SYMBOL1 = true;

    //HOME
    private LinearLayout g_home_layout;
    private ImageButton g_btn_home;

    //FEATURE
    private LinearLayout g_feature_layout;
    private ImageButton g_btn_feature_back;
    private Button g_btn_feature_ongkir;

    public BCABOSKeyboardInputMethod(){
        super();
    }

    @Override
    public View onCreateInputView() {
        g_viewparent = getLayoutInflater().inflate(R.layout.layout_bcabos_keyboard, null);
        initiateKeyboardView();

        try{
            initiateMenu();
            initiateFeature();
        } catch (Exception ex){
            Log.i("EHS", ex.toString());
        }

        return g_viewparent;
    }

    private void initiateKeyboardView() {
        g_keyboardview = g_viewparent.findViewById(R.id.bcabos_keyboard_view);

        g_keyboard_alphabet = new Keyboard(this, R.xml.bcabos_keyboard_alphabet);
        g_keyboard_symbol1 = new Keyboard(this, R.xml.bcabos_keyboard_number, R.integer.bos_keyboard_mode_symbol_1);
        g_keyboard_symbol2 = new Keyboard(this, R.xml.bcabos_keyboard_number, R.integer.bos_keyboard_mode_symbol_2);

        g_keyboardview.setKeyboard(g_keyboard_alphabet);
        g_keyboardview.setOnKeyboardActionListener(this);
        g_keyboardview.setPreviewEnabled(false);
    }

    private void initiateMenu() {
        g_home_layout = g_viewparent.findViewById(R.id.bcabos_extended_home_layout);

        g_btn_home = g_viewparent.findViewById(R.id.bcabos_extended_home_button);
        g_btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeatureMenu();
            }
        });

    }

    private void initiateFeature() {
        g_feature_layout = g_viewparent.findViewById(R.id.bcabos_extended_feature_layout);

        g_btn_feature_back = g_viewparent.findViewById(R.id.bcabos_extended_feature_back_button);
        g_btn_feature_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHomeMenu();
            }
        });

        g_btn_feature_ongkir = g_viewparent.findViewById(R.id.bcabos_extended_feature_ongkir_button);
        g_btn_feature_ongkir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void showHomeMenu() {
        g_feature_layout.setVisibility(View.GONE);
        g_home_layout.setVisibility(View.VISIBLE);
    }

    private void showFeatureMenu() {
        g_home_layout.setVisibility(View.GONE);
        g_feature_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPress(int i) {
        char code = (char) i;
        if(code == ' '){
            InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
            imeManager.showInputMethodPicker();
        }
    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int i, int[] ints) {
        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            switch(i) {
                case KEYCODE_CHANGE_NUMBER_SYMBOL:
                    if(IS_SYMBOL1){
                        g_keyboardview.setKeyboard(g_keyboard_symbol2);
                    }
                    else{
                        g_keyboardview.setKeyboard(g_keyboard_symbol1);
                    }

                    IS_SYMBOL1 = !IS_SYMBOL1;
                    break;
                case Keyboard.KEYCODE_MODE_CHANGE :
                    if(IS_ALPHABET){
                        IS_SYMBOL1 = true;

                        g_keyboardview.setKeyboard(g_keyboard_symbol1);
                    }
                    else{
                        g_keyboardview.setKeyboard(g_keyboard_alphabet);
                    }

                    IS_ALPHABET = !IS_ALPHABET;
                    break;
                case Keyboard.KEYCODE_DELETE :
                    CharSequence selectedText = inputConnection.getSelectedText(0);

                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0);
                    } else {
                        inputConnection.commitText("", 1);
                    }

                    break;
                case Keyboard.KEYCODE_SHIFT:
                    IS_CAPS = !IS_CAPS;

                    g_keyboard_alphabet.setShifted(IS_CAPS);
                    g_keyboardview.invalidateAllKeys();

                    break;
                case Keyboard.KEYCODE_DONE:
                    inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    break;
                default :
                    char code = (char) i;
                    if(Character.isLetter(code) && IS_CAPS){
                        code = Character.toUpperCase(code);
                    }
                    inputConnection.commitText(String.valueOf(code), 1);

            }
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

    @Override
    public void onClick(View v) {

    }
}
