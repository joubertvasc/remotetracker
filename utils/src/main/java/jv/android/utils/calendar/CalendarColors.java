package jv.android.utils.calendar;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

public class CalendarColors {

	private List<Cor> cores;
	
	public CalendarColors() {
		cores = new ArrayList<Cor>();
		
		cores.add(new Cor(Color.rgb( 84, 132, 237), -11238163, 9));   // DARK BLUE
		cores.add(new Cor(Color.rgb(164, 189, 252), -5980676 , 1));   // LIGHT BLUE
		cores.add(new Cor(Color.rgb( 70, 214, 219), -12134693, 7));   // CYAN
		cores.add(new Cor(Color.rgb(122, 231, 191), -8722497 , 2));   // LIGHT GREEN 
		cores.add(new Cor(Color.rgb( 81, 183,  73), -11421879, 10));  // DARK GREEN
		cores.add(new Cor(Color.rgb(251, 215,  91), -272549  , 5));   // YELLOW
		cores.add(new Cor(Color.rgb(255, 184, 120), -18312   , 6));   // ORANGE
		cores.add(new Cor(Color.rgb(255, 136, 124), -30596   , 4));   // PINK
		cores.add(new Cor(Color.rgb(220,  33,  39), -2350809 , 11));  // RED
		cores.add(new Cor(Color.rgb(219, 173, 255), -2380289 , 3));   // PURPLE
		cores.add(new Cor(Color.rgb(225, 225, 225), -1973791 , 8));   // LIGHT GRAY
	}

	public List<Cor> getColors() {
		return cores;
	}
	
	public class Cor {
		
		private int colorToShow;
		private int colorId;
		private int colorKey;
		
		public Cor(int colorToShow, int colorId, int colorKey) {
			this.colorToShow = colorToShow;
			this.colorId = colorId;
			this.colorKey = colorKey;
		}
		
		public int getColorToShow() {
			return colorToShow;
		}
		
		public int getColorId() {
			return colorId;
		}
		
		public int getColorKey() {
			return colorKey;
		}
	}
}
