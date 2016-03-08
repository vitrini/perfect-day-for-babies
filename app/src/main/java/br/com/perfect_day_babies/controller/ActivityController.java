package br.com.perfect_day_babies.controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import br.com.perfect_day_babies.Constants;
import br.com.perfect_day_babies.R;
import br.com.perfect_day_babies.entities.Vitrini;
import br.com.perfect_day_babies.persistence.DatabaseManager;
import br.com.perfect_day_babies.service.IVitriniService;
import br.com.perfect_day_babies.service.VitriniService;
import br.com.perfect_day_babies.utils.ApplicationContextProvider;

public class ActivityController {


	protected Activity currentActivity;
	
	protected IVitriniService vitriniService;
	public static int lastTabIssued = 0;
		
	
	public ActivityController(Activity mainActivity, DatabaseManager manager )	{
		super();
		
		currentActivity = mainActivity;
		vitriniService =  new VitriniService( manager, mainActivity.getResources() );
	}
	
	
//	public void goToLocationActivity() 	{
//		if ( currentActivity == null ) {
//			Log.e("CONTROLLER_ERROR", "Unnexpected null currentActivity at Controller");
//			return;
//		}
//
//		// Activity que mostra a imagem do mapa 
////		Intent locationIntent = new Intent( currentActivity, PhotoMapActivity.class );
//
//		// Activity que mostra o mapa em opengl 
//		Intent locationIntent = new Intent( currentActivity, GLMapActivity.class );		
//
//		// Lan�a a activity
//		currentActivity.startActivity( locationIntent );		
//	}
	

	public void setCurrentActivity(Activity activity) {
		currentActivity = activity;		
	}

	/**
	 * 
	 * @return
	 */
	public List<String> listVitriniNames() 
	{
		return vitriniService.listVitriniNames();
	}
	
	public List<String> listVitriniSegments() {
		return vitriniService.listVitriniSegments();
	}
	
	public List<String> listVitriniSegments(boolean considerJustFavorites) {
		return vitriniService.listVitriniSegments(considerJustFavorites);
	}
	
	public List<Vitrini> listVitrinis(String[] segmentsToFilter)
	{
		return vitriniService.listVitrinis(segmentsToFilter);
	}


	public void goToVitriniActivity(final String vitrini, final boolean newTask) {
		
		if ( currentActivity == null ) {
			Log.e("CONTROLLER_ERROR", "Unnexpected null currentActivity at Controller");
			return;
		}
		currentActivity.runOnUiThread( new Runnable() {			
			@Override
			public void run() {
				Intent intent = new Intent( currentActivity, VitriniActivity.class);				
				intent.putExtra(Constants.VITRINI_FIED_NAME, vitrini);
				if(newTask) {
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				}
				if(currentActivity.getParent() instanceof TabGroupActivity){
					startFromTabGroupParent("vitrini", 1, intent);
			  	    return;
				}		
				
				currentActivity.startActivityForResult(intent, 1 );

			}
		});
		
	}
	


	public void goToVitriniListActivity() {
		if ( currentActivity == null ) {
			Log.e("CONTROLLER_ERROR", "Unnexpected null currentActivity at Controller");
			return;
		}
		
		Intent locationIntent = new Intent( currentActivity, VitriniListActivity.class );
		
		currentActivity.startActivity( locationIntent );
		
	}

	public void goToFavoriteListActivity() {
		if ( currentActivity == null )
		{
			Log.e("CONTROLLER_ERROR", "Unnexpected null currentActivity at Controller");
			return;
		}
		
		Intent favoritesIntent = new Intent(currentActivity, VitriniListActivity.class);
		favoritesIntent.putExtra(Constants.RENDER_JUST_FAVORITES_FLAG, true);
		currentActivity.startActivity(favoritesIntent);
		
	}

	public List<String> filterVitrinisBySegment(String segment, boolean considerJustFavorites) {
		return vitriniService.filterVitrinisBySegment( segment, considerJustFavorites);
	}
	
	public List<Vitrini> filterVitrinis(String segment, boolean considerJustFavorites) {
		return vitriniService.filterVitrinis( segment, considerJustFavorites );
	}

	public List<Vitrini> listFavorites(String[] segmentsToFilter) {
		return vitriniService.listFavorites(segmentsToFilter);
	}
	
	public List<String> getFilteredVitrinis(String input, boolean considerJustFavorites) {
		return vitriniService.getFilteredVitrinis( input, considerJustFavorites );
	}
	
	public void updateFavoriteStatus(Vitrini vitrini, boolean favoriteChecked){
    	vitrini.setfavorite(favoriteChecked);
		if(vitrini.isFavorite())
			vitriniService.checkAsFavorite(vitrini);
		else
			vitriniService.uncheckAsFavorite(vitrini);
	}

	public Vitrini getVitriniByName(String vitriniName) {
		return vitriniService.getVitriniByName( vitriniName );
	}


	public void goToPhotoMapActivity() {
		if ( currentActivity == null ) {
			Log.e("CONTROLLER_ERROR", "Unnexpected null currentActivity at Controller");
			return;
		}
		
		hideKeyboard();
		
		Intent locationIntent = new Intent( currentActivity, MapTabGroupActivity.class );
		currentActivity.startActivity( locationIntent );		
	}
	
