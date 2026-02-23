package ftn.mrs_team11_gorki.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ftn.mrs_team11_gorki.service.UserService;

public class ChangePasswordViewModelFactory implements ViewModelProvider.Factory {

    private final UserService userService;
    private final Long userId;

    public ChangePasswordViewModelFactory(UserService userService, Long userId) {
        this.userService = userService;
        this.userId = userId;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChangePasswordViewModel.class)) {
            return (T) new ChangePasswordViewModel(userService, userId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
