package com.sangh.bioauth;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private List<UserProfile> userProfileList;
    private SharedPreferences sharedPreferences;
    private OnProfileClickListener onProfileClickListener; // Interface for click listener

    // Constructor
    public ProfileAdapter(List<UserProfile> userProfileList, SharedPreferences sharedPreferences, OnProfileClickListener onProfileClickListener) {
        this.userProfileList = userProfileList;
        this.sharedPreferences = sharedPreferences;
        this.onProfileClickListener = onProfileClickListener;
    }

    // ViewHolder class
    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewEmail;
        TextView textViewUserId;
        Button buttonDelete;

        ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewUserId = itemView.findViewById(R.id.textViewUserId);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        UserProfile userProfile = userProfileList.get(position);
        holder.textViewName.setText(userProfile.getName());
        holder.textViewEmail.setText(userProfile.getEmail());
        holder.textViewUserId.setText(userProfile.getUserId());

        // Set click listener for delete button
        holder.buttonDelete.setOnClickListener(v -> {
            // Remove the profile from the list
            userProfileList.remove(position);
            // Notify adapter about item removal
            notifyItemRemoved(position);

            deleteProfileFromSharedPreferences(userProfile.getUserId());
        });

        // Set click listener for the entire item
        holder.itemView.setOnClickListener(v -> {
            // Call interface method to handle item click
            if (onProfileClickListener != null) {
                onProfileClickListener.onProfileClick(userProfile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userProfileList.size();
    }

    private void deleteProfileFromSharedPreferences(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_name_" + userId);
        editor.remove("user_email_" + userId);
        editor.remove("user_id_" + userId);
        editor.apply();
    }

    // Interface for click listener
    interface OnProfileClickListener {
        void onProfileClick(UserProfile userProfile);
    }
}
