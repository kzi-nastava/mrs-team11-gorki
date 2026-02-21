package ftn.mrs_team11_gorki.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.adapters.UserCardListAdapter;
import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.dto.BlockUserDTO;
import ftn.mrs_team11_gorki.dto.GetUserDTO;
import ftn.mrs_team11_gorki.service.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminBlockUserFragment extends Fragment implements BlockNoteDialogFragment.Listener {

    private ListView lvUsers;
    private ProgressBar progress;
    private TextView txtError;

    private UserCardListAdapter adapter;
    private UserService userService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_block_user, container, false);

        userService = ApiClient.getRetrofit(requireContext()).create(UserService.class);

        lvUsers = v.findViewById(R.id.lvUsers);
        progress = v.findViewById(R.id.progress);
        txtError = v.findViewById(R.id.txtError);

        adapter = new UserCardListAdapter(requireContext(), user -> {
            long id = user.getId() != null ? user.getId() : -1L;
            if (id == -1L) return;

            boolean blocked = getBlockedValue(user);
            BlockNoteDialogFragment dialog = BlockNoteDialogFragment.newInstance(id, blocked);
            dialog.show(getChildFragmentManager(), "block_note");
        });

        lvUsers.setAdapter(adapter);

        loadUsers();

        return v;
    }

    private void loadUsers() {
        setLoading(true);
        setError(null);

        userService.getAllUsers().enqueue(new Callback<Collection<GetUserDTO>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<GetUserDTO>> call,
                                   @NonNull Response<Collection<GetUserDTO>> response) {
                setLoading(false);

                if (!response.isSuccessful() || response.body() == null) {
                    setError("Failed to load users. Code: " + response.code());
                    return;
                }

                List<GetUserDTO> list = new ArrayList<>(response.body());
                adapter.setUsers(list);
            }

            @Override
            public void onFailure(@NonNull Call<Collection<GetUserDTO>> call, @NonNull Throwable t) {
                setLoading(false);
                setError(t.getMessage() != null ? t.getMessage() : "Failed to load users.");
            }
        });
    }

    @Override
    public void onConfirm(long userId, String reason) {
        BlockUserDTO dto = new BlockUserDTO(userId, reason);

        setLoading(true);
        setError(null);

        userService.blockUser(dto).enqueue(new Callback<GetUserDTO>() {
            @Override
            public void onResponse(@NonNull Call<GetUserDTO> call, @NonNull Response<GetUserDTO> response) {
                setLoading(false);

                if (!response.isSuccessful() || response.body() == null) {
                    setError("Failed to update user. Code: " + response.code());
                    return;
                }

                GetUserDTO updated = response.body();
                adapter.updateUser(updated);

                Toast.makeText(requireContext(),
                        getBlockedValue(updated) ? "User blocked." : "User unblocked.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<GetUserDTO> call, @NonNull Throwable t) {
                setLoading(false);
                setError(t.getMessage() != null ? t.getMessage() : "Failed to update user.");
            }
        });
    }

    private boolean getBlockedValue(GetUserDTO u) {
        try {
            return (boolean) GetUserDTO.class.getMethod("isBlocked").invoke(u);
        } catch (Exception ignore) {
            try {
                Object v = GetUserDTO.class.getMethod("getBlocked").invoke(u);
                return Boolean.TRUE.equals(v);
            } catch (Exception ignored2) {
                return false;
            }
        }
    }

    private void setLoading(boolean loading) {
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
        lvUsers.setEnabled(!loading);
    }

    private void setError(@Nullable String message) {
        if (message == null || message.trim().isEmpty()) {
            txtError.setVisibility(View.GONE);
            txtError.setText("");
        } else {
            txtError.setVisibility(View.VISIBLE);
            txtError.setText(message);
        }
    }
}
