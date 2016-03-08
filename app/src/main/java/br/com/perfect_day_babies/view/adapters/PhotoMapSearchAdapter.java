package br.com.perfect_day_babies.view.adapters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.perfect_day_babies.R;
import br.com.perfect_day_babies.controller.ActivityController;
import br.com.perfect_day_babies.entities.Vitrini;
import br.com.perfect_day_babies.utils.BitmapHelper;
import br.com.perfect_day_babies.utils.StringUtils;

public class PhotoMapSearchAdapter extends ArrayAdapter<Vitrini> implements Filterable{
	private final List<Vitrini> vitriniItemListData;
	private List<Vitrini> vitriniItemList;
	private LayoutInflater mInflater;
	private ActivityController controller;

	public PhotoMapSearchAdapter(Context c, List<Vitrini> objects, Resources resources, ActivityController controller)
	{
		super(c, 0);
		vitriniItemList = objects;
		vitriniItemListData = vitriniItemList;
		mInflater = LayoutInflater.from(c); 
		this.controller = controller;
	}

	@Override
	public int getCount() 
	{
		return vitriniItemList.size();
	}

	@Override
	public Vitrini getItem(int position) 
	{
		return vitriniItemList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return this.vitriniItemList.get(position).getId().hashCode();
	}

	public void add(Vitrini item)
	{
		if (item != null)
			this.vitriniItemList.add(item);
	}

	public void clear()
	{
		vitriniItemList.clear();
	}

	public void resetList()
	{
		this.vitriniItemList = vitriniItemListData;
//		VitriniListAdapter.this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View itemView, ViewGroup parent)
	{
		return initView(position, itemView);
	}

	
	
	private View initView( int position, View itemView)
	{
		final ItemView vitriniItemView;
		Vitrini currentVitriniItem = vitriniItemList.get(position);

		if (itemView == null)
		{
			itemView = mInflater.inflate(R.layout.adapter_item_autocomplete,
					null);

			vitriniItemView = new ItemView();
			vitriniItemView.itemSegmentImage = (ImageView) itemView.findViewById(R.id.vitrini_list_segment_image);
			vitriniItemView.itemName = (TextView) itemView.findViewById(R.id.name);	  
			vitriniItemView.itemSegmentName = (TextView) itemView.findViewById(R.id.segment_name);
			vitriniItemView.viewId = position;
//			vitriniItemView.favoriteCheckbox = (CheckBox) itemView.findViewById(R.id.checkbox_favorite);		
			vitriniItemView.locationBtn = (ImageButton) itemView.findViewById( R.id.img_btn_location );

			itemView.setId(position);
			itemView.setTag(vitriniItemView);			 
		}
		else 
		{
			vitriniItemView = (ItemView) itemView.getTag();
		}
		
//		removeFavoriteListener(vitriniItemView.favoriteCheckbox);
//		vitriniItemView.favoriteCheckbox.setChecked(currentVitriniItem.isFavorite());
//		setupFavoriteListener(vitriniItemView.favoriteCheckbox, currentVitriniItem );
		
		vitriniItemView.itemName.setText(currentVitriniItem.getName());
		
		vitriniItemView.itemSegmentName.setText(currentVitriniItem.getSegment());

		// Recupera a chave do segmento a ser pesquisada no hasmap
		String hashKey = StringUtils.normalize( currentVitriniItem.getSegment() );

		// Adiciona as fotos dos segmentos
		Bitmap segmentBitmap = BitmapHelper.segmentImages.get(hashKey);
		if (segmentBitmap != null)
			vitriniItemView.itemSegmentImage.setImageBitmap( segmentBitmap );
						
		//inserindo evento de vitrini

		ImageButton location = vitriniItemView.locationBtn;
		final String vitriniName = currentVitriniItem.getName();
		location.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				controller.goToPhotoMapActivity( vitriniName );					
			}
		} );

			return itemView;

//		} catch (IOException e) {
//			e.printStackTrace();
//			Log.e("ERROR", "it was not possible to open " + logoPath);
//
//			return itemView;

	}
		
	




	@SuppressWarnings("unused")
	private void removeFavoriteListener(CheckBox favoriteCheckbox)	{
		favoriteCheckbox.setOnCheckedChangeListener(null);

	}

	@SuppressWarnings("unused")
	private void setupFavoriteListener(CheckBox favoriteCheckbox, Vitrini currentVitriniItem)
	{
		final Vitrini vitrini = currentVitriniItem;
		favoriteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton view, boolean isChecked)
			{
				/*ListView vitrinisListView = (ListView)view.getParent().getParent().getParent();  
				View vitriniItem = (View)view.getParent().getParent();
				int vitriniPosition = vitrinisListView.getPositionForView(vitriniItem);
				Vitrini checkedVitrini = (Vitrini)getItem(vitriniPosition);
				controller.updateFavoriteStatus(checkedVitrini, isChecked);*/
				controller.hideKeyboard();
				controller.updateFavoriteStatus(vitrini, vitrini.setfavorite(isChecked));
			}
		});		
	}
	
	public void filterVitrinisByName(List<String> filteredVitrinis) {
		Set<String> vitriniNamesHash = new HashSet<String>(filteredVitrinis);
		List<Vitrini> newVitriniList = new ArrayList<Vitrini>(vitriniNamesHash.size());
		for ( Vitrini vitrini : vitriniItemList) {
			if(vitriniNamesHash.contains(vitrini.getName())) {
				newVitriniList.add(vitrini);
			}
		}
		this.vitriniItemList = newVitriniList;		
	}

	public class ItemView 
	{
		ImageView itemSegmentImage;
		TextView itemName;	  
		TextView itemSegmentName;	  
		int viewId;
//		CheckBox favoriteCheckbox;
		ImageButton locationBtn;
	}
	
	 @Override
     public View getDropDownView(int position, View convertView,
                                 ViewGroup parent) {
         return initView(position, convertView);
     }
	 
	   @Override
	    public Filter getFilter() {
	        return new Filter() {
	            @SuppressWarnings("unchecked")
	            @Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	                	vitriniItemList = (List<Vitrini>) results.values;
	                    PhotoMapSearchAdapter.this.notifyDataSetChanged();
	            }

	            @Override
	            protected FilterResults performFiltering(CharSequence constraint) {
	                FilterResults results = new FilterResults();

	                if (constraint == null || constraint.length() == 0) {
	                    // No filter implemented we return all the list
	                    results.values = vitriniItemListData;
	                    results.count = vitriniItemListData.size();

	                } else { // We perform filtering operation
	                    List<Vitrini> filteredRowItems = new ArrayList<Vitrini>();

	                    for (Vitrini p : vitriniItemListData) {
	                        
	                    	final String leftString = StringUtils.normalize(p.getName());
							final String rightString = StringUtils.normalize(constraint.toString());
							
							if (leftString.contains(rightString)) {
	                            filteredRowItems.add(p);
	                        }
	                    }
	                    results.values = filteredRowItems;
	                    results.count = filteredRowItems.size();
	                }
	                	                
	                return results;
	            }
	        };
	    }
}
