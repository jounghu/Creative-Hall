package cht.com.cht.model;

public class FavoriteInfo {
	private int id;
	private String favorite_item;
	private String desc;
	private String img;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFavorite_item() {
		return favorite_item;
	}
	public void setFavorite_item(String favorite_item) {
		this.favorite_item = favorite_item;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public FavoriteInfo(int id, String favorite_item, String desc, String img) {
		super();
		this.id = id;
		this.favorite_item = favorite_item;
		this.desc = desc;
		this.img = img;
	}
	public FavoriteInfo() {
		super();
	}
	@Override
	public String toString() {
		return "FavoriteInfo [id=" + id + ", favorite_item=" + favorite_item
				+ ", desc=" + desc + ", img=" + img + "]";
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof FavoriteInfo)){return false;}
		FavoriteInfo fa = (FavoriteInfo) o;
		return fa.id == id;
	}

	@Override
	public int hashCode() {
		return this.id;
	}
}
