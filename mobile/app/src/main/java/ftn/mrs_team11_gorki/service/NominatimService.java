package ftn.mrs_team11_gorki.service;

import ftn.mrs_team11_gorki.dto.NominatimPlaceDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import java.util.List;

public interface NominatimService {
    @GET("search")
    Call<List<NominatimPlaceDTO>> search(
            @Query("q") String q,
            @Query("format") String format,      // "json"
            @Query("limit") int limit,
            @Query("addressdetails") int addressDetails,
            @Query("countrycodes") String countryCodes // npr "rs"
    );
}