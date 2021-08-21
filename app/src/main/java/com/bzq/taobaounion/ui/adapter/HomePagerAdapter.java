package com.bzq.taobaounion.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bzq.taobaounion.model.DTO.Categories;
import com.bzq.taobaounion.ui.fragment.HomePagerFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gerkey
 * Created on 2021/7/2
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<Categories.DataDTO> categoryList = new ArrayList<>();

    public HomePagerAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList.get(position).getTitle();
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        Categories.DataDTO dataDTO = categoryList.get(position);
        return HomePagerFragment.newInstance(dataDTO);
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    public void setCategories(Categories categories) {
        categoryList.clear();
        List<Categories.DataDTO> data = categories.getData();
        categoryList.addAll(data);
        notifyDataSetChanged();
    }
}

