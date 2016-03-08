package br.com.perfect_day_babies.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class Product {
	
	private String id;
	private String name;	
	private ImageContainer imageContainer = new ImageContainer();
	private BigDecimal price;	
	private String description;
	private Vitrini vitrini;
	
	public Product(String id, String name, String lightPhotoPath, String heavyPhotoPath,
			BigDecimal price, String description)
	{
		super();
		this.id = id;
		this.name = name;		
		this.imageContainer.small = lightPhotoPath;
		this.imageContainer.large = heavyPhotoPath;
		this.price = price;
		this.description = description;
	}
	
	public Product()
	{
		super();	
	}	

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public String getLightPhotoPath()
	{
		return imageContainer.small;
	}
	public void setLightPhotoPath(String LightPhotoPath)
	{
		this.imageContainer.small = LightPhotoPath;
	}
	
	public String getHeavyPhotoPath()
	{
		return imageContainer.large;
	}
	public void setHeavyPhotoPath(String heavyPhotoPath)
	{
		this.imageContainer.large = heavyPhotoPath;
	}
	
	public String getLightPhotoURL(String serverURL)
	{				
		return getPhotoURLBuilder(serverURL).append("/image-small").toString();
	}
	
	public String getHeavyPhotoURL(String serverURL)
	{		
		return getPhotoURLBuilder(serverURL).append("/image-large").toString();
	}
	
	private StringBuilder getPhotoURLBuilder(String serverURL)
	{
		StringBuilder bitmapUrlBuilder = new StringBuilder();
		bitmapUrlBuilder.append(serverURL)
		.append(getVitrini().getName())
		.append("/category/")
		.append(getVitrini().getId())
		.append("/product/")
		.append(getId());
		return bitmapUrlBuilder;
	}
	public BigDecimal getPrice()
	{
		return price.setScale(2, RoundingMode.HALF_UP);
	}
	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public Vitrini getVitrini() {
		return vitrini;
	}

	public void setVitrini(Vitrini vitrini) {
		this.vitrini = vitrini;
	}


	private static class ImageContainer{
		public String small;
		public String large;
	}


	@Override
	public String toString() {
		String desc = "Vitrini: " + vitrini.getName() + "\n" 
				+ description + "\n"
				+ "Small Image: " + getLightPhotoPath() + "\n"
				+ "Image: " + getHeavyPhotoPath() + "\n"
				+ "Price: " + getPrice() + "\n";				
		return desc;
	}
	
	

}
