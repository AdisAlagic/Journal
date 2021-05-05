package com.adisalagic.journal;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class ScrollUpButton extends CardView {
    private View rootView;

    public ScrollUpButton(@NonNull Context context) {
        super(context);
        init(null, 0);
    }

    public ScrollUpButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ScrollUpButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attributeSet, int defStyle){
        TypedArray a = getContext().obtainStyledAttributes(
                attributeSet, R.styleable.ScrollUpButton, defStyle, 0);
        a.recycle();
        rootView = inflate(getContext(), R.layout.scrollup_button, this);
        setPreventCornerOverlap(true);
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
    }
}
