package com.example.pharmacie.retrofit;

import com.example.pharmacie.models.Medicament;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface MedicamentApi {
    @Multipart
    @POST("medicament")
    Call<Medicament> uploadMedicament(
            @Part("codeMed") RequestBody codeMed,
            @Part("libelle") RequestBody libelle,
            @Part("dateExpiration") RequestBody dateExpiration,
            @Part("prixUnitaire") RequestBody prixUnitaire,
            @Part("stockMin") RequestBody stockMin,
            @Part("qteStock") RequestBody qteStock,
            @Part("familleMed") RequestBody familleMed,
            @Part MultipartBody.Part image
    );
    @GET("medicaments")
    Call<List<Medicament>> getAllMedicaments();

    @GET("medicament/{id}")
    Call<Medicament> getMedicamentById(@Path("id") Long id);

    @POST("medicament")
    Call<Medicament> createMedicament(@Body Medicament medicament);

    @PUT("medicament/{id}")
    Call<Medicament> updateMedicament(@Path("id") Long id, @Body Medicament medicament);

    @DELETE("medicament/{id}")
    Call<Void> deleteMedicament(@Path("id") Long id);
    @GET("alerts")
    Call<List<Medicament>> getMedicamentsAlerts();
}