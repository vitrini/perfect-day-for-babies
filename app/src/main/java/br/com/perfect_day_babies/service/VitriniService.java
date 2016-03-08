package br.com.perfect_day_babies.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.content.res.Resources;
import android.database.Cursor;
import br.com.perfect_day_babies.Constants;
import br.com.perfect_day_babies.entities.Vitrini;
import br.com.perfect_day_babies.persistence.DatabaseManager;
import br.com.perfect_day_babies.persistence.VitriniDBAdapter;

public class VitriniService implements IVitriniService 
{

	final protected DatabaseManager manager;
	Resources res;
	final VitriniDBAdapter vitriniAdapter;

	public VitriniService ( final DatabaseManager manager, Resources res )
	{
		this.manager = manager;
		this.res = res;
		this.vitriniAdapter = manager.getVitriniAdapter();
	}

	//TODO mover para Adapter
	public List<String> listVitriniNames() 
	{
		Cursor vitrinisCursor = vitriniAdapter.getAllVitrinisCursor();


		List<String> vitriniNames = new ArrayList<String>();

		if(vitrinisCursor.moveToFirst())
		{
			do
			{
				String name = vitrinisCursor.getString(VitriniDBAdapter.NAME_COLUMN);
				vitriniNames.add( name );

			} while(vitrinisCursor.moveToNext());
		}
		
		Arrays.sort( vitriniNames.toArray() );
		
		return vitriniNames;

	}

	//TODO mover para Adapter
	public List<String> listVitriniSegments() {

		Cursor vitrinisCursor = vitriniAdapter.getAllVitrinisCursor();

		List<String> segments = new ArrayList<String>();

		Set<String> segmentsSet = new TreeSet<String>();

		if(vitrinisCursor.moveToFirst())
		{
			do
			{
				String segment = vitrinisCursor.getString(VitriniDBAdapter.SEGMENT_COLUMN);
				segmentsSet.add( segment );

			} while(vitrinisCursor.moveToNext());
		}

		segments.addAll(segmentsSet);
		Arrays.sort( segments.toArray());


		return segments;
	}

	public List<String> filterVitrinisBySegment(String segment, boolean considerJustFavorites) {
		
		if ( Constants.ALL_CATEGORIES.equals( segment))
		{
			return listVitriniNames();
		}
		
		Cursor vitrinisCursor = vitriniAdapter.filterVitrinisBySegment(segment, considerJustFavorites);

		List<String> vitrinis = new ArrayList<String>();

		if(vitrinisCursor.moveToFirst())
		{
			do
			{
				String vitriniName = vitrinisCursor.getString(VitriniDBAdapter.NAME_COLUMN);
				vitrinis.add(vitriniName);

			} while(vitrinisCursor.moveToNext());
		}
		
		Arrays.sort( vitrinis.toArray() );

		return vitrinis;
	}

	@Override
	public List<String> getFilteredVitrinis(String input, boolean considerJustFavorites) {
		
		Cursor vitrinisCursor = vitriniAdapter.filterVitrinisByInput(input, considerJustFavorites);

		List<String> vitrinis = new ArrayList<String>();
		
		if ( vitrinisCursor == null ) {
			return vitrinis;
		}

		if(vitrinisCursor.moveToFirst()) {
			do {
				String vitriniName = vitrinisCursor.getString(VitriniDBAdapter.NAME_COLUMN);
				vitrinis.add(vitriniName);

			} while(vitrinisCursor.moveToNext());
		}
		
		Arrays.sort( vitrinis.toArray() );

		return vitrinis;
	}

	@Override
	public Vitrini getVitriniByName(String vitriniName) {
		return vitriniAdapter.findVitriniByName(vitriniName);
	}
	
	public List<Vitrini> listFavorites(String[] segmentsToFilter) {
		return vitriniAdapter.findFavorites(segmentsToFilter);
	}

	@Override
	public List<Vitrini> listVitrinis(String[] segmentsToFilter) {
	
		List<Vitrini> vitrinis = vitriniAdapter.listVitrinis(segmentsToFilter);
		
		Collections.sort(vitrinis, new VitriniComparator());
		
		return vitrinis;
	}

//	@Override
//	public List<Vitrini> filterVitrinis(String segment, boolean considerJustFavorites) {
//		List<Vitrini> vitrinis = new ArrayList<Vitrini>();
//		if ( Constants.ALL_CATEGORIES.equals( segment))
//		{
//			if(considerJustFavorites){
//				return listFavorites();
//			} else {
//				return listVitrinis();
//			}			
//		}		
//		
//		vitrinis = vitriniAdapter.filterVitrinis( segment, considerJustFavorites );
//		
//		Collections.sort( vitrinis, new VitriniComparator());
//		
//		return vitrinis;
//	}

	@Override
	public void checkAsFavorite(Vitrini vitrini) {
		vitriniAdapter.setAsFavorite(vitrini);		
	}

	@Override
	public void uncheckAsFavorite(Vitrini vitrini) {
		vitriniAdapter.removeFavorite(vitrini);		
	}

	@Override
	public List<String> listVitriniSegments(boolean considerJustFavorites) {
		Cursor vitrinisCursor = null;
		if(considerJustFavorites) {
			vitrinisCursor = vitriniAdapter.listFavoriteSegments();
		} else {
			vitrinisCursor = vitriniAdapter.listAllSegments();
		}

		List<String> segments = new LinkedList<String>();	

		if(vitrinisCursor != null && vitrinisCursor.moveToFirst()) {
			do {
				String segment = vitrinisCursor.getString(0);
				segments.add(segment);

			} while(vitrinisCursor.moveToNext());
		}

		Collections.sort(segments);
		return segments;
	}

	@Override
	public List<Vitrini> filterVitrinis(String segment,
			boolean considerJustFavorites) {
		// TODO Auto-generated method stub
		return null;
	}	
}
