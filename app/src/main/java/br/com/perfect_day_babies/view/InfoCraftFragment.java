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

public class InfoCraftFragment extends Fragment 
{

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle b)
	{

		View view = inflater.inflate(
				R.layout.fragment_craft,
				container,
				false);  //!!! this is important

		// Redirecionando o click dos links
		TextView linkInscricao = (TextView)view.findViewById( R.id.lingueta_instagram);
		TextView linkFacebook = (TextView)view.findViewById( R.id.lingueta_link_facebook);
		linkInscricao.setMovementMethod(LinkMovementMethod.getInstance());
		linkFacebook.setMovementMethod(LinkMovementMethod.getInstance());
		
		// Modificando a cor do início do texto do release
		String releaseContent = StringUtils.htmlFileFromAssetsToString("craft_content/release.html", view.getContext());
		TextView textviewCraftRelease = (TextView)view.findViewById( R.id.lingueta_craft_release);
		textviewCraftRelease.setText(Html.fromHtml(releaseContent));
		
		return view;
	}
}
