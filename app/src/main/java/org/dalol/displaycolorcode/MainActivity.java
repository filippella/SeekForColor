package org.dalol.displaycolorcode;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final int RENDER_CODE_LENGTH = 6;
    private EditText mRenderCode;
    private TextView mRenderBg, mRedVal, mGreenVal, mBlueVal;
    private SeekBar mRedSeek, mBlueSeek, mGreenSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configViews();
        configViewsAction();
        mRenderCode.setText("ff7043");
    }

    private void configViewsAction() {
        mRenderCode.addTextChangedListener(mWatcher);
        mRedSeek.setOnSeekBarChangeListener(this);
        mGreenSeek.setOnSeekBarChangeListener(this);
        mBlueSeek.setOnSeekBarChangeListener(this);
    }

    /**
     * Exists to configures the rest of the views
     */
    private void configViews() {
        mRenderCode = (EditText) this.findViewById(R.id.renderCode);
        mRenderBg = (TextView) this.findViewById(R.id.renderBG);
        mRedSeek = (SeekBar) this.findViewById(R.id.redSeekBar);
        mBlueSeek = (SeekBar) this.findViewById(R.id.blueSeekBar);
        mGreenSeek = (SeekBar) this.findViewById(R.id.greenSeekBar);

        mRedVal = (TextView) this.findViewById(R.id.redColorCodeVal);
        mGreenVal = (TextView) this.findViewById(R.id.greenColorCodeVal);
        mBlueVal = (TextView) this.findViewById(R.id.blueColorCodeVal);
    }

    private TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String insertedRenderCode = editable.toString();
            int renderCode = insertedRenderCode.replaceAll("\\s", "").length();
            if (renderCode > RENDER_CODE_LENGTH) {
                editable.delete((renderCode - 1), renderCode);
            }

            if (!insertedRenderCode.isEmpty() && (insertedRenderCode.length() == 6)) {
                changeBG(insertedRenderCode);
            }
        }
    };

    private void changeBG(String code) {
        try {
            mRenderBg.setBackgroundColor(Color.parseColor("#" + code));
            closeKeyBoard();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Cannot resolve color code!", Toast.LENGTH_LONG).show();
        }
    }

    private void closeKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager.isAcceptingText()) {
            inputManager.hideSoftInputFromWindow(mRenderCode.getWindowToken(), 0);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.redSeekBar:
                handleSeekChange(mRedVal, seekBar, SeekColor.RED);
                break;
            case R.id.greenSeekBar:
                handleSeekChange(mGreenVal, seekBar, SeekColor.GREEN);
                break;
            case R.id.blueSeekBar:
                handleSeekChange(mBlueVal, seekBar, SeekColor.BLUE);
                break;
        }
    }

    private void handleSeekChange(TextView val, SeekBar seekBar, SeekColor color) {

        int progress = getValue(seekBar.getProgress());
        val.setText(Integer.toString(progress));

        switch (color) {
            case RED:
                changeRGB(progress, Integer.parseInt(mGreenVal.getText().toString()), Integer.parseInt(mBlueVal.getText().toString()));
                break;
            case GREEN:
                changeRGB(Integer.parseInt(mRedVal.getText().toString()), progress, Integer.parseInt(mBlueVal.getText().toString()));
                break;
            case BLUE:
                changeRGB(Integer.parseInt(mGreenVal.getText().toString()), Integer.parseInt(mRedVal.getText().toString()), progress);
                break;
        }
    }

    private void changeRGB(int red, int green, int blue) {
        try {
            mRenderBg.setBackgroundColor(Color.rgb(red, green, blue));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Cannot resolve color code!", Toast.LENGTH_LONG).show();
        }
    }

    private int getValue(int progress) {
        float value = (progress * 255) / 100;
        return (int) Math.floor(value);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    enum SeekColor {
        RED, GREEN, BLUE
    }
}
