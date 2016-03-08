package br.com.perfect_day_babies.view.adapters;

import br.com.perfect_day_babies.utils.BitmapHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ImagePagerAdapter extends PagerAdapter {
	private Context context;
	private String[] imagesPath;
	
	public ImagePagerAdapter( Context context, String[] imagesPath ) {
		this.context = context;
		this.imagesPath = imagesPath;
	}	

    @Override
    public int getCount() {
      return imagesPath.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem( ViewGroup container, int position ) {
    	ImageView imageView = new ImageView( context );
  
	  	String imagePath = imagesPath[position];
	  	Bitmap bitmap = BitmapHelper.readBitmapFromAssets( imagePath, context );
//Blur	  	bitmap = BitmapHelper.fastblur(bitmap, 10);
		Drawable d = new BitmapDrawable( context.getResources(), bitmap );

		imageView.setScaleType( ImageView.ScaleType.FIT_XY );
		imageView.setImageDrawable( d );
		((ViewPager) container).addView( imageView );

		return imageView;
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      ((ViewPager) container).removeView((ImageView) object);
    }
}
