package com.dhairya.dhairtatask.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhairya.dhairtatask.Adapter.ImageSliderAdapter;
import com.dhairya.dhairtatask.Adapter.ReviewAdapter;
import com.dhairya.dhairtatask.Model.Product;
import com.dhairya.dhairtatask.Model.Review;
import com.dhairya.dhairtatask.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {
    private TextView title, description, price, category, brand, stock, rating;
    private ViewPager2 viewPager;
    private LinearLayout dotsIndicator;
    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private ImageSliderAdapter imageSliderAdapter;
    private ImageView[] dots;

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
        dotsIndicator = findViewById(R.id.dotsIndicator);
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(), MainActivity.class));
               finish();
            }
        });
        // Get the product object from the intent
        Product product = (Product) getIntent().getSerializableExtra("product");

        // Set the product details to the views
        if (product != null) {
            title.setText(product.getTitle());
            description.setText(product.getDescription());
            price.setText("Price: " + String.format("$%.2f", product.getPrice()));
            category.setText("Category: " + product.getCategory());
            brand.setText("Brand: " + product.getBrand());
            stock.setText("Stock: " + String.valueOf(product.getStock()));
            rating.setText(String.valueOf(product.getRating()));

            // Set up the image slider
            List<String> imageUrls = product.getImages();
            imageSliderAdapter = new ImageSliderAdapter(this, imageUrls);
            viewPager.setAdapter(imageSliderAdapter);

            // Set up the dot indicators
            setupDotIndicators(imageUrls.size());
            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    updateDotIndicators(position);
                }
            });

            // Set up the reviews RecyclerView
            List<Review> reviews = product.getReviews();
            reviewAdapter = new ReviewAdapter(reviews);
            reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            reviewsRecyclerView.setAdapter(reviewAdapter);
        }
    }

    private void setupDotIndicators(int count) {
        dots = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 0, 8, 0);

        for (int i = 0; i < count; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getDrawable(R.drawable.non_active_dot));
            dots[i].setLayoutParams(params);
            dotsIndicator.addView(dots[i]);
        }

        // Set the first dot as active
        if (dots.length > 0) {
            dots[0].setImageDrawable(getDrawable(R.drawable.active_dot));
        }
    }

    private void updateDotIndicators(int position) {
        for (int i = 0; i < dots.length; i++) {
            dots[i].setImageDrawable(getDrawable(i == position ? R.drawable.active_dot : R.drawable.non_active_dot));
        }
    }
}