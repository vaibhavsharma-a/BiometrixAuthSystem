package com.sangh.bioauth;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment implements ProfileAdapter.OnProfileClickListener{

    private static final int REQUEST_IMAGE_CAPTURE = 1 ;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 2;
    private LinearLayout inputFieldsLayout;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextUserId;
    private Button buttonAddProfile;
    private Button buttonSave;
    private Button buttonUploadPhoto;
    private RecyclerView recyclerViewProfiles;
    private ProfileAdapter profileAdapter;
    private List<UserProfile> userProfileList;
    private SharedPreferences sharedPreferences;


    private String token;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_first, container, false);

        // Initialize views
        inputFieldsLayout = root.findViewById(R.id.inputFieldsLayout);
        editTextName = root.findViewById(R.id.editTextName);
        editTextEmail = root.findViewById(R.id.editTextEmail);
        editTextUserId = root.findViewById(R.id.editTextUserId);
        buttonAddProfile = root.findViewById(R.id.buttonAddProfile);
        buttonSave = root.findViewById(R.id.buttonSave);
        buttonUploadPhoto = root.findViewById(R.id.buttonUploadPhoto);
        recyclerViewProfiles = root.findViewById(R.id.recyclerViewProfiles);

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("UserData", requireActivity().MODE_PRIVATE);

        // Initialize RecyclerView
        userProfileList = new ArrayList<>();
        profileAdapter = new ProfileAdapter(userProfileList, sharedPreferences, this);
        recyclerViewProfiles.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewProfiles.setAdapter(profileAdapter);

        // Button click listeners
        buttonAddProfile.setOnClickListener(v -> toggleInputFieldsVisibility());
        buttonSave.setOnClickListener(v -> saveProfile());
        buttonUploadPhoto.setOnClickListener(v -> dispatchTakePictureIntent());

        return root;
    }

    private void toggleInputFieldsVisibility() {
        if (inputFieldsLayout.getVisibility() == View.VISIBLE) {
            inputFieldsLayout.setVisibility(View.GONE);
        } else {
            inputFieldsLayout.setVisibility(View.VISIBLE);
        }
    }

    private void saveProfile() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String userId = editTextUserId.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || userId.isEmpty() ) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save profile
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_name", name);
        editor.putString("user_email", email);
        editor.putString("user_id", userId);
        editor.apply();

        // Update RecyclerView
        userProfileList.add(new UserProfile(name, email, userId));
        profileAdapter.notifyDataSetChanged();

        // Clear input fields
        clearInputFields();
        // Hide input fields
        inputFieldsLayout.setVisibility(View.GONE);
    }

    private void clearInputFields() {
        editTextName.setText("");
        editTextEmail.setText("");
        editTextUserId.setText("");
    }

    private void dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permission granted, start camera intent
            startCameraIntent();
        }
    }

    private void startCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, start camera intent
                startCameraIntent();
            } else {
                // Camera permission denied, show a message or handle it accordingly
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//            Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");
//            String imagePath = saveImage(imageBitmap);
//            // Store imagePath along with other profile data...
//        }
//    }

    @Override
    public void onProfileClick(UserProfile userProfile) {
        // Handle the click event here
        // For example, you can send the selected user details to your Flask app
        sendUserDetailsToFlaskApp(userProfile);
    }

    private void setToken(String token){
        this.token = token;
    }
    private void sendUserDetailsToFlaskApp(UserProfile userProfile) {
        // Assuming you have a method to send data to your Flask app
        // You can use Intent to pass data to another activity or service that communicates with your Flask app

//        JSONObject userJson = new JSONObject();
//        try {
//            userJson.put("name", userProfile.getName());
//            userJson.put("email", userProfile.getEmail());
//            userJson.put("user_id", userProfile.getUserId());
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        // Convert JSON object to string
//        String userJsonString = userJson.toString();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get the new registration token
                        String token = task.getResult();
                        Log.d("FCM", "Fetched registration token: " + token);

                        // Send the token to your server for association with the user (explained later)
                        setToken(token);
                    }
                });

//        Toast.makeText(getActivity().getApplicationContext(), "sending request to flask app", Toast.LENGTH_SHORT).show();

        String username = userProfile.getUserId();
        String url = "http://192.168.11.63:8080/send_notification"; // Replace with your actual URL
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JSONObject data = new JSONObject();
        try {
            data.put("username", username);
            data.put("registration_token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, data,
                response -> {
                    // Handle successful notification sending response (e.g., show a toast)
                    Toast.makeText(getContext(), "successful ", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle error sending notification (e.g., show error message)
                    Log.e("Error", error.toString());
                    Toast.makeText(getContext(), "error l266", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
//        FlaskAppComm task = new FlaskAppComm();
//        task.execute(userJsonString);
    }

}