	public void goToPhotoMapActivity( final String vitriniName ) {
		Intent locationIntent = new Intent( currentActivity, PhotoMapActivity.class );
		locationIntent.putExtra(Constants.VITRINI_FIED_NAME, vitriniName);
		if ( currentActivity == null )
		{
			Log.e("CONTROLLER_ERROR", "Unnexpected null currentActivity at Controller");
			return;
		}
				
		startFromTabGroupParent("PhotoMap", 1, locationIntent);		
	}
	
	public void configEventTab(VitriniTabActivity vitriniTabActivity) {
		Intent eventIntent = 
				new Intent(vitriniTabActivity.getApplicationContext(), MainActivity.class);
		
		vitriniTabActivity.addTab(R.string.tab_1, R.drawable.event_tab_info, eventIntent);
	}
	
	public void configVitriniListTab(VitriniTabActivity vitriniTabActivity) {
		Intent menuIntent = 
				new Intent(vitriniTabActivity.getApplicationContext(), VitriniTabGroupActivity.class);
		menuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		vitriniTabActivity.addTab(R.string.tab_2, R.drawable.vitrinis_tab_info, menuIntent);
	
	}
	
	public void configMapTab(VitriniTabActivity vitriniTabActivity) {
		Intent menuIntent = 
				new Intent(vitriniTabActivity.getApplicationContext(), MapTabGroupActivity.class);
		menuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		vitriniTabActivity.addTab(R.string.tab_3, R.drawable.map_tab_info, menuIntent);
	}
	
	public void configFavoritesTab(VitriniTabActivity vitriniTabActivity) {
		Intent favoritesIntent = new Intent(currentActivity, VitriniTabGroupActivity.class);
		favoritesIntent.putExtra(Constants.RENDER_JUST_FAVORITES_FLAG, true);
		favoritesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		vitriniTabActivity.addTab(R.string.tab_4, R.drawable.favorites_tab_info, favoritesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}
	
	private void startFromTabGroupParent(String label, int parentLevel, Intent intent) {
		
		Activity parentActivity = currentActivity;
		for (int i = 0; i < parentLevel; i++) {
			parentActivity = currentActivity.getParent();			
		}
		TabGroupActivity parentTabGroupActivity = (TabGroupActivity)parentActivity;
  	    parentTabGroupActivity.startChildActivity(label, intent);  	    
	}


	public void goToSegmentFilteringActivity(String[] checkedSegments, boolean mJustFavorites) {
		Intent intent = new Intent(currentActivity, SegmentFilteringActivity.class);
		if(checkedSegments != null) {
			intent.putExtra(Constants.PREVIOUS_FILTER, Arrays.copyOf(checkedSegments, checkedSegments.length));
		}		
		intent.putExtra(Constants.RENDER_JUST_FAVORITES_FLAG, mJustFavorites);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if(currentActivity.getParent() instanceof TabGroupActivity){
			startFromTabGroupParent("segments", 1, intent);
	  	    return;
		}		
		currentActivity.startActivity(intent);
	}


	public void goToVitriniListActivity(List<String> checkedSegments, boolean mJustFavorites) {
		Intent intent = new Intent(currentActivity, VitriniListActivity.class);
		intent.putExtra(Constants.RENDER_JUST_FAVORITES_FLAG, mJustFavorites);
		intent.putExtra(Constants.SEGMENTS_TO_FILTER, checkedSegments.toArray(new String[checkedSegments.size()]));
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if(currentActivity.getParent() instanceof TabGroupActivity){
			startFromTabGroupParent("vitriniList", 1, intent);
	  	    return;
		}
		currentActivity.startActivity(intent);
	}


	public static void pushTabCall(ApplicationContextProvider provider, int currentTab) {
		
		if(provider.tabCallStack.isEmpty()) {
			provider.tabCallStack.add(currentTab);
		} else {
			if(provider.tabCallStack.get(provider.tabCallStack.size() - 1) != currentTab) {
				provider.tabCallStack.add(currentTab);
			}
		}
		if(provider.tabCallStack.size() == Constants.MAX_CALL_STACK_SIZE) {
			provider.tabCallStack = new LinkedList<Integer>();
			provider.tabCallStack.add(0);
			if(currentTab != 0) {
				provider.tabCallStack.add(currentTab);
			}
		}
		Log.i("stack push", provider.tabCallStack.toString());
	}
	
	public static int getLastCalledTab(ApplicationContextProvider provider) {		
		if(!provider.tabCallStack.isEmpty()) {
			int lastTab = provider.tabCallStack.remove(provider.tabCallStack.size() - 1);
			Log.e("stack", provider.tabCallStack.toString());
			return lastTab;
		}
		Log.e("stack", provider.tabCallStack.toString());
		return -1;
	}
	
	public void hideKeyboard()
	{
		final AutoCompleteTextView autocompleteVitriniNames = (AutoCompleteTextView) currentActivity.findViewById(R.id.auto_complete_search_box);

		//Esconde o keyboard
		@SuppressWarnings("static-access")
		InputMethodManager imm = (InputMethodManager)currentActivity.getSystemService(currentActivity.getApplicationContext().INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(autocompleteVitriniNames.getWindowToken(), 0);						
	}
}
