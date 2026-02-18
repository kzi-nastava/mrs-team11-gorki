package ftn.mrs_team11_gorki.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ftn.mrs_team11_gorki.dto.DriverRideHistoryDTO;
import ftn.mrs_team11_gorki.service.AdminService;
import ftn.mrs_team11_gorki.service.ClientUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPanicViewModel extends ViewModel {

    private String fromDate = null;
    private String toDate = null;

    public void setFromDate(String from) { fromDate = from; }
    public void setToDate(String to) { toDate = to; }
    public String getFromDate() { return fromDate; }
    public String getToDate() { return toDate; }

    private final MutableLiveData<List<DriverRideHistoryDTO>> visibleRides =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> errorText = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    private List<DriverRideHistoryDTO> allRides = new ArrayList<>();
    private int sortOption = 0;

    private final AdminService adminService = ClientUtils.getAdminService();

    public LiveData<List<DriverRideHistoryDTO>> getVisibleRides() { return visibleRides; }
    public LiveData<String> getErrorText() { return errorText; }
    public LiveData<Boolean> getLoading() { return loading; }

    public void setSortOption(int option) {
        this.sortOption = option;
        applySort(); // BITNO: da spinner odmah radi
    }

    public void loadPanicRides(String jwtToken, String from, String to) {
        if (jwtToken == null || jwtToken.trim().isEmpty()) {
            errorText.setValue("Nema tokena. Uloguj se prvo.");
            return;
        }

        loading.setValue(true);
        errorText.setValue("");

        Call<List<DriverRideHistoryDTO>> call = adminService.getPanicRides(
                "Bearer " + jwtToken,
                from,   // može null ili "YYYY-MM-DD"
                to      // može null ili "YYYY-MM-DD"
        );

        call.enqueue(new Callback<List<DriverRideHistoryDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<DriverRideHistoryDTO>> call,
                                   @NonNull Response<List<DriverRideHistoryDTO>> resp) {

                loading.setValue(false);

                if (!resp.isSuccessful()) {
                    errorText.setValue("Greška: HTTP " + resp.code());
                    visibleRides.setValue(Collections.emptyList());
                    return;
                }

                List<DriverRideHistoryDTO> body = resp.body();
                allRides = (body == null) ? new ArrayList<>() : new ArrayList<>(body);

                applySort();
            }

            @Override
            public void onFailure(@NonNull Call<List<DriverRideHistoryDTO>> call,
                                  @NonNull Throwable t) {
                loading.setValue(false);
                errorText.setValue("Network failure: " + t.getMessage());
                visibleRides.setValue(Collections.emptyList());
            }
        });
    }

    private void applySort() {
        List<DriverRideHistoryDTO> tmp = new ArrayList<>(allRides);

        switch (sortOption) {
            case 1: tmp.sort((a, b) -> safeCompare(b.getStartingTime(), a.getStartingTime())); break;
            case 2: tmp.sort((a, b) -> safeCompare(a.getStartingTime(), b.getStartingTime())); break;
            case 3: tmp.sort((a, b) -> safeCompare(b.getEndingTime(), a.getEndingTime())); break;
            case 4: tmp.sort((a, b) -> safeCompare(a.getEndingTime(), b.getEndingTime())); break;
            case 5: tmp.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice())); break;
            case 6: tmp.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice())); break;
            case 7: tmp.sort((a, b) -> Boolean.compare(!a.isCanceled(), !b.isCanceled())); break;
            case 8: tmp.sort((a, b) -> Boolean.compare(!a.isPanicActivated(), !b.isPanicActivated())); break;
            default: break;
        }

        visibleRides.setValue(tmp);
    }

    private static <T extends Comparable<T>> int safeCompare(T a, T b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
    }
}