package br.com.perfect_day_babies.service;

import java.util.Comparator;

import br.com.perfect_day_babies.entities.Vitrini;

public class VitriniComparator implements Comparator<Vitrini> {

	@Override
	public int compare(Vitrini lhs, Vitrini rhs) {
		
		final String leftVitriniName = lhs.getName();
		final String rightVitriniName = rhs.getName();
		
		return leftVitriniName.compareTo( rightVitriniName);
	}

}
