package com.example.zapposproduct;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable{

	private String productName;
	private String brandName;
	private String originalPrice;
	private String percentOff;
	private String price;
	private String productId;
	private String imageUrl;
	
	public Product (String productId, String brandName, String productName, 
			String originalPrice, String percentOff, String price, String imageUrl) {
	
		this.productId = productId;
		this.brandName = brandName;
		this.productName = productName;
		this.originalPrice = originalPrice;
		this.percentOff = percentOff;
		this.price = price;
		this.imageUrl = imageUrl;
	}
	
		public String getProductName() {
			return productName;
		}
		
		public String getBrandName() {
			return brandName;
		}
		
		public String getOriginalPrice() {
			return originalPrice;	
		}
		
		public String getPercentOff() {
			return percentOff;
		}
		
		public String getPrice() {
			return price;
		}
		
		public String getProductId() {
			return productId;
		}

		public String getImageUrl() {
			return imageUrl;
		}
		
	    @Override
	    public int describeContents() {
	        // TODO Auto-generated method stub
	        return 1;
	    }
	 
	    @Override
	    public void writeToParcel(Parcel dest, int flags) {
	        // TODO Auto-generated method stub
	    	dest.writeString(productId);
	        dest.writeString(brandName);
	        dest.writeString(productName);
	        dest.writeString(originalPrice);
	        dest.writeString(percentOff);
	        dest.writeString(price);
	        dest.writeString(imageUrl);
	 
	    }
	 
	    public Product(Parcel source) {
	        // TODO Auto-generated method stub
//	        id = source.readInt();
//	        name = source.readString();
	 
	    }
	 
	    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	 
	        @Override
	        public Product createFromParcel(Parcel source) {
	            // TODO Auto-generated method stub
	            return new Product(source);
	        }
	 
	        @Override
	        public Product[] newArray(int size) {
	            // TODO Auto-generated method stub
	            return new Product[size];
	        }
	    };
	 
}

