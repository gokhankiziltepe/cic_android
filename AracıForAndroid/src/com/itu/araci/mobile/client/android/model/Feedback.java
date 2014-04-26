package com.itu.araci.mobile.client.android.model;

public class Feedback {
	private Integer id;
	private Integer placeId;
	private String placeName;
	private Integer ambiance;
	private Integer flavour;
	private Integer service;
	private Integer price;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPlaceId() {
		return placeId;
	}

	public void setPlaceId(Integer placeId) {
		this.placeId = placeId;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public Integer getAmbiance() {
		return ambiance;
	}

	public void setAmbiance(Integer ambiance) {
		this.ambiance = ambiance;
	}

	public Integer getFlavour() {
		return flavour;
	}

	public void setFlavour(Integer flavour) {
		this.flavour = flavour;
	}

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

}
