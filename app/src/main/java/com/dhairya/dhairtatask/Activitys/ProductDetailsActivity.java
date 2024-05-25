package com.dhairya.dhairtatask.Activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dhairya.dhairtatask.Adapter.ImageSliderAdapter;
import com.dhairya.dhairtatask.Adapter.ReviewAdapter;
import com.dhairya.dhairtatask.Model.Product;
import com.dhairya.dhairtatask.Model.Review;
import com.dhairya.dhairtatask.R;
import com.dhairya.dhairtatask.utiles.ApiService;
import com.dhairya.dhairtatask.utiles.RetrofitClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductDetailsActivity extends AppCompatActivity {
    private TextView title, description, price, category, brand, stock, rating;
    private ViewPager2 viewPager;
    private LinearLayout dotsIndicator;
    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private ImageSliderAdapter imageSliderAdapter;
    private ImageView[] dots;
    Button btn_add, btn_delete;
    private ApiService apiService;
    private Product product;

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
        btn_add = findViewById(R.id.btn_add);
        btn_delete = findViewById(R.id.btn_delete);

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

        // Get the product object from the intent
        product = (Product) getIntent().getSerializableExtra("product");

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

    private void showAddDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        EditText editTextTitle = dialogView.findViewById(R.id.edit_text_title);
        Button btnAdd = dialogView.findViewById(R.id.btn_add);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(dialogView);
        builder.setTitle("Add Product");
        builder.setCancelable(true);

        final AlertDialog dialog = builder.create();
        dialog.show();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                if (!title.isEmpty()) {
                    addItem(title);
                    dialog.dismiss();
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addItem(String title) {
        Call<Void> call = apiService.addProduct(new Product(title));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductDetailsActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct(product.getId());
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteProduct(int productId) {
        Call<Void> call = apiService.deleteProduct(productId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductDetailsActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish the activity after successful deletion
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}