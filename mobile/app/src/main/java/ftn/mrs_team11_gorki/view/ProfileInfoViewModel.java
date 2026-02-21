package ftn.mrs_team11_gorki.view; // ili viewmodel

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ftn.mrs_team11_gorki.dto.GetUserDTO;
import ftn.mrs_team11_gorki.dto.UpdateUserDTO;
import ftn.mrs_team11_gorki.dto.UpdatedUserDTO;
import ftn.mrs_team11_gorki.service.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileInfoViewModel extends ViewModel {

    private final UserService userService;
    private final Long userId;

    private final MutableLiveData<GetUserDTO> user = new MutableLiveData<>();
    private final MutableLiveData<GetUserDTO> originalUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public ProfileInfoViewModel(UserService userService, Long userId) {
        this.userService = userService;
        this.userId = userId;
    }

    public LiveData<GetUserDTO> getUser() { return user; }
    public LiveData<GetUserDTO> getOriginalUser() { return originalUser; }
    public LiveData<Boolean> isLoading() { return loading; }
    public LiveData<String> getMessage() { return message; }

    public void loadUser() {
        loading.setValue(true);

        userService.getUser(userId).enqueue(new Callback<GetUserDTO>() {
            @Override
            public void onResponse(@NonNull Call<GetUserDTO> call,
                                   @NonNull Response<GetUserDTO> response) {
                loading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    user.setValue(response.body());
                    originalUser.setValue(response.body()); // snapshot za revert
                } else {
                    message.setValue("Ne mogu da učitam profil (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetUserDTO> call, @NonNull Throwable t) {
                loading.setValue(false);
                message.setValue("Greška: " + t.getMessage());
            }
        });
    }

    public void updateUser(UpdateUserDTO dto) {
        loading.setValue(true);

        userService.updateUser(userId, dto).enqueue(new Callback<UpdatedUserDTO>() {
            @Override
            public void onResponse(@NonNull Call<UpdatedUserDTO> call,
                                   @NonNull Response<UpdatedUserDTO> response) {
                loading.setValue(false);

                if (response.isSuccessful()) {
                    message.setValue("Sačuvano");
                    loadUser(); // refresuje user + original snapshot
                } else {
                    message.setValue("Neuspešan update (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdatedUserDTO> call, @NonNull Throwable t) {
                loading.setValue(false);
                message.setValue("Greška: " + t.getMessage());
            }
        });
    }

    public void revert() {
        GetUserDTO orig = originalUser.getValue();
        if (orig != null) {
            user.setValue(orig);
        }
    }
    public void clearMessage() {
        message.setValue(null);
    }
}
