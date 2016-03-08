package br.com.perfect_day_babies.entities;

import java.util.List;



public class Vitrini {
	
	private String id;
	private String name;
	private String logoPath;
	private String imagePath;
	private String description;
	private String segment;
	private int favorite;
	private List<Location> locations;
	
	private String site;
	private String city;
	private String estate;
	
	public Vitrini(String id, String name, String logoPath, String imagePath,
			String description, String segment, String site, int favorite, String city, String estate) {
		super();
		this.id = id;
		this.name = name;
		this.logoPath = logoPath;
		this.imagePath = imagePath;
		this.description = description;
		this.segment = segment;
		this.site = site;
		this.favorite = favorite;
		this.city = city;
		this.estate = estate;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getLogoPath() {
		return logoPath;
	}


	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}


	public String getImagePath() {
		return imagePath;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getSegment() {
		return segment;
	}


	public void setSegment(String segment) {
		this.segment = segment;
	}


	@Override
	public String toString() {
		String desc = "Vitrini " + name + ":\n" 
				+ description + "\n"
				+ "Logo: " + logoPath + "\n"
				+ "Image: " + imagePath + "\n"
				+ "Segment: " + segment + "\n"
				+ "FAVORITE: " + isFavorite() + "\n";		
		return desc;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public boolean isFavorite() {
		return favorite == 1;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEstate() {
		return estate;
	}

	public void setEstate(String estate) {
		this.estate = estate;
	}
	
	public String getCityEstateText(){
		return city + " - " + estate;
	}	
	
	public boolean setfavorite( boolean isFavorite ){
		if(isFavorite == true)
			this.favorite = 1;
		else
			this.favorite = 0;
		
		return isFavorite();
	}
	
	

}
