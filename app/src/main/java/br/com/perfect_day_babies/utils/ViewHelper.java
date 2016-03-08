package br.com.perfect_day_babies.utils;

import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabWidget;

public class ViewHelper {
//	private static int heightDiscount = 60;
	public static void resizeTabWidgetForDevice(TabWidget tabWidget, WindowManager windowManager) {
		DisplayMetrics metrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metrics);		
		int windowWidth = metrics.widthPixels;
		
		tabWidget.getLayoutParams().width = windowWidth;
		int totalOfTabs = tabWidget.getChildCount();
		for (int tabIndex = 0; tabIndex < totalOfTabs; tabIndex++) {
			LayoutParams actualParams = tabWidget.getLayoutParams();
			tabWidget.getChildAt(tabIndex).setLayoutParams(new
	                LinearLayout.LayoutParams(windowWidth/totalOfTabs,actualParams.height));
			
		}		
	}

}
