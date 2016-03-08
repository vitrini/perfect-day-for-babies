package br.com.perfect_day_babies.controller;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import br.com.perfect_day_babies.Constants;
import br.com.perfect_day_babies.R;
import br.com.perfect_day_babies.view.adapters.SegmentListAdapter;

public class SegmentFilteringActivity extends AbstractActivity {
	private boolean mJustFavorites;
	private String[] previousCheckedSegments;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.segment_filtering_list);
        
        
        initDatabaseAndServices();
    }
    
    @Override
    protected void onResume() {    	
    	super.onResume();
    	final Intent intent = getIntent();
		mJustFavorites = intent.getBooleanExtra(Constants.RENDER_JUST_FAVORITES_FLAG, false);
		previousCheckedSegments = intent.getStringArrayExtra(Constants.PREVIOUS_FILTER);
    	loadSegmentsAdapter();
    	initView();
    }
    
    private SegmentListAdapter sAdapter;   
    
    private void loadSegmentsAdapter(){
    	
    	List<String> segmentList = this.controller.listVitriniSegments(mJustFavorites);
    	
    	sAdapter = new SegmentListAdapter(this, getResources(), segmentList, previousCheckedSegments);
    	
    	ListView segmentsListView = (ListView)findViewById(R.id.segments_list_view);
        segmentsListView.setAdapter(sAdapter);        
        sAdapter.notifyDataSetChanged();
    }   
    
	public void initView() {

    	initFilterButtons();
    	setupSelectAllCheckbox();
	}
	
	private void setupSelectAllCheckbox() {
		final CheckBox selectAllCheckbox = (CheckBox)findViewById(R.id.checkbox_select_all);
		selectAllCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton view, boolean isChecked) {
				ListView segmentsListView = (ListView)SegmentFilteringActivity.this.findViewById(R.id.segments_list_view);
				for (int listItemIndex = 0; listItemIndex < segmentsListView.getCount(); listItemIndex++) {					
					RelativeLayout listItem = (RelativeLayout)segmentsListView.getAdapter().getView(listItemIndex, segmentsListView.getChildAt(listItemIndex), segmentsListView);
					CheckBox checkbox = (CheckBox)listItem.getChildAt(2);
					checkbox.setChecked(isChecked);					
				}
				selectAllCheckbox.setChecked( isChecked );
			}
		});
		
		((View)selectAllCheckbox.getParent()).setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {				
				if(selectAllCheckbox.isChecked()) {
					selectAllCheckbox.setChecked(false);
				} else {
					selectAllCheckbox.setChecked(true);
				}
			}				
		});
		
		if(previousCheckedSegments != null && previousCheckedSegments.length == sAdapter.getCount()) {
			selectAllCheckbox.setChecked(true);
		}
	}


	private void initFilterButtons() {
		Button filterButtonOk = (Button) findViewById(R.id.filter_button_ok);
		filterButtonOk.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				controller.goToVitriniListActivity(sAdapter.getCheckedSegments(), mJustFavorites);
			}
		});
		
		Button filterButtonCancel = (Button) findViewById(R.id.filter_button_cancel);
		filterButtonCancel.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				controller.goToVitriniListActivity(new ArrayList<String>(), mJustFavorites);							
			}
		});
	}
}
