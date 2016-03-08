package br.com.perfect_day_babies.controller;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import br.com.perfect_day_babies.Constants;
import br.com.perfect_day_babies.R;
import br.com.perfect_day_babies.entities.Vitrini;
import br.com.perfect_day_babies.view.adapters.VitriniListAdapter;

public class VitriniListActivity extends AbstractActivity {
	private boolean mJustFavorites;
	protected int previousTabIndex = 0;
	private String[] segmentsToFilter;
	
    private List<Vitrini> vItems = null;
    private VitriniListAdapter vAdapter;

    
    protected ArrayAdapter<String> categoriesAdapter;    
    protected EditText vitriniFinderInputEdtTxt;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitrini_list);
        
        final Intent intent = getIntent();
		mJustFavorites = intent.getBooleanExtra(Constants.RENDER_JUST_FAVORITES_FLAG, false);
		segmentsToFilter = intent.getStringArrayExtra(Constants.SEGMENTS_TO_FILTER);
        initDatabaseAndServices();        
        
        initView();        
    }
    
  
    
    @Override
    protected void onResume() {    	
    	super.onResume();
    	final Intent intent = getIntent();
		mJustFavorites = intent.getBooleanExtra(Constants.RENDER_JUST_FAVORITES_FLAG, false);
		segmentsToFilter = intent.getStringArrayExtra(Constants.SEGMENTS_TO_FILTER);
    	loadVitrinisAdapter(getVitrinis());   	
    }    

	private void loadVitrinisAdapter(List<Vitrini> vitrinis){
        
    	if(vAdapter == null){
    		vAdapter = new VitriniListAdapter(getApplicationContext(), vItems, getResources(), controller);
    	}
        vAdapter.clear();
        for(int i=0;i<vItems.size();i++)
        	vAdapter.add(vItems.get(i));
        vAdapter.notifyDataSetChanged();    	
    }
    
    private List<Vitrini> getVitrinis(){
    	List<Vitrini> vitrinis =  new ArrayList<Vitrini>();
    	if(mJustFavorites) {
    		vitrinis = controller.listFavorites(segmentsToFilter);
    	} else {
    		vitrinis = controller.listVitrinis(segmentsToFilter);
    	}
    	vItems = new ArrayList<Vitrini>( vitrinis);    
    	return vitrinis;
    }
    
	public void initView() {

    	final ListView vitriniListView =  (ListView) findViewById( R.id.list_view_vitrini_list );    	
    	vitriniListView.setItemsCanFocus(true);

    	List<Vitrini> vitrinis =  getVitrinis();
    	    	
    	loadVitrinisAdapter(vitrinis);    	
    	
    	initVitriniListView(vitriniListView);
    	
    	initSegemntsButton();
    	initAutoCompleteSearchBox();
    		
	}

	private void initSegemntsButton() {
		final View segmentSearchButton = findViewById(R.id.button_segment);
		segmentSearchButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				//Esconde o keyboard
				controller.hideKeyboard();
				
				controller.goToSegmentFilteringActivity(segmentsToFilter, mJustFavorites);				
			}
		});
	}


	private void initVitriniListView(ListView vitriniListView) {
		
		vitriniListView.setAdapter(this.vAdapter);
		vitriniListView.setOnItemClickListener(new OnItemClickListener() 
		{   
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id)
			{
				//Esconde o keyboard
				controller.hideKeyboard();
				
				Vitrini selectedItem = (Vitrini)vAdapter.getItem(position); 	     	    
				final String vitriniName = selectedItem.getName();				
				controller.goToVitriniActivity(vitriniName, false);
			}
		});		
		


		//    	vitriniListView.setAdapter(vitrinisAdapter );
		//    	vitriniListView.setOnItemClickListener( new OnItemClickListener() {
		//
		//			@Override
		//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		//					long arg3) 
		//			{
		//				
		//				if ( ! (arg1 instanceof TextView ) )
		//				{
		//					return;
		//				}
		//
		//				TextView textSelected = (TextView) arg1;
		//
		//				final String vitriniName = textSelected.getText().toString();
		//				
		//				controller.goToVitriniActivity(vitriniName);
		//			}
		//		});
	}
	
	private void initAutoCompleteSearchBox() {
		//autocomplete

//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, boothNames );
//		adapter.setDropDownViewResource(R.id.segments_list_view);

		final RelativeLayout searchArea = (RelativeLayout) findViewById(R.id.search_area_layout);
		final AutoCompleteTextView autocompleteVitriniNames = (AutoCompleteTextView) findViewById(R.id.auto_complete_search_box);
		final ImageView iconSearch = (ImageView) findViewById( R.id.icon_search );
		final ImageButton closeButton = (ImageButton) findViewById( R.id.close_search_button );
		final Button cancelButton = (Button) findViewById( R.id.cancel_search_button );
		
		autocompleteVitriniNames.setAdapter(vAdapter);
		autocompleteVitriniNames.setThreshold(1);
		autocompleteVitriniNames.setCursorVisible(false);
		autocompleteVitriniNames.setDropDownHeight(0);
		
		closeButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				autocompleteVitriniNames.setText("");				
				autocompleteVitriniNames.setHint("");				
			}
		});


		cancelButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				autocompleteVitriniNames.setCursorVisible(false);
				iconSearch.setVisibility(View.VISIBLE);
				closeButton.setVisibility(View.INVISIBLE);
				autocompleteVitriniNames.setText("");
				autocompleteVitriniNames.setHint("       Procurar estande");
				searchArea.setBackgroundResource(R.drawable.filter_text_area);
				cancelButton.setVisibility(View.GONE);
				
