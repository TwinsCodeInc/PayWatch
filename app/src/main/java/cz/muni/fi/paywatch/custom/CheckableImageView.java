package cz.muni.fi.paywatch.custom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

import cz.muni.fi.paywatch.R;

/**
 * Created by Daniel on 14. 5. 2017.
 */

public class CheckableImageView extends ImageView implements Checkable {
    private boolean checked = false;
    private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };

    public CheckableImageView(Context context) {
        super(context);
    }

    public CheckableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked())
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        return drawableState;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void setChecked(boolean _checked) {
        checked = _checked;
        refreshDrawableState();
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }

}