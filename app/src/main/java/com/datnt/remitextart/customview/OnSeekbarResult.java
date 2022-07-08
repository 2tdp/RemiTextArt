package com.datnt.remitextart.customview;

import android.view.View;

public interface OnSeekbarResult {

    void onDown(View v);

    void onMove(View v, int value);

    void onUp(View v, int value);

}
