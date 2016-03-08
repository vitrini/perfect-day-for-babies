package br.com.perfect_day_babies.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.perfect_day_babies.R;

public class InfoComfortFragment extends Fragment {

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle b)
	{

		View view = inflater.inflate(
				R.layout.fragment_comfort,
				container,
				false);  //!!! this is important
		
		return view;
	}
}
