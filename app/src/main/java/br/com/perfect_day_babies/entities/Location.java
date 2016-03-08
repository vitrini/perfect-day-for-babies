package br.com.perfect_day_babies.entities;

/**
 * Classe utilizar para representar localizações de
 * vitrinis, banheiros, saidas e restaurantes.
 * Usado apenas para lincar Vitrinis com os pontos da webview
 * @author AlessandroGurgel
 *
 */
public class Location {
	
	
	public Location(String id, String vitriniId ) {
		super();
		this.id = id;
		this.vitriniId = vitriniId;
	}
	private String id;
	private String vitriniId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVitriniId() {
		return vitriniId;
	}
	public void setVitriniId(String vitriniId) {
		this.vitriniId = vitriniId;
	}
	
}
