package ftn.mrs_team11_gorki.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.dto.PassengerRegisterRequest;
import ftn.mrs_team11_gorki.service.ClientUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerRegisterFragment extends Fragment {

    private EditText etEmail, etPassword, etConfirm, etFirstName, etLastName, etAddress, etPhone;
    private ImageView ivProfile;

    private Uri photoUri;
    private String photoFileName = null;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> cameraPermissionLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == android.app.Activity.RESULT_OK) {
                        if (photoUri != null && ivProfile != null) {
                            ivProfile.setImageURI(photoUri);
                        }
                    }
                }
        );

        cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (granted) {
                        openCamera();
                    } else {
                        boolean canAskAgain = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                        if (!canAskAgain) {
                            Toast.makeText(getContext(),
                                    "Camera permission is permanently denied. Enable it in Settings.",
                                    Toast.LENGTH_LONG).show();
                            openAppSettings();
                        } else {
                            Toast.makeText(getContext(),
                                    "Camera permission denied",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_passenger_registration, container, false);

        ivProfile = view.findViewById(R.id.userPic);

        etFirstName = view.findViewById(R.id.firstNameInput);
        etLastName  = view.findViewById(R.id.lastNameInput);
        etAddress   = view.findViewById(R.id.homeAddressInput);
        etPhone     = view.findViewById(R.id.phoneInput);

        etEmail     = view.findViewById(R.id.emailInput);
        etPassword  = view.findViewById(R.id.passwordInput);
        etConfirm   = view.findViewById(R.id.confirmPasswordInput);

        Button btnRegister = view.findViewById(R.id.registerButton);

        ivProfile.setOnClickListener(v -> checkCameraPermissionAndOpen());

        btnRegister.setOnClickListener(v -> register());

        return view;
    }

    private void checkCameraPermissionAndOpen() {
        if (!isAdded()) return;

        int granted = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);
        if (granted == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        if (!isAdded()) return;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile;
        try {
            photoFile = File.createTempFile(
                    "profile_", ".jpg",
                    requireContext().getExternalFilesDir(null) // app storage
            );
        } catch (IOException e) {
            Toast.makeText(getContext(), "Cannot create image file", Toast.LENGTH_SHORT).show();
            return;
        }

        photoFileName = photoFile.getName();

        photoUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().getPackageName() + ".provider",
                photoFile
        );

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        cameraLauncher.launch(intent);
    }

    private void register() {
        if (!isAdded()) return;

        String first = etFirstName.getText().toString().trim();
        String last = etLastName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phoneText = etPhone.getText().toString().trim();

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirm = etConfirm.getText().toString();

        if (email.isEmpty() || password.isEmpty() || confirm.isEmpty()
                || first.isEmpty() || last.isEmpty() || address.isEmpty() || phoneText.isEmpty()) {
            Toast.makeText(getContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        int phone;
        try {
            phone = Integer.parseInt(phoneText);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Phone must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        String profileImage = (photoFileName != null) ? photoFileName : null;

        PassengerRegisterRequest req = new PassengerRegisterRequest(
                email, password, confirm, first, last, address, phone, profileImage
        );

        ClientUtils.auth().register(req).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(),
                            "Registration successful. Check email.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(),
                            "Error: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openAppSettings() {
        if (!isAdded()) return;
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}