package br.com.perfect_day_babies.view;

import br.com.perfect_day_babies.R;
import br.com.perfect_day_babies.utils.StringUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoPlacesFragment extends Fragment 
{
	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle b)
	{

		View view = inflater.inflate(
				R.layout.fragment_places,
				container,
				false);  //!!! this is important

		setContent(view);
		
		return view;
	}
	
	/**
	 * Preenche a view com o conteúdo obtido do arquivo html
	 * @param view View a ser preenchida
	 */
	private void setContent( View view )
	{
		// Realiza  aleitura do arquivo
		String content = StringUtils.htmlFileFromAssetsToString("craft_content/lingueta_aonde_ir.html", view.getContext());

		// Popula a view
		TextView textviewCraftRelease = (TextView)view.findViewById( R.id.lingueta_aonde_ir);
		textviewCraftRelease.setText(Html.fromHtml( content ));
		
		// Permite que a view dispare os links html
		textviewCraftRelease.setMovementMethod(LinkMovementMethod.getInstance());
	}

}
