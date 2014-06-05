package com.scopely.fontain.transformationmethods;

import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;

import static com.scopely.fontain.utils.FontViewUtils.capitalizeCharSequence;


public enum CapsTransformationMethod implements TransformationMethod {
    NONE,
    CHARACTER,
    WORD,
    SENTENCE
    ;

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return capitalizeCharSequence(source, ordinal());
    }

    @Override
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {
        //no op
    }
}