package ftn.mrs_team11_gorki.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.adapter.FavouriteRoutesAdapter;
import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.dto.GetRouteDTO;
import ftn.mrs_team11_gorki.dto.LocationDTO;
import ftn.mrs_team11_gorki.service.PassengerService;
import ftn.mrs_team11_gorki.view.FavouriteRouteViewModel;
import ftn.mrs_team11_gorki.view.FavouriteRouteViewModelFactory;

public class FavouriteRouteFragment extends Fragment {

    private FavouriteRouteViewModel viewModel;
    private FavouriteRoutesAdapter adapter;

    private ListView favouriteRoutesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favouriteRoutesList = view.findViewById(R.id.favouriteRoutesList);

        // Service + ViewModel
        PassengerService passengerService = ApiClient.getRetrofit(requireContext()).create(PassengerService.class);
        FavouriteRouteViewModelFactory factory = new FavouriteRouteViewModelFactory(passengerService);
        viewModel = new ViewModelProvider(this, factory).get(FavouriteRouteViewModel.class);

        TokenStorage ts = new TokenStorage(requireContext());
        long passengerId = ts.getUserId(); // <-- prilagodi ako se kod tebe drugačije zove

        adapter = new FavouriteRoutesAdapter(requireContext(), new FavouriteRoutesAdapter.Listener() {
            @Override
            public void onRemoveClicked(@NonNull GetRouteDTO route) {
                if (route.getId() == null) return;
                viewModel.deleteFavouriteRoute(passengerId, route.getId());
            }

            @Override
            public void onChooseClicked(@NonNull GetRouteDTO route) {
                navigateToRideOrdering(route);
            }
        });

        favouriteRoutesList.setAdapter(adapter);

        // Observers
        viewModel.getRoutes().observe(getViewLifecycleOwner(), routes -> {
            if (routes != null) adapter.submitList(routes);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) Toast.makeText(requireContext(), err, Toast.LENGTH_SHORT).show();
        });

        // Load data
        viewModel.loadFavouriteRoutes(passengerId);
    }

    private void navigateToRideOrdering(@NonNull GetRouteDTO route) {
        // Extract addresses
        String start = "";
        String end = "";
        ArrayList<String> stops = new ArrayList<>();

        if (route.getLocations() != null && !route.getLocations().isEmpty()) {
            start = safeAddress(route.getLocations().get(0));
            end = safeAddress(route.getLocations().get(route.getLocations().size() - 1));

            for (int i = 1; i < route.getLocations().size() - 1; i++) {
                stops.add(safeAddress(route.getLocations().get(i)));
            }
        }

        Bundle args = new Bundle();
        args.putString("startAddress", start);
        args.putString("endAddress", end);
        args.putStringArrayList("stoppingPoints", stops);

        NavController nav = NavHostFragment.findNavController(this);

        nav.navigate(R.id.rideOrderingFragment, args);
    }

    private String safeAddress(LocationDTO loc) {
        if (loc == null) return "";
        String a = loc.getAddress();
        return a != null ? a : "";
    }
}
