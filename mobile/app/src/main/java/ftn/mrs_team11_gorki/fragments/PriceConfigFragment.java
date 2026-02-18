package ftn.mrs_team11_gorki.fragments;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import  ftn.mrs_team11_gorki.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.dto.PriceConfigDTO;
import ftn.mrs_team11_gorki.service.PriceConfService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriceConfigFragment extends Fragment {

    private TextInputLayout tilStandard, tilLuxury, tilVan, tilPerKm;
    private TextInputEditText etStandard, etLuxury, etVan, etPerKm;
    private MaterialButton btnSave;
    private ProgressBar progress;

    private PriceConfService api;

    private Call<PriceConfigDTO> getCall;
    private Call<PriceConfigDTO> putCall;

    public PriceConfigFragment() {
        super(R.layout.fragment_price_config);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tilStandard = view.findViewById(R.id.tilStandard);
        tilLuxury   = view.findViewById(R.id.tilLuxury);
        tilVan      = view.findViewById(R.id.tilVan);
        tilPerKm    = view.findViewById(R.id.tilPerKm);

        etStandard = view.findViewById(R.id.etStandard);
        etLuxury   = view.findViewById(R.id.etLuxury);
        etVan      = view.findViewById(R.id.etVan);
        etPerKm    = view.findViewById(R.id.etPerKm);

        btnSave  = view.findViewById(R.id.btnSave);
        progress = view.findViewById(R.id.progress);

        api = ApiClient
                .getRetrofit(requireContext())
                .create(PriceConfService.class);

        btnSave.setOnClickListener(v -> saveConfig());

        loadConfig();
    }

    private void loadConfig() {
        clearErrors();
        setLoading(true);

        getCall = api.getCurrentPriceConfig();
        getCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PriceConfigDTO> call,
                                   @NonNull Response<PriceConfigDTO> response) {
                if (!isAdded()) return;
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    fillForm(response.body());
                } else {
                    showHttpError("Load failed", response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PriceConfigDTO> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                setLoading(false);
                Snackbar.make(requireView(),
                        t.getMessage() != null ? t.getMessage() : "Network error",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void saveConfig() {
        clearErrors();

        Double standard = readDouble(tilStandard, etStandard);
        Double luxury   = readDouble(tilLuxury, etLuxury);
        Double van      = readDouble(tilVan, etVan);
        Double perKm    = readDouble(tilPerKm, etPerKm);

        if (standard == null || luxury == null || van == null || perKm == null) return;

        if (standard < 0 || luxury < 0 || van < 0 || perKm < 0) {
            Snackbar.make(requireView(), "Values cannot be negative", Snackbar.LENGTH_LONG).show();
            return;
        }

        PriceConfigDTO dto = new PriceConfigDTO();
        dto.setPriceForStandardVehicles(standard);
        dto.setPriceForLuxuryVehicles(luxury);
        dto.setPriceForVans(van);
        dto.setPricePerKm(perKm);

        setLoading(true);

        putCall = api.updatePriceConfig(dto);
        putCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PriceConfigDTO> call,
                                   @NonNull Response<PriceConfigDTO> response) {
                if (!isAdded()) return;
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    fillForm(response.body()); // backend vraća current config
                    Snackbar.make(requireView(), "Saved", Snackbar.LENGTH_SHORT).show();
                } else {
                    showHttpError("Save failed", response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PriceConfigDTO> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                setLoading(false);
                Snackbar.make(requireView(),
                        t.getMessage() != null ? t.getMessage() : "Network error",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void fillForm(PriceConfigDTO data) {
        etStandard.setText(String.valueOf(data.getPriceForStandardVehicles()));
        etLuxury.setText(String.valueOf(data.getPriceForLuxuryVehicles()));
        etVan.setText(String.valueOf(data.getPriceForVans()));
        etPerKm.setText(String.valueOf(data.getPricePerKm()));
    }

    private void setLoading(boolean loading) {
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSave.setEnabled(!loading);

        etStandard.setEnabled(!loading);
        etLuxury.setEnabled(!loading);
        etVan.setEnabled(!loading);
        etPerKm.setEnabled(!loading);
    }

    private void clearErrors() {
        tilStandard.setError(null);
        tilLuxury.setError(null);
        tilVan.setError(null);
        tilPerKm.setError(null);
    }

    private Double readDouble(TextInputLayout til, TextInputEditText et) {
        String s = et.getText() != null ? et.getText().toString().trim() : "";
        if (s.isEmpty()) {
            til.setError("Required");
            return null;
        }
        try {
            return Double.parseDouble(s.replace(',', '.'));
        } catch (Exception e) {
            til.setError("Invalid number");
            return null;
        }
    }

    private void showHttpError(String prefix, int code) {
        String msg = switch (code) {
            case 401 -> prefix + ": unauthorized (401)";
            case 403 -> prefix + ": forbidden (403) - admin only";
            default -> prefix + " (" + code + ")";
        };
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getCall != null) getCall.cancel();
        if (putCall != null) putCall.cancel();
    }
}
