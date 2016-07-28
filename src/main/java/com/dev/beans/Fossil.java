
package com.dev.beans;


public class Fossil extends Observation implements Comparable<Fossil>{
	
	private String species;
	private String image;
	
	

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	@Override
	public int compareTo(Fossil o) {
	
		return getDate().compareTo(o.getDate());
	}
	
	

}
