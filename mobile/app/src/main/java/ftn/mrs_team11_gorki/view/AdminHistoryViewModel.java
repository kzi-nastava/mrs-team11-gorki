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

public class AdminHistoryViewModel extends ViewModel {

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
    private String filterText = "";

    private final AdminService adminService =
            ClientUtils.getRetrofit().create(AdminService.class);

    public LiveData<List<DriverRideHistoryDTO>> getVisibleRides() { return visibleRides; }
    public LiveData<String> getErrorText() { return errorText; }
    public LiveData<Boolean> getLoading() { return loading; }

    public void setSortOption(int option) {
        this.sortOption = option;
        applySortAndFilter();
    }

    public void setFilterText(String text) {
        this.filterText = (text == null) ? "" : text.trim();
        applySortAndFilter();
    }

    public void loadHistory(String jwtToken, String from, String to) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            errorText.setValue("Nema tokena. Uloguj se prvo.");
            return;
        }

        loading.setValue(true);
        errorText.setValue("");

        Call<List<DriverRideHistoryDTO>> call = adminService.getAdminRideHistory(
                "Bearer " + jwtToken,
                from,   // može null ili "YYYY-MM-DD"
                to      // može null ili "YYYY-MM-DD"
        );

        call.enqueue(new Callback<List<DriverRideHistoryDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<DriverRideHistoryDTO>> call,
                                   @NonNull Response<List<DriverRideHistoryDTO>> response) {

                loading.setValue(false);

                if (!response.isSuccessful()) {
                    String msg = "Greška: HTTP " + response.code();

                    try {
                        if (response.errorBody() != null) {
                            msg += "\n" + response.errorBody().string();
                        }
                    } catch (Exception ignored) {}

                    errorText.setValue(msg);
                    visibleRides.setValue(Collections.emptyList());
                    return;
                }

                List<DriverRideHistoryDTO> body = response.body();
                allRides = (body == null) ? new ArrayList<>() : new ArrayList<>(body);

                applySortAndFilter();
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

    private void applySortAndFilter() {
        List<DriverRideHistoryDTO> tmp = new ArrayList<>(allRides);

        if (!filterText.isEmpty()) {
            String q = filterText.toLowerCase();
            List<DriverRideHistoryDTO> filtered = new ArrayList<>();

            for (DriverRideHistoryDTO r : tmp) {
                boolean match = false;

                if (r.getRideId() != null && String.valueOf(r.getRideId()).contains(q)) match = true;

                if (!match && r.getRoute() != null && r.getRoute().getLocations() != null) {
                    if (!r.getRoute().getLocations().isEmpty() && r.getRoute().getLocations().get(0) != null) {
                        String a0 = safeLower(r.getRoute().getLocations().get(0).getAddress());
                        if (a0.contains(q)) match = true;
                    }
                    if (!match && r.getRoute().getLocations().size() > 1 && r.getRoute().getLocations().get(1) != null) {
                        String a1 = safeLower(r.getRoute().getLocations().get(1).getAddress());
                        if (a1.contains(q)) match = true;
                    }
                }

                if (!match && r.getPassengers() != null) {
                    for (int i = 0; i < r.getPassengers().size(); i++) {
                        if (r.getPassengers().get(i) != null) {
                            String em = safeLower(r.getPassengers().get(i).getEmail());
                            if (em.contains(q)) { match = true; break; }
                        }
                    }
                }

                if (match) filtered.add(r);
            }
            tmp = filtered;
        }

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

    private static String safeLower(String s) {
        return s == null ? "" : s.toLowerCase();
    }

    private static <T extends Comparable<T>> int safeCompare(T a, T b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
    }
}