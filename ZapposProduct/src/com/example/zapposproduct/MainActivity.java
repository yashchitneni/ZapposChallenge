package com.example.zapposproduct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// Separate to have list of total products and then store certain products in wishlist
	List<Product> productList;
	ArrayList<Product> wishListArray = new ArrayList<Product>();
	
	ListAdapter adapter = null;
	
	public MainActivity()
	{
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		checkPercentOff();
		ListView myListView = (ListView)findViewById(R.id.list_view);
		
		if(productList == null)
		{
			productList = new ArrayList<Product>();
		}
		AsyncTask<String, Void, ArrayList<Product>> getZapposProducts = new GetZapposProducts().execute("");
		try {
			productList = getZapposProducts.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		adapter = new ListAdapter();
		myListView.setAdapter(adapter);
		
		final Context context = this;
		myListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.dialog_box);
				dialog.setTitle("Wish List");
				dialog.show();
				
				TextView dialogText = (TextView) dialog.findViewById(R.id.dialog_text);
				Button dialogButtonYes = (Button) dialog.findViewById(R.id.button_yes);
				Button dialogButtonNo = (Button) dialog.findViewById(R.id.button_no);
				
				dialogButtonYes.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						MainActivity.this.wishListArray.add(productList.get(arg2));
						dialog.dismiss();
					}
				});
				
				dialogButtonNo.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
			}
		});
		
		Button checkWishList = (Button) findViewById(R.id.check_wishlist);
		checkWishList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkPercentOff();
			}
		});
	
	}
	
	void checkPercentOff()
	{
		Product thisProduct;
		int percent;
		String percentString;
		for (int i = 0; i < wishListArray.size(); i++) {
			thisProduct = wishListArray.get(i);
			percentString = thisProduct.getPercentOff();
			percentString = percentString.substring(0, percentString.length() - 1);
			percent = Integer.parseInt(percentString);
			if (percent >= 20)
				Toast.makeText(getApplicationContext(), thisProduct.getProductName() + " is " + percent + "% off", Toast.LENGTH_LONG).show();
		}
	}

	// Custom ListAdapter
	class ListAdapter extends ArrayAdapter<Product>{

		ListAdapter() {
			super(MainActivity.this, android.R.layout.simple_list_item_1, productList);
		}
		
		//The view that is shown
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.list_values, null);
				
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.populateFrom(productList.get(position));
			return (convertView);
		}

	}
	
	class ViewHolder {
		public TextView textBrandName = null;
		public TextView textProductName = null;
		public TextView textOriginalPrice = null;
		
		ViewHolder(View row) {
			textBrandName = (TextView)row.findViewById(R.id.brand_name);
			textProductName = (TextView)row.findViewById(R.id.product_name);
			textOriginalPrice = (TextView)row.findViewById(R.id.original_price);
		}
		
		void populateFrom(Product r) {
			textBrandName.setText("Brand Name: " + r.getBrandName());
			textProductName.setText("Product Name: " + r.getProductName());
			textOriginalPrice.setText("Original Price: " + r.getOriginalPrice());
		}
	}
	
	private class GetZapposProducts extends AsyncTask<String, Void, ArrayList<Product>> {

		@Override
		protected ArrayList<Product> doInBackground(String... arg0) {
			//API KEYS ARE LIMITED DUE TO BEING THROTTLED, SO USING DATA GIVEN MANUALLY
			//String readApi = readAPI();
			String readApi = "{\"statusCode\":\"200\",\"results\":[{\"styleId\":\"2276730\",\"price\":\"$159.96\",\"originalPrice\":\"$199.95\",\"productUrl\":\"http:\\/\\/www.zappos.com\\/product\\/8149427\\/color\\/401\",\"colorId\":\"401\",\"productName\":\"Mutiny\",\"brandName\":\"DC\",\"thumbnailImageUrl\":\"http:\\/\\/www.zappos.com\\/images\\/z\\/2\\/2\\/7\\/6\\/7\\/3\\/2276730-t-THUMBNAIL.jpg\",\"percentOff\":\"19%\",\"productId\":\"8149427\"},{\"styleId\":\"2276728\",\"price\":\"$159.96\",\"originalPrice\":\"$199.95\",\"productUrl\":\"http:\\/\\/www.zappos.com\\/product\\/8149427\\/color\\/20\",\"colorId\":\"20\",\"productName\":\"Mutiny\",\"brandName\":\"DC\",\"thumbnailImageUrl\":\"http:\\/\\/www.zappos.com\\/images\\/z\\/2\\/2\\/7\\/6\\/7\\/2\\/2276728-t-THUMBNAIL.jpg\",\"percentOff\":\"19%\",\"productId\":\"8149427\"},{\"styleId\":\"2276732\",\"price\":\"$159.96\",\"originalPrice\":\"$199.95\",\"productUrl\":\"http:\\/\\/www.zappos.com\\/product\\/8149427\\/color\\/3\",\"colorId\":\"3\",\"productName\":\"Mutiny\",\"brandName\":\"DC\",\"thumbnailImageUrl\":\"http:\\/\\/www.zappos.com\\/images\\/z\\/2\\/2\\/7\\/6\\/7\\/3\\/2276732-t-THUMBNAIL.jpg\",\"percentOff\":\"19%\",\"productId\":\"8149427\"},{\"styleId\":\"2317958\",\"price\":\"$127.95\",\"originalPrice\":\"$159.95\",\"productUrl\":\"http:\\/\\/www.zappos.com\\/product\\/8166134\\/color\\/1489\",\"colorId\":\"1489\",\"productName\":\"Moto\",\"brandName\":\"Burton\",\"thumbnailImageUrl\":\"http:\\/\\/www.zappos.com\\/images\\/z\\/2\\/3\\/1\\/7\\/9\\/5\\/2317958-t-THUMBNAIL.jpg\",\"percentOff\":\"20%\",\"productId\":\"8166134\"},{\"styleId\":\"2317953\",\"price\":\"$127.95\",\"originalPrice\":\"$159.95\",\"productUrl\":\"http:\\/\\/www.zappos.com\\/product\\/8166134\\/color\\/3\",\"colorId\":\"3\",\"productName\":\"Moto\",\"brandName\":\"Burton\",\"thumbnailImageUrl\":\"http:\\/\\/www.zappos.com\\/images\\/z\\/2\\/3\\/1\\/7\\/9\\/5\\/2317953-t-THUMBNAIL.jpg\",\"percentOff\":\"20%\",\"productId\":\"8166134\"},{\"styleId\":\"2317961\",\"price\":\"$127.95\",\"originalPrice\":\"$159.95\",\"productUrl\":\"http:\\/\\/www.zappos.com\\/product\\/8166134\\/color\\/742\",\"colorId\":\"742\",\"productName\":\"Moto\",\"brandName\":\"Burton\",\"thumbnailImageUrl\":\"http:\\/\\/www.zappos.com\\/images\\/z\\/2\\/3\\/1\\/7\\/9\\/6\\/2317961-t-THUMBNAIL.jpg\",\"percentOff\":\"20%\",\"productId\":\"8166134\"},{\"styleId\":\"2310644\",\"price\":\"$159.00\",\"originalPrice\":\"$199.00\",\"productUrl\":\"http:\\/\\/www.zappos.com\\/product\\/8163043\\/color\\/151\",\"colorId\":\"151\",\"productName\":\"Encore\",\"brandName\":\"Vans\",\"thumbnailImageUrl\":\"http:\\/\\/www.zappos.com\\/images\\/z\\/2\\/3\\/1\\/0\\/6\\/4\\/2310644-t-THUMBNAIL.jpg\",\"percentOff\":\"20%\",\"productId\":\"8163043\"},{\"styleId\":\"2310645\",\"price\":\"$159.00\",\"originalPrice\":\"$199.00\",\"productUrl\":\"http:\\/\\/www.zappos.com\\/product\\/8163043\\/color\\/4154\",\"colorId\":\"4154\",\"productName\":\"Encore\",\"brandName\":\"Vans\",\"thumbnailImageUrl\":\"http:\\/\\/www.zappos.com\\/images\\/z\\/2\\/3\\/1\\/0\\/6\\/4\\/2310645-t-THUMBNAIL.jpg\",\"percentOff\":\"20%\",\"productId\":\"8163043\"},{\"styleId\":\"2310759\",\"price\":\"$175.00\",\"originalPrice\":\"$219.00\",\"productUrl\":\"http:\\/\\/www.zappos.com\\/product\\/8163094\\/color\\/424213\",\"colorId\":\"424213\",\"productName\":\"Encore\",\"brandName\":\"Vans\",\"thumbnailImageUrl\":\"http:\\/\\/www.zappos.com\\/images\\/z\\/2\\/3\\/1\\/0\\/7\\/5\\/2310759-t-THUMBNAIL.jpg\",\"percentOff\":\"20%\",\"productId\":\"8163094\"},{\"styleId\":\"2310761\",\"price\":\"$160.00\",\"originalPrice\":\"$199.95\",\"productUrl\":\"http:\\/\\/www.zappos.com\\/product\\/8163094\\/color\\/33041\",\"colorId\":\"33041\",\"productName\":\"Encore\",\"brandName\":\"Vans\",\"thumbnailImageUrl\":\"http:\\/\\/www.zappos.com\\/images\\/z\\/2\\/3\\/1\\/0\\/7\\/6\\/2310761-t-THUMBNAIL.jpg\",\"percentOff\":\"19%\",\"productId\":\"8163094\"}],\"term\":\"boots\",\"originalTerm\":\"boots\",\"currentResultCount\":\"10\",\"totalResultCount\":\"11033\"}";
			
			ArrayList<Product> productList = new ArrayList<Product>();
			try {
				Log.i("log_tag","pre json");
				JSONArray json = new JSONObject(readApi).getJSONArray("results");
				Log.i("log_tag","Json length: " + json.length());
				for (int i = 0; i < json.length(); i++) {
					JSONObject productsMade = json.getJSONObject(i);
					
					String PRODUCT_ID = productsMade.getString("productId");
					String BRAND_NAME = productsMade.getString("brandName");
					String PRODUCT_NAME = productsMade.getString("productName");
					String ORIGINAL_PRICE = productsMade.getString("originalPrice");
					String PERCENT_OFF = productsMade.getString("percentOff");
					String PRICE = productsMade.getString("price");
					String IMAGE_URL = productsMade.getString("thumbnailImageUrl");
					
					productList.add(new Product(PRODUCT_ID, BRAND_NAME, PRODUCT_NAME, ORIGINAL_PRICE, PERCENT_OFF, PRICE, IMAGE_URL));
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return productList;
		}
		
		//UNUSED DUE TO TECHNICAL DIFFICULTIES
		private String readAPI() {
			String result = "";
			
			try {
	            // defaultHttpClient
	        	DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpPost httpPost = new HttpPost("http://api.zappos.com/Search?term=boots&key=b05dcd698e5ca2eab4a0cd1eee4117e7db2a10c4");
	            
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            InputStream is = httpEntity.getContent();
	            
	            try {
	            	BufferedReader reader = new BufferedReader(new InputStreamReader(
	                        is, "iso-8859-1"), 8);
	                StringBuilder sb = new StringBuilder();
	                String line = null;
	                while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	                }
	                is.close();
	                result = sb.toString();
	            } catch (Exception e) {
	            	Log.e("log_tag", "Error converting result" + e.toString());
	            }
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection" + e.toString());
			}
			Log.i("log_tag", result);
			
			return result;
		}
	}

}