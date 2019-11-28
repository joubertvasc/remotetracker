package jv.android.utils.customComponents.customLinearLayout;

import jv.android.utils.R;
import jv.android.utils.customComponents.BorderColors;
import jv.android.utils.customComponents.CustomComponentUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class CustomLinearLayout extends LinearLayout {

	private Context context;

	public CustomLinearLayout(Context context) {
		super(context);

		this.context = context;

		setCustomValues(null);
	}

	public CustomLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;

		setCustomValues(attrs);
	}

	public CustomLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.context = context;

		setCustomValues(attrs);
	}

	private void setCustomValues(AttributeSet attrs) {
		setBackgroundResource(R.drawable.generic_rounded_shape_blue);
		setOrientation(LinearLayout.HORIZONTAL);
	    setGravity(Gravity.CENTER_VERTICAL);
	    
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomLinearLayout);
			try {
				boolean corners = a.getInt(R.styleable.CustomLinearLayout_Corners, 0) == 0;
				boolean solid = a.getBoolean(R.styleable.CustomLinearLayout_Solid, false);
				BorderColors bc = BorderColors.values()[a.getInt(R.styleable.CustomLinearLayout_Background_Color, BorderColors.BLUE.getValue())];
				setBackgroundResource(CustomComponentUtils.getGenericBackGroundResourceFromAttr(corners, solid, bc));
			} finally {
				a.recycle();
			}
		}
	}

}
