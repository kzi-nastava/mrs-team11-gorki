package ftn.mrs_team11_gorki.service;

import ftn.mrs_team11_gorki.dto.PriceConfigDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface PriceConfService {
    @GET("api/admin/priceConfig")
    Call<PriceConfigDTO> getCurrentPriceConfig();

    @PUT("api/admin/changePriceConfig")
    Call<PriceConfigDTO> updatePriceConfig(@Body PriceConfigDTO dto);
}
