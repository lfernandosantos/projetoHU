package br.com.lf.hotelurbano.intefaces;


import br.com.lf.hotelurbano.json.CatalogoDisponibilidades;
import br.com.lf.hotelurbano.json.CatalogoHoteis;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ferna on 17/01/2017.
 */

public interface IHotelService {

    String BASE_URL = "https://polar-fortress-66094.herokuapp.com/api/";

    @GET("hoteis")
    Call<CatalogoHoteis> listaHotel(@Query("nome") String nome);

    @GET("disponibilidade")
    Call<CatalogoDisponibilidades> listaDisponibilidade(@Query("hotel") String hotel);
}
