package ftn.mrs_team11_gorki.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ftn.mrs_team11_gorki.dto.UpdatePasswordDTO;
import ftn.mrs_team11_gorki.dto.UpdatedUserDTO;
import ftn.mrs_team11_gorki.service.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordViewModel extends ViewModel {

    private final UserService userService;
    private final Long userId;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<Boolean> success = new MutableLiveData<>(false);

    public ChangePasswordViewModel(UserService userService, Long userId) {
        this.userService = userService;
        this.userId = userId;
    }

    public LiveData<Boolean> isLoading() { return loading; }
    public LiveData<String> getMessage() { return message; }
    public LiveData<Boolean> getSuccess() { return success; }

    public void changePassword(UpdatePasswordDTO dto) {
        loading.setValue(true);
        success.setValue(false);

        userService.changePassword(userId, dto).enqueue(new Callback<UpdatedUserDTO>() {
            @Override
            public void onResponse(@NonNull Call<UpdatedUserDTO> call,
                                   @NonNull Response<UpdatedUserDTO> response) {
                loading.setValue(false);

                if (response.isSuccessful()) {
                    message.setValue("Lozinka promenjena");
                    success.setValue(true);
                } else {
                    message.setValue("Neuspešno (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdatedUserDTO> call, @NonNull Throwable t) {
                loading.setValue(false);
                message.setValue("Greška: " + t.getMessage());
            }
        });
    }

    public void clearMessage() {
        message.setValue(null);
    }

    public void clearSuccess() {
        success.setValue(false);
    }
}