//				// Recarrega a lista de vitrinis
//				loadVitrinisAdapter(getVitrinis());   	
////				if(!vitriniName.trim().isEmpty()) {
////					List<String> filteredVitrinis = controller.getFilteredVitrinis(vitriniName, mJustFavorites);
////					vAdapter.filterVitrinisByName(filteredVitrinis);
//				vAdapter.resetList();
//					vAdapter.notifyDataSetChanged();
////				}
//				
				//Esconde o keyboard
				controller.hideKeyboard();
				
			}
		});
		
//		autocompleteVitriniNames.setOnFocusChangeListener( new View.OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (!hasFocus)
//					hideKeyboard(autocompleteVitriniNames);
//				
//			}
//		} );
//		autocompleteVitriniNames.setOnClickListener( new OnClickListener(){
//
//			@Override
//			public void onClick(View arg0) {
//				autocompleteVitriniNames.setCursorVisible(true);
//				iconSearch.setVisibility(View.INVISIBLE);
//				closeButton.setVisibility(View.VISIBLE);
//				autocompleteVitriniNames.setText("");
//				autocompleteVitriniNames.setHint("");				
//				searchArea.setBackgroundResource(R.drawable.filter_text_area_selected);
//				cancelButton.setVisibility(View.VISIBLE);
//			}
//		});
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

			@Override
			public void onItemClick (AdapterView<?> parent, View arg1, int pos, long id) {
				
				AutoCompleteTextView autocompleteVitriniNames = (AutoCompleteTextView) findViewById(R.id.auto_complete_search_box);				
				Vitrini vitrini = (Vitrini) autocompleteVitriniNames.getAdapter().getItem(pos);
				final String vitriniName = vitrini.getName();
				
				loadVitrinisAdapter(getVitrinis());
				if(!vitriniName.trim().isEmpty()) {
					List<String> filteredVitrinis = controller.getFilteredVitrinis(vitriniName, mJustFavorites);
					vAdapter.filterVitrinisByName(filteredVitrinis);
					vAdapter.notifyDataSetChanged();
				}
				
				//Esconde o keyboard
				controller.hideKeyboard();

				controller.goToVitriniActivity(vitriniName, false);				
			}
		});

	}
}
