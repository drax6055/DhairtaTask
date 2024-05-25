package com.dhairya.dhairtatask.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhairya.dhairtatask.Adapter.ImageSliderAdapter;
import com.dhairya.dhairtatask.Adapter.ReviewAdapter;
import com.dhairya.dhairtatask.Model.Product;
import com.dhairya.dhairtatask.Model.Review;
import com.dhairya.dhairtatask.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {
    private TextView title, description, price, category, brand, stock, rating;
    private ViewPager2 viewPager;
    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private ImageSliderAdapter imageSliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        title = findViewById(R.id.productTitle);
        description = findViewById(R.id.productDescription);
        price = findViewById(R.id.productPrice);
        category = findViewById(R.id.productCategory);
        brand = findViewById(R.id.productBrand);
        stock = findViewById(R.id.productStock);
        rating = findViewById(R.id.productRating);
        viewPager = findViewById(R.id.viewPager);
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);

        // Get the product object from the intent
        Product product = (Product) getIntent().getSerializableExtra("product");

        // Set the product details to the views
        if (product != null) {
            title.setText(product.getTitle());
            description.setText(product.getDescription());
            price.setText(String.format("$%.2f", product.getPrice()));
            category.setText(product.getCategory());
            brand.setText(product.getBrand());
            stock.setText(String.valueOf(product.getStock()));
            rating.setText(String.valueOf(product.getRating()));

            // Set up the image slider
            List<String> imageUrls = product.getImages();
            imageSliderAdapter = new ImageSliderAdapter(this, imageUrls);
            viewPager.setAdapter(imageSliderAdapter);

            // Set up the reviews RecyclerView
            List<Review> reviews = product.getReviews();
            reviewAdapter = new ReviewAdapter(reviews);
            reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            reviewsRecyclerView.setAdapter(reviewAdapter);
        }
    }
}