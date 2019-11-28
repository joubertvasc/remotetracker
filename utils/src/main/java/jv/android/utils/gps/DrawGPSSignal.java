package jv.android.utils.gps;

import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

public class DrawGPSSignal {

	public static void drawChart(Context context, LinearLayout linearChart, List<GPSSignal> signals) {
		if (linearChart != null) {		
			linearChart.removeAllViews();

			int h = linearChart.getMeasuredHeight();
			int w = linearChart.getMeasuredWidth();

			for (int k = 1; k <= signals.size(); k++) {
				View view = new View(context);

				if (signals.get(k).isUsedInFix()) {
					view.setBackgroundColor(Color.GRAY);
				} else if (signals.get(k).getStrength() < h/4) {
					view.setBackgroundColor(Color.RED);
				} else if (signals.get(k).getStrength() < h /2) {
					view.setBackgroundColor(Color.YELLOW);
				} else {
					view.setBackgroundColor(Color.GREEN);
				}

				view.setLayoutParams(new LinearLayout.LayoutParams((w / signals.size()) -3, (int)signals.get(k).getStrength()));
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
				params.setMargins(3, h - (int)signals.get(k).getStrength(), 0, 0); 
				view.setLayoutParams(params);
				linearChart.addView(view);
			}
		}
	}	
}
