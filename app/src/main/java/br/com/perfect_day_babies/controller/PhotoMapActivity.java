package br.com.perfect_day_babies.controller;

import java.util.ArrayList;

import java.util.List;

import br.com.perfect_day_babies.Constants;
import br.com.perfect_day_babies.R;
import br.com.perfect_day_babies.entities.Location;
import br.com.perfect_day_babies.entities.Vitrini;
import br.com.perfect_day_babies.persistence.LocationDBAdapter;
import br.com.perfect_day_babies.persistence.VitriniDBAdapter;
import br.com.perfect_day_babies.view.adapters.PhotoMapSearchAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("JavascriptInterface")
public class PhotoMapActivity extends AbstractActivity {


	private VitriniDBAdapter vitriniDbAdapter;
	private LocationDBAdapter locationDBAdapter;

	private JsInterface myJavaScriptInterface;
	private WebView mapWebView;

	protected ArrayAdapter<String> categoriesAdapter;

	protected ImageButton goToVitriniBtn;

	protected String vitriniName;

	private String[] segmentsToFilter;

	private PhotoMapSearchAdapter vAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_map);

		initDatabaseAndServices();

		locationDBAdapter =  databaseManager.getLocationAdapter();
		vitriniDbAdapter = databaseManager.getVitriniAdapter();

		//capture parameters 
		final Intent intent = getIntent();
		final Bundle extras = intent.getExtras();
		if (extras != null )
		{
			vitriniName = extras.getString(Constants.VITRINI_FIED_NAME);
		}

		initView();		
	}

	@Override
	protected void onResume() {    	
		super.onResume();
		loadVitrinisAdapter(getVitrinis());
		//    	Activity parentActivity = this;
		//    	if(!(this.getParent() instanceof MapTabGroupActivity)) {
		//    		while (parentActivity != null) {
		//        		parentActivity = parentActivity.getParent();
		//        		if(parentActivity instanceof VitriniTabActivity) {
		//        			((VitriniTabActivity)parentActivity).selectMapView();
		//        		}
		//    		}    		
		//    	}    	
	}

	private List<Vitrini> getVitrinis(){
		List<Vitrini> vitrinis =  new ArrayList<Vitrini>();

		vitrinis = controller.listVitrinis(segmentsToFilter);

		return vitrinis;
	}

	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	@Override
	@JavascriptInterface
	protected void initView() 
	{
		mapWebView = (WebView)findViewById( R.id.web_view_map);

		WebSettings settings = mapWebView.getSettings();
		settings.setUseWideViewPort(true); 
		settings.setLoadWithOverviewMode(true); 
		settings.setSupportZoom(true); 
		settings.setBuiltInZoomControls(true); 


		myJavaScriptInterface = new JsInterface(this, controller);
		mapWebView.addJavascriptInterface(myJavaScriptInterface, "VitriniControllerInterface");

		settings.setJavaScriptEnabled(true); 

		String url = "file:///android_asset/webviewmap/mapa.html";

		mapWebView.loadUrl(url);
		mapWebView.setWebViewClient( new MapWebViewClient());

		List<Vitrini> vitrinis =  getVitrinis();
		loadVitrinisAdapter(vitrinis);
		initAutoCompleteFocusVitrini();
		
		mapWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

	}

	private void initAutoCompleteFocusVitrini() {

		vAdapter.setDropDownViewResource(R.id.segments_list_view);

		final RelativeLayout searchArea = (RelativeLayout) findViewById(R.id.search_area_layout);
		final AutoCompleteTextView autocompleteVitriniNames = (AutoCompleteTextView) findViewById(R.id.photo_map_auto_complete_search_box);
		final ImageView iconSearch = (ImageView) findViewById( R.id.icon_search );
		final ImageButton closeButton = (ImageButton) findViewById( R.id.close_search_button );
		final Button cancelButton = (Button) findViewById( R.id.cancel_search_button );

		autocompleteVitriniNames.setAdapter(this.vAdapter);
		autocompleteVitriniNames.setThreshold(1);
		autocompleteVitriniNames.setCursorVisible(false);


		closeButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				autocompleteVitriniNames.setText("");
				autocompleteVitriniNames.setHint("");	
			}
		});


		cancelButton.setOnClickListener( new OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View arg0) {
				autocompleteVitriniNames.setCursorVisible(false);
				iconSearch.setVisibility(View.VISIBLE);
				closeButton.setVisibility(View.INVISIBLE);
				autocompleteVitriniNames.setText("       Procurar estande");
				searchArea.setBackgroundResource(R.drawable.filter_text_area);
				cancelButton.setVisibility(View.GONE);

				//Esconde o keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(autocompleteVitriniNames.getWindowToken(), 0);					
			}
		});

		autocompleteVitriniNames.setOnFocusChangeListener( new View.OnFocusChangeListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus)
				{
					//Esconde o keyboard
					InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(autocompleteVitriniNames.getWindowToken(), 0);
				}
			}
		});


		autocompleteVitriniNames.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				autocompleteVitriniNames.setCursorVisible(true);
				iconSearch.setVisibility(View.INVISIBLE);
				closeButton.setVisibility(View.VISIBLE);
				autocompleteVitriniNames.setText("");
				autocompleteVitriniNames.setHint("");				
				searchArea.setBackgroundResource(R.drawable.filter_text_area_selected);
				cancelButton.setVisibility(View.VISIBLE);
				return false;
			}
		});

		autocompleteVitriniNames.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onItemClick (AdapterView<?> parent, View arg1, int pos, long id) {
				autocompleteVitriniNames.setCursorVisible(false);
				iconSearch.setVisibility(View.VISIBLE);
				closeButton.setVisibility(View.INVISIBLE);
				autocompleteVitriniNames.setText("       Procurar estande");
				searchArea.setBackgroundResource(R.drawable.filter_text_area);
				cancelButton.setVisibility(View.GONE);

				//Esconde o keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(autocompleteVitriniNames.getWindowToken(), 0);					


				Vitrini vitrini = (Vitrini) autocompleteVitriniNames.getAdapter().getItem(pos);
				final String vitriniName = vitrini.getName();

				loadVitrinisAdapter(getVitrinis());
				if(!vitriniName.trim().isEmpty()) {
					Boolean mJustFavorites = false;
					List<String> filteredVitrinis = controller.getFilteredVitrinis(vitriniName, mJustFavorites);
					vAdapter.filterVitrinisByName(filteredVitrinis);
					vAdapter.notifyDataSetChanged();
				}

				myJavaScriptInterface.focusVitrini(vitriniName);

			}
		});	
	}

	private void loadVitrinisAdapter(List<Vitrini> vitrinis){

		if(vAdapter == null){
			vAdapter = new PhotoMapSearchAdapter(getApplicationContext(), vitrinis, getResources(), controller);
		}
	}

	private final class MapWebViewClient extends WebViewClient {
		public void onPageFinished(WebView view, String url) 
		{

			StringBuffer locations = new StringBuffer(); 
			List<Vitrini> vitrinis = vitriniDbAdapter.listVitrinis();
			for ( Vitrini vitrini : vitrinis  )
			{
				List<Location> vitriniLocations = locationDBAdapter.getLocationsListByVitrini(vitrini );
				if ( vitriniLocations == null || vitriniLocations.isEmpty() )
				{
					continue;
				}

				for ( Location loc : vitriniLocations )
				{
					
					//A associação com o map esta direto no webview, no renderer.js
					myJavaScriptInterface.plotLocationFromVitrini(loc, vitrini);
					
					String parameters = String.format("\"%s\", \"%s\", \"%s\", \"%s\"", 
							vitrini.getId(), vitrini.getName(), loc.getId(), vitrini.getSegment());

					locations.append("showVitrini(" + parameters + ");\n");
				}
			}

			//reset fitlers
			//mapWebView.loadUrl("javascript:resetSegmentFilter()");

			//Log.i("MAPA", locations.toString());

			if ( vitriniName != null && vitriniName != "" )
			{
				myJavaScriptInterface.focusVitrini(vitriniName);
			}

		}
	}

	public class JsInterface 
	{
		Context mContext;
		ActivityController controller;


		JsInterface(Context c, ActivityController controllerArg) 
		{
			mContext = c;
			controller = controllerArg;
		}

		@JavascriptInterface
		public void centerMapInPoint(String s_posX, String s_posY)
		{
			final float win_width  = mapWebView.getWidth();
			final float win_height = mapWebView.getHeight();

			final int posX = Integer.parseInt(s_posX);
			final int posY = Integer.parseInt(s_posY);

			runOnUiThread( new Runnable() {
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					mapWebView.scrollTo((int)((posX * mapWebView.getScale()) - (win_width  / 2)),
							(int)((posY * mapWebView.getScale()) - (win_height / 2)));
				}
			});
		}

		/**
		 * called by javascript
		 * @see assets/renderer.js
		 * @param vitriniName
		 */
		@JavascriptInterface
		public void goToVitrini(String vitriniName)
		{
			if (vitriniName.equals("undefined"))
			{
				return;
			}

			controller.goToVitriniActivity(vitriniName, true);
		}

		/**
		 * called by javascript
		 * @see assets/renderer.js
		 * @param vitriniName
		 */
		@JavascriptInterface
		public void setScroll( String posX, String posY, String width, String height )
		{
			if (posX.equals("undefined") || posY.equals("undefined"))
			{
				return;
			}

			final String positionX = posX;
			final String positionY = posY;

			final int wPx = mapWebView.getWidth();
			final int hPy = mapWebView.getHeight();

			final int poxX = Integer.parseInt( positionX );
			final int posyY = Integer.parseInt( positionY );

			runOnUiThread( new Runnable() {

				@Override
				public void run() {

					while (mapWebView.zoomIn())
					{
						mapWebView.zoomIn();
					}

					mapWebView.scrollBy(Math.abs(poxX - (wPx/2)), Math.abs( posyY - (hPy/2)));
					while (mapWebView.zoomOut())
					{
						mapWebView.zoomOut();
					}
				}
			});

		}

		/**
		 * This functions is called by the native code
		 * @param loc
		 * @param vitrini
		 */
		@JavascriptInterface
		public void plotLocationFromVitrini(Location loc, Vitrini vitrini) 
		{
			String parameters = String.format("\"%s\", \"%s\", \"%s\", \"%s\"", 
					vitrini.getId(), vitrini.getName(), loc.getId(), vitrini.getSegment());

			String url = "javascript:showVitrini(" + parameters + ")";

			mapWebView.loadUrl(url);

		}

		/**
		 * This functions is called by the native code
		 * @param loc
		 * @param vitrini
		 */
		@JavascriptInterface
		public void focusVitrini(final String vitriniName) {
			String parameters = String.format("\"%s\"", vitriniName );

			String url = "javascript:focusVitrini(" + parameters + ")";

			mapWebView.loadUrl(url);
		}
	}

	//	@Override
	//	protected void onPause() {
	//		Activity parentActivity = this;
	//    	if(!(this.getParent() instanceof MapTabGroupActivity)) {
	//    		while (parentActivity != null) {
	//        		parentActivity = parentActivity.getParent();
	//        		if(parentActivity instanceof VitriniTabActivity) {
	//        			((VitriniTabActivity)parentActivity).unselectMapView();
	//        		}
	//    		}    		
	//    	}
	//		super.onPause();
	//	}
	//
	//	@Override
	//	protected void onStop() {
	//		Activity parentActivity = this;
	//    	if(!(this.getParent() instanceof MapTabGroupActivity)) {
	//    		while (parentActivity != null) {
	//        		parentActivity = parentActivity.getParent();
	//        		if(parentActivity instanceof VitriniTabActivity) {
	//        			((VitriniTabActivity)parentActivity).unselectMapView();
	//        		}
	//    		}    		
	//    	}
	//		super.onStop();
	//	}

}
