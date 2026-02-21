package ftn.mrs_team11_gorki.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.List;

import ftn.mrs_team11_gorki.R;
import ftn.mrs_team11_gorki.auth.ApiClient;
import ftn.mrs_team11_gorki.auth.TokenStorage;
import ftn.mrs_team11_gorki.databinding.FragmentRideOrderingBinding;
import ftn.mrs_team11_gorki.service.NominatimService;
import ftn.mrs_team11_gorki.service.RideService;
import ftn.mrs_team11_gorki.view.RideOrderingViewModel;
import ftn.mrs_team11_gorki.view.RideOrderingViewModelFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RideOrderingFragment extends Fragment {

    private FragmentRideOrderingBinding binding;
    private RideOrderingViewModel vm;

    // ride_ordering_form views
    private EditText startAddressInput;
    private EditText endAddressInput;
    private EditText scheduledTimeInput;
    private AutoCompleteTextView vehicleTypeDropdown;
    private RadioButton yesBabyTransport, yesPetFriendly;
    private Button chooseFromFavButton, addStoppingPointsButton, linkOtherPassengersButton, orderButton;

    // stopping_points_form views
    private EditText firstStop, secondStop, thirdStop, fourthStop, fifthStop, sixthStop;
    private Button doneStopButton;

    // linked_passengers_form views
    private EditText firstLinked, secondLinked, thirdLinked;
    private Button doneLinkButton;

    public RideOrderingFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRideOrderingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm = new ViewModelProvider(
                this,
                new RideOrderingViewModelFactory(
                        ApiClient.getRetrofit(requireContext()).create(RideService.class),
                        provideNominatimService()
                )
        ).get(RideOrderingViewModel.class);

        // Bind views from included forms
        bindRideOrderingForm(binding.rideOrderingForm.getRoot());
        bindStoppingPointsForm(binding.stoppingPointsForm.getRoot());
        bindLinkedPassengersForm(binding.linkedPassengersForm.getRoot());

        applyRouteArgsIfPresent();

        // Vehicle type dropdown (prilagodi vrednosti kako backend očekuje)
        String[] vehicleTypes = new String[]{"STANDARD", "LUXURY", "VAN"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, vehicleTypes);
        vehicleTypeDropdown.setAdapter(adapter);

        chooseFromFavButton.setOnClickListener(v -> {
            // (Opcionalno) prosledi trenutno popunjena polja, da se ne izgube kad se vratiš
            Bundle args = new Bundle();
            args.putString("startAddress", text(startAddressInput));
            args.putString("endAddress", text(endAddressInput));

            ArrayList<String> currentStops = new ArrayList<>();
            currentStops.add(text(firstStop));
            currentStops.add(text(secondStop));
            currentStops.add(text(thirdStop));
            currentStops.add(text(fourthStop));
            currentStops.add(text(fifthStop));
            currentStops.add(text(sixthStop));
            args.putStringArrayList("stoppingPoints", currentStops);

            // PROMENI ID na tvoj pravi favourite routes fragment u nav_graph.xml
            NavHostFragment.findNavController(this)
                    .navigate(R.id.favouriteRouteFragment, args);
        });

        // Form switching
        addStoppingPointsButton.setOnClickListener(v -> showStoppingPointsForm());
        doneStopButton.setOnClickListener(v -> showMainForm());

        linkOtherPassengersButton.setOnClickListener(v -> showLinkedPassengersForm());
        doneLinkButton.setOnClickListener(v -> showMainForm());

        // Order
        orderButton.setOnClickListener(v -> onOrderClicked());

        // Observe VM
        vm.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            boolean loading = isLoading != null && isLoading;
            orderButton.setEnabled(!loading);
            addStoppingPointsButton.setEnabled(!loading);
            linkOtherPassengersButton.setEnabled(!loading);
        });

        vm.getMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                vm.clearMessage();
            }
        });

        vm.getCreatedRide().observe(getViewLifecycleOwner(), ride -> {
            if (ride != null) {
                Toast.makeText(requireContext(), "Ride id: " + ride.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        // Start state
        showMainForm();
    }

    private void applyRouteArgsIfPresent() {
        Bundle args = getArguments();
        if (args == null) return;

        String start = args.getString("startAddress", "");
        String end = args.getString("endAddress", "");
        ArrayList<String> stops = args.getStringArrayList("stoppingPoints");

        if (startAddressInput != null && start != null && !start.isEmpty()) {
            startAddressInput.setText(start);
        }
        if (endAddressInput != null && end != null && !end.isEmpty()) {
            endAddressInput.setText(end);
        }

        // Popuni stop inpute redom (max 6)
        if (stops != null) {
            setStopText(firstStop,  stops, 0);
            setStopText(secondStop, stops, 1);
            setStopText(thirdStop,  stops, 2);
            setStopText(fourthStop, stops, 3);
            setStopText(fifthStop,  stops, 4);
            setStopText(sixthStop,  stops, 5);

            // Ako ima stopova, opciono: pokaži stopping points form automatski
            // (ako ti je UX bolji da user odmah vidi da su popunjeni)
            // if (!stops.isEmpty()) showStoppingPointsForm();
        }

        // (Opcionalno) Ako hoćeš da se args ne koriste opet kad se vratiš na fragment:
        // setArguments(null);
    }

    private void setStopText(EditText et, ArrayList<String> stops, int index) {
        if (et == null) return;
        if (stops.size() > index) {
            String v = stops.get(index);
            et.setText(v != null ? v : "");
        } else {
            et.setText("");
        }
    }


    private void onOrderClicked() {
        TokenStorage ts = new TokenStorage(requireContext());
        Long creatorId = ts.getUserId();
        if (creatorId == null) {
            Toast.makeText(requireContext(), "Nisi ulogovan.", Toast.LENGTH_SHORT).show();
            return;
        }

        String start = text(startAddressInput);
        String end = text(endAddressInput);
        String scheduled = text(scheduledTimeInput);
        String vehicleType = vehicleTypeDropdown.getText() != null ? vehicleTypeDropdown.getText().toString().trim() : "STANDARD";

        boolean baby = yesBabyTransport.isChecked();
        boolean pet = yesPetFriendly.isChecked();

        List<String> stops = new ArrayList<>();
        stops.add(text(firstStop));
        stops.add(text(secondStop));
        stops.add(text(thirdStop));
        stops.add(text(fourthStop));
        stops.add(text(fifthStop));
        stops.add(text(sixthStop));

        List<String> emails = new ArrayList<>();
        emails.add(text(firstLinked));
        emails.add(text(secondLinked));
        emails.add(text(thirdLinked));

        vm.orderRide(
                creatorId,
                start,
                end,
                stops,
                emails,
                scheduled,
                vehicleType,
                baby,
                pet
        );
    }

    private String text(EditText et) {
        if (et == null || et.getText() == null) return "";
        return et.getText().toString().trim();
    }

    // ---------- bind included forms ----------

    private void bindRideOrderingForm(View root) {
        startAddressInput = root.findViewById(R.id.startAddressInput);
        endAddressInput = root.findViewById(R.id.endAddressInput);
        scheduledTimeInput = root.findViewById(R.id.scheduledTimeInput);
        vehicleTypeDropdown = root.findViewById(R.id.vehicleTypeDropdown);

        yesBabyTransport = root.findViewById(R.id.yesBabyTransport);
        yesPetFriendly = root.findViewById(R.id.yesPetFriendly);

        chooseFromFavButton = root.findViewById(R.id.chooseButton);
        addStoppingPointsButton = root.findViewById(R.id.addStoppingPointsButton);
        linkOtherPassengersButton = root.findViewById(R.id.linkOtherPassengersButton);
        orderButton = root.findViewById(R.id.orderButton);
    }

    private void bindStoppingPointsForm(View root) {
        firstStop = root.findViewById(R.id.firstStoppingPointInput);
        secondStop = root.findViewById(R.id.secondStoppingPointInput);
        thirdStop = root.findViewById(R.id.thirdStoppingPointInput);
        fourthStop = root.findViewById(R.id.fourthStoppingPointInput);
        fifthStop = root.findViewById(R.id.fifthStoppingPointInput);
        sixthStop = root.findViewById(R.id.sixthStoppingPointInput);
        doneStopButton = root.findViewById(R.id.doneStopButton);
    }

    private void bindLinkedPassengersForm(View root) {
        firstLinked = root.findViewById(R.id.firstLinkedPassengerInput);
        secondLinked = root.findViewById(R.id.secondLinkedPassengerInput);
        thirdLinked = root.findViewById(R.id.thirdLinkedPassengerInput);
        doneLinkButton = root.findViewById(R.id.doneLinkButton);
    }

    // ---------- visibility switching ----------

    private void showMainForm() {
        binding.rideOrderingForm.getRoot().setVisibility(View.VISIBLE);
        binding.stoppingPointsForm.getRoot().setVisibility(View.INVISIBLE);
        binding.linkedPassengersForm.getRoot().setVisibility(View.INVISIBLE);
    }

    private void showStoppingPointsForm() {
        binding.rideOrderingForm.getRoot().setVisibility(View.INVISIBLE);
        binding.stoppingPointsForm.getRoot().setVisibility(View.VISIBLE);
        binding.linkedPassengersForm.getRoot().setVisibility(View.INVISIBLE);
    }

    private void showLinkedPassengersForm() {
        binding.rideOrderingForm.getRoot().setVisibility(View.INVISIBLE);
        binding.stoppingPointsForm.getRoot().setVisibility(View.INVISIBLE);
        binding.linkedPassengersForm.getRoot().setVisibility(View.VISIBLE);
    }

    // ---------- Nominatim Retrofit (separate baseUrl) ----------

    private NominatimService provideNominatimService() {
        OkHttpClient http = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request r = chain.request().newBuilder()
                            .header("User-Agent", requireContext().getPackageName() + " (student-project)")
                            .build();
                    return chain.proceed(r);
                })
                .build();

        Retrofit nominatimRetrofit = new Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org/")
                .client(http)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return nominatimRetrofit.create(NominatimService.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
