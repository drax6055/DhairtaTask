package com.dhairya.dhairtatask.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dhairya.dhairtatask.Adapter.ProductAdapter;
import com.dhairya.dhairtatask.Model.Product;
import com.dhairya.dhairtatask.Model.ProductResponse;
import com.dhairya.dhairtatask.R;
import com.dhairya.dhairtatask.databinding.ActivityMainBinding;
import com.dhairya.dhairtatask.utiles.ApiService;
import com.dhairya.dhairtatask.utiles.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private ProductAdapter adapter;
    private List<Product> productList;
    private int currentPage = 1;
    private boolean isLoading = false;
    private ApiService apiService;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setAdapter(adapter);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        loadProducts(currentPage);

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == productList.size() - 1) {
                    currentPage++;
                    loadProducts(currentPage);
                    isLoading = true;
                }
            }
        });
    }

    private void loadProducts(int page) {
        int limit = 10;
        int skip = (page - 1) * limit;
        apiService.getProducts(limit, skip).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.addAll(response.body().getProducts());
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        if (!query.isEmpty()) {
            List<Product> filteredList = new ArrayList<>();
            for (Product product : productList) {
                if (product.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
            adapter.setFilter(filteredList);
        } else {
            // If search query is empty, show the original list
            adapter.setFilter(productList);
        }
    }
}