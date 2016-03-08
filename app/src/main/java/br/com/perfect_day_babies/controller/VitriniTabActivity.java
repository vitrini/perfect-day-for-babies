package br.com.perfect_day_babies.controller;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import br.com.perfect_day_babies.Constants;
import br.com.perfect_day_babies.R;
import br.com.perfect_day_babies.persistence.DatabaseManager;
import br.com.perfect_day_babies.utils.ApplicationContextProvider;
import br.com.perfect_day_babies.utils.ViewHelper;

@SuppressWarnings("deprecation")
public class VitriniTabActivity extends TabActivity
{
	public static String SELECTED_RESTAURANT_ID = "SELECTED_RESTAURANT_ID";	
	
	public OnClickListener tabActivationListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ActivityController.pushTabCall((ApplicationContextProvider)getApplicationContext(), getTabHost().getCurrentTab());
			setCurrentTab(getTabWidget().indexOfChild(v));
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Log.i("VitriniTabActivity.OnCreate", "CALLED");
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.tab_host);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
		
		TabHost tabHost = getTabHost();
		tabHost.getTabWidget().setDividerDrawable(null);		
		ActivityController controller = new ActivityController(this, DatabaseManager.getInstance());	
		controller.configEventTab(this);
		controller.configVitriniListTab(this);
		controller.configMapTab(this);
		controller.configFavoritesTab(this);
		ViewHelper.resizeTabWidgetForDevice(tabHost.getTabWidget(), getWindowManager());
		//set Windows tab as default (zero based)
		int selectedTab = getIntent().getIntExtra(Constants.SELECTED_TAB, 0);
		tabHost.setCurrentTab(selectedTab);
		
		tabHost.requestLayout();
	}
	
	public void setCurrentTab(int index) {
		TabHost tabHost = getTabHost();
		tabHost.setCurrentTab(index);
		tabHost.requestLayout();
	}
	
	public void addTab(int labelId, int drawableId, Intent intent) {
		
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);		
		
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		
		TextView title = (TextView) tabIndicator.findViewById(R.id.tab_title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);		
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);		
		tabHost.addTab(spec);
		tabIndicator.setOnClickListener(this.tabActivationListener);
	}
	
	public void selectMapView() {
		changeTabView(2, R.drawable.tab_map_icon_selected, R.color.text_tab_selected, true);
		changeCurrentTabView(false);
	}
	
	public void unselectMapView() {
		changeTabView(2, R.drawable.map_tab_info, R.color.tab_indicator_text, false);
		changeCurrentTabView(true);
	}
	private void changeTabView(int tabIndex, int drawableId, int colorId, boolean select) {
		View tabIndicator = getTabWidget().getChildAt(tabIndex);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		TextView title = (TextView) tabIndicator.findViewById(R.id.tab_title);
		if(!select) {
			title.setBackgroundResource(colorId);
		} else {
			title.setTextColor(getResources().getColor(colorId));
		}
		tabIndicator.invalidate();
	}	
	
	private void changeCurrentTabView(boolean select) {
		View currentTabView = getTabHost().getCurrentTabView();
		switch (getTabWidget().indexOfChild(currentTabView)) {
		case 1:
			if(select) {
				changeTabView(1, R.drawable.tab_vitrinis_icon_selected, R.color.text_tab_selected, select);
			} else {
				changeTabView(1, R.drawable.vitrinis_tab_info, R.color.tab_indicator_text, select);
			}			
			break;
		case 3:
			if(select) {
				changeTabView(3, R.drawable.tab_favorites_icon_selected, R.color.text_tab_selected, select);
			} else {
				changeTabView(3, R.drawable.favorites_tab_info, R.color.tab_indicator_text, select);
			}
			break;
		default:
			break;
		}
	}
	
}
