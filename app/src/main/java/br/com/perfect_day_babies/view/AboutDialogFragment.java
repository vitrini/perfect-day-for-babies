package br.com.perfect_day_babies.view;

import br.com.perfect_day_babies.R;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class AboutDialogFragment  extends DialogFragment 
{

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		View view = inflater.inflate(R.layout.dialog_about, container);
		
		ImageButton closeBtn = (ImageButton) view.findViewById( R.id.img_button_close );
		
		TextView site = (TextView) view.findViewById( R.id.about_vitrini_site );

		// Permite que a view dispare os links html
		String htmlSite = "<a href=http://" + site.getText() + " style=\"color:#FFFFFF\">" + site.getText() + "</a>";
		site.setMovementMethod(LinkMovementMethod.getInstance());
		
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
		site.setText(s);
		site.setLinkTextColor(getResources().getColor(R.color.white));
		
		closeBtn.setOnClickListener( new View.OnClickListener() 
		{
			
			public void onClick(View v) 
			{
				dismiss();
			}
			
		});
		
		
		return view;
	}
	
	@Override
	public void onStart()
	{
	  super.onStart();

	  // safety check
	  if (getDialog() == null)
	    return;

//	  int dialogWidth = 920; // specify a value here
//	  int dialogHeight = 1210; // specify a value here

//	  getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

	  // ... other stuff you want to do in your onStart() method
	}

}
