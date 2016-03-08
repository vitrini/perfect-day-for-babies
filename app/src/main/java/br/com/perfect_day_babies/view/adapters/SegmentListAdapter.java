package br.com.perfect_day_babies.view.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.perfect_day_babies.R;
import br.com.perfect_day_babies.utils.BitmapHelper;
import br.com.perfect_day_babies.utils.StringUtils;

public class SegmentListAdapter extends BaseAdapter{
	private List<String> segmentItemList;
	private LayoutInflater mInflater;
	@SuppressWarnings("unused")
	private Context context;
	private boolean[] checkboxStatusArray;
	private Set<String> previousCheckedSegments = new HashSet<String>();
	
	public SegmentListAdapter(Context c, Resources resources, List<String> segmentList, String[] previousCheckedSegments)
	{
		segmentItemList = segmentList;
		mInflater = LayoutInflater.from(c); 
		context = c;
		if(previousCheckedSegments != null) {
			this.previousCheckedSegments = new HashSet<String>(Arrays.asList(previousCheckedSegments));
		}
		initCheckboxStatusArray();
		
	}

	 private void initCheckboxStatusArray() {
		checkboxStatusArray = new boolean[segmentItemList.size()];
		for (int checkBoxIndex = 0; checkBoxIndex < checkboxStatusArray.length; checkBoxIndex++) {
			String segment = segmentItemList.get(checkBoxIndex);
			if(this.previousCheckedSegments.contains(segment))
				checkboxStatusArray[checkBoxIndex] = true;
			else {
				checkboxStatusArray[checkBoxIndex] = false;
			}
		}		
	}

	@Override
	 public int getCount() 
	 {
		 return segmentItemList.size();
	 }

	 @Override
	 public Object getItem(int position) 
	 {
		 return segmentItemList.get(position);
	 }

	 @Override
	 public long getItemId(int position) 
	 {
		 return this.segmentItemList.get(position).hashCode();
	 }
	 
	 public void add(String segment)
	 {
		 if (segment != null)
			 this.segmentItemList.add(segment);
	 }
	 
	 public void clear()
	 {
		 segmentItemList.clear();
	 }

	@Override
	public View getView(int position, View itemView, ViewGroup parent)
	{
		final SegmentView segmentView;
		String segment = segmentItemList.get(position);
		 
		if (itemView == null) {
			itemView = mInflater.inflate(R.layout.segment_list_item,null);
				 
			segmentView = new SegmentView();
			segmentView.itemSegmentImage = (ImageView) itemView.findViewById(R.id.photo);
			segmentView.itemName = (TextView) itemView.findViewById(R.id.segment_text_view);	  
			segmentView.viewId = position;
			segmentView.filterCheckbox = (CheckBox) itemView.findViewById(R.id.checkbox_filter);			 
			itemView.setId(position);			 
			itemView.setTag(segmentView);
			setupItemClickListener(itemView);
		}
		else {
			segmentView = (SegmentView) itemView.getTag();
		}
		
		 
		segmentView.itemName.setText(segment);
		
		segmentView.filterCheckbox.setOnCheckedChangeListener(null);
		segmentView.filterCheckbox.setChecked(checkboxStatusArray[position]);
		setupFilterCheckboxListener(segmentView.filterCheckbox);		 
				 
		// Recupera a chave do segmento a ser pesquisada no hasmap
		String hashKey = StringUtils.normalize( segment );

		// Adiciona as fotos dos segmentos
		Bitmap segmentBitmap = BitmapHelper.segmentImages.get(hashKey);
		if (segmentBitmap != null)
			segmentView.itemSegmentImage.setImageBitmap( segmentBitmap );
		
		return itemView;	
	 }
	 
	 private void setupFilterCheckboxListener(CheckBox favoriteCheckbox) {		 
		 favoriteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton view, boolean isChecked) {					
			    	View segmentItem = (View)view.getParent();
			    	int segmentPosition = segmentItemList.indexOf(((SegmentView)segmentItem.getTag()).itemName.getText());
			    	checkboxStatusArray[segmentPosition] = isChecked;
				}
			});		
	}
	
	private void setupItemClickListener(final View itemView) {
		itemView.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				CheckBox segmentCheckBox = (CheckBox)((ViewGroup)v).getChildAt(2);
				if(segmentCheckBox.isChecked()) {
					segmentCheckBox.setChecked(false);
				} else {
					segmentCheckBox.setChecked(true);
				}
			}
		});
	}
	
	public class SegmentView {
		 ImageView itemSegmentImage;
		 TextView itemName;	  
		 int viewId;
		 CheckBox filterCheckbox;
	 }
	
	public boolean isSegmentChecked(int index) {
		return checkboxStatusArray[index];
	}
	
	public List<String> getCheckedSegments() {
		List<String> checkedSegments = new ArrayList<String>();
		for (int i = 0; i < segmentItemList.size(); i++) {
			if(checkboxStatusArray[i]) {
				checkedSegments.add(segmentItemList.get(i));
			}
		}
		return checkedSegments;
	}


}
