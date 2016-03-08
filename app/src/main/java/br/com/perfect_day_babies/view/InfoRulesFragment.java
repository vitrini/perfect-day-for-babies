package br.com.perfect_day_babies.view;

import br.com.perfect_day_babies.R;
import br.com.perfect_day_babies.utils.StringUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoRulesFragment extends Fragment 
{
	
	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle b)
	{

		View view = inflater.inflate(
				R.layout.fragment_rules,
				container,
				false);  //!!! this is important

		String releaseContent = StringUtils.htmlFileFromAssetsToString("craft_content/rules.html", view.getContext());
		TextView textviewrules = (TextView)view.findViewById( R.id.text_view_content);
		textviewrules.setText(Html.fromHtml(releaseContent));
		return view;
	}

}
