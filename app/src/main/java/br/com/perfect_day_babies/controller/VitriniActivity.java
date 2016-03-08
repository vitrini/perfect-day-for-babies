 package br.com.perfect_day_babies.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.perfect_day_babies.Constants;
import br.com.perfect_day_babies.R;
import br.com.perfect_day_babies.entities.Vitrini;
import br.com.perfect_day_babies.persistence.VitriniDBAdapter;
import br.com.perfect_day_babies.utils.BitmapHelper;
import br.com.perfect_day_babies.utils.CirclePageIndicator;
import br.com.perfect_day_babies.utils.StringUtils;
import br.com.perfect_day_babies.view.adapters.ImagePagerAdapterVitrinis;

public class VitriniActivity extends AbstractActivity {

	protected Vitrini vitrini;
//	private List<Product> productList;
	private VitriniDBAdapter vitriniDbAdapter;
	
    ViewPager viewPager;
	CirclePageIndicator mIndicator;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatabaseAndServices();
        setContentView(R.layout.activity_vitrini);
        
        final Intent intent = getIntent();
		String vitriniName = intent.getExtras().getString(Constants.VITRINI_FIED_NAME);
		
		vitriniDbAdapter = databaseManager.getVitriniAdapter();
//		ProductDBAdapter productAdapater = dbManager.getProductAdapter();
		
//		initDatabaseAndServices();
		vitrini = vitriniDbAdapter.findVitriniByName(vitriniName);
//		productList = productAdapater.findByVitrini(vitrini);
		initView();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_vitrini, menu);
//        return true;
//    } 
        
    public void onFavoriteCheckboxClicked(View view){
    	CheckBox favoriteCheckbox = (CheckBox)view;
    	vitrini.setfavorite(favoriteCheckbox.isChecked());
    		if(vitrini.isFavorite()) {
    			vitriniDbAdapter.setAsFavorite(vitrini);
    		}else
    			vitriniDbAdapter.removeFavorite(vitrini);
    }

    @Override
	protected void initView() {

		initImageSliderWidget();

		CheckBox favoriteCheck = (CheckBox) findViewById(R.id.checkbox_favorite);
		if(vitrini.isFavorite()){
			favoriteCheck.setChecked(true);
		}
		else{
			favoriteCheck.setChecked(false);
		}
		
		final TextView txtViewVitriniName = (TextView) findViewById( R.id.text_vitrini_name);
		final String vitriniNameTxt = String.format("%s", vitrini.getName() );
		txtViewVitriniName.setText( vitriniNameTxt);
		
		ImageView segmentImage = (ImageView) findViewById(R.id.view_vitrini_segment_icon);
		
		// Recupera a chave do segmento a ser pesquisada no hasmap
		String hashKey = StringUtils.normalize( vitrini.getSegment() );

		// Adiciona as fotos dos segmentos
		Bitmap segmentBitmap = BitmapHelper.segmentImages.get( hashKey );
		if (segmentBitmap != null)
			segmentImage.setImageBitmap( segmentBitmap );
		
		final TextView textCidadeEstado =  (TextView) findViewById( R.id.text_city_estate);
		textCidadeEstado.setText(vitrini.getCityEstateText());
		
		final TextView textSite = (TextView) findViewById(R.id.text_vitrini_site);
		textSite.setText(vitrini.getSite());
		
		final TextView txtDescription =  (TextView) findViewById( R.id.text_vitrini_description);
		txtDescription.setText( vitrini.getDescription() );
		
		
		// Definindo que o textview pode sofre scrool
		txtDescription.setMovementMethod(new ScrollingMovementMethod());
		
		// Recupera o site da vitrini
		String strSite = vitrini.getSite();
		String htmlSite = "<a href=http://" + strSite + ">" + strSite + "</a>";
//		String htmlSite = "<a href=http://" + strSite + " style=\"color:" + BitmapHelper.segmentColors.get(hashKey) + "\">" + strSite + "</a>";

		//Removendo o underline do link
		Spannable s = (Spannable) Html.fromHtml(htmlSite);
		for (URLSpan u: s.getSpans(0, s.length(), URLSpan.class)) {
		    s.setSpan(new UnderlineSpan() {
		        public void updateDrawState(TextPaint tp) {
		            tp.setUnderlineText(false);
		        }
		    }, s.getSpanStart(u), s.getSpanEnd(u), 0);
		}
		
		// Tornando o link clic�vel
//		site.setText( Html.fromHtml(htmlSite) );
		textSite.setText(s);
		textSite.setMovementMethod(LinkMovementMethod.getInstance());
		
		//Define a cor do link
		textSite.setLinkTextColor( Color.parseColor(BitmapHelper.segmentColors.get(hashKey).toString()) );


				
		ImageButton map = (ImageButton) findViewById(R.id.button_map);
		
		map.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
//				controller.goToPhotoMapActivity();
				controller.goToPhotoMapActivity( vitrini.getName() );
			}
		});
	}
	
	protected void initImageSliderWidget() {

		final String vitriniName = StringUtils.normalize(vitrini.getName());
		
	    viewPager = (ViewPager) findViewById(R.id.view_pager_vitrini );
	    ImagePagerAdapterVitrinis adapter = new ImagePagerAdapterVitrinis( this, vitriniName );
	    viewPager.setAdapter(adapter);
	    
	    mIndicator = (CirclePageIndicator) findViewById(R.id.indicator_vitrini);
	    mIndicator.setViewPager( (ViewPager)viewPager);    
	}	

}
