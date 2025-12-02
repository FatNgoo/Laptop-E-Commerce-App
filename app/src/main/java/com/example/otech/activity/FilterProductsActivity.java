package com.example.otech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.adapter.UsageCategoryAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class FilterProductsActivity extends AppCompatActivity implements UsageCategoryAdapter.OnCategoryClickListener {

    private MaterialToolbar toolbar;
    private RecyclerView rvUsageCategories;
    private ChipGroup chipGroupBrands, chipGroupPriceRanges, chipGroupProcessors, chipGroupScreenSizes;
    private MaterialButton btnReset, btnApply;

    private UsageCategoryAdapter usageCategoryAdapter;
    private ArrayList<String> usageCategories;

    // Filter state
    private String selectedUsageCategory = "";
    private ArrayList<String> selectedBrands = new ArrayList<>();
    private String selectedPriceRange = "";
    private ArrayList<String> selectedProcessors = new ArrayList<>();
    private String selectedScreenSize = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_products);

        initViews();
        setupUsageCategories();
        applyPreSelectedCategory();
        setupFilterChips();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvUsageCategories = findViewById(R.id.rvUsageCategories);
        chipGroupBrands = findViewById(R.id.chipGroupBrands);
        chipGroupPriceRanges = findViewById(R.id.chipGroupPriceRanges);
        chipGroupProcessors = findViewById(R.id.chipGroupProcessors);
        chipGroupScreenSizes = findViewById(R.id.chipGroupScreenSizes);
        btnReset = findViewById(R.id.btnReset);
        btnApply = findViewById(R.id.btnApply);

        setSupportActionBar(toolbar);
    }

    private void setupUsageCategories() {
        usageCategories = new ArrayList<>(Arrays.asList(
                "Văn phòng",
                "Gaming",
                "Mỏng nhẹ",
                "Sinh viên",
                "Cảm ứng",
                "Laptop AI",
                "Đồ họa - Kỹ thuật",
                "MacBook CTO"
        ));

        usageCategoryAdapter = new UsageCategoryAdapter(this, usageCategories, this);
        rvUsageCategories.setLayoutManager(new LinearLayoutManager(this));
        rvUsageCategories.setAdapter(usageCategoryAdapter);

        // Default select first category
        selectedUsageCategory = usageCategories.get(0);
    }
    
    private void applyPreSelectedCategory() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("usage_category")) {
            String preSelectedCategory = intent.getStringExtra("usage_category");
            int position = usageCategories.indexOf(preSelectedCategory);
            if (position >= 0) {
                selectedUsageCategory = preSelectedCategory;
                usageCategoryAdapter.setSelectedPosition(position);
            }
        }
    }

    private void setupFilterChips() {
        // Multi-select for brands
        setupChipGroupMultiSelect(chipGroupBrands, selectedBrands);

        // Single-select for price ranges
        chipGroupPriceRanges.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                int checkedId = checkedIds.get(0);
                Chip chip = findViewById(checkedId);
                if (chip != null) {
                    selectedPriceRange = chip.getText().toString();
                }
            } else {
                selectedPriceRange = "";
            }
        });

        // Multi-select for processors
        setupChipGroupMultiSelect(chipGroupProcessors, selectedProcessors);

        // Single-select for screen sizes
        chipGroupScreenSizes.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                int checkedId = checkedIds.get(0);
                Chip chip = findViewById(checkedId);
                if (chip != null) {
                    selectedScreenSize = chip.getText().toString();
                }
            } else {
                selectedScreenSize = "";
            }
        });
    }

    private void setupChipGroupMultiSelect(ChipGroup chipGroup, ArrayList<String> selectedList) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                String chipText = buttonView.getText().toString();
                if (isChecked) {
                    if (!selectedList.contains(chipText)) {
                        selectedList.add(chipText);
                    }
                } else {
                    selectedList.remove(chipText);
                }
            });
        }
    }

    private void setupListeners() {
        toolbar.setNavigationOnClickListener(v -> finish());

        btnReset.setOnClickListener(v -> resetFilters());
        
        btnApply.setOnClickListener(v -> applyFilters());
    }

    private void resetFilters() {
        // Clear all selections
        selectedBrands.clear();
        selectedPriceRange = "";
        selectedProcessors.clear();
        selectedScreenSize = "";

        // Uncheck all chips
        chipGroupBrands.clearCheck();
        chipGroupPriceRanges.clearCheck();
        chipGroupProcessors.clearCheck();
        chipGroupScreenSizes.clearCheck();

        // Reset to first usage category
        usageCategoryAdapter.setSelectedPosition(0);
        selectedUsageCategory = usageCategories.get(0);

        Toast.makeText(this, "Đã xóa bộ lọc", Toast.LENGTH_SHORT).show();
    }

    private void applyFilters() {
        // Create filter data object
        FilterData filterData = new FilterData(
                selectedUsageCategory,
                new ArrayList<>(selectedBrands),
                selectedPriceRange,
                new ArrayList<>(selectedProcessors),
                selectedScreenSize
        );

        // Navigate to CategoriesActivity with filter data
        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.putExtra("filter_data", filterData);
        startActivity(intent);
        finish(); // Close filter screen
    }

    @Override
    public void onCategoryClick(String category, int position) {
        selectedUsageCategory = category;
        // You can update filter options based on selected category if needed
    }

    // FilterData class for passing filter information
    public static class FilterData implements Serializable {
        private String usageCategory;
        private ArrayList<String> brands;
        private String priceRange;
        private ArrayList<String> processors;
        private String screenSize;

        public FilterData(String usageCategory, ArrayList<String> brands, String priceRange,
                          ArrayList<String> processors, String screenSize) {
            this.usageCategory = usageCategory;
            this.brands = brands;
            this.priceRange = priceRange;
            this.processors = processors;
            this.screenSize = screenSize;
        }

        public String getUsageCategory() {
            return usageCategory;
        }

        public ArrayList<String> getBrands() {
            return brands;
        }

        public String getPriceRange() {
            return priceRange;
        }

        public ArrayList<String> getProcessors() {
            return processors;
        }

        public String getScreenSize() {
            return screenSize;
        }
    }
}
