package br.com.lf.hotelurbano.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.com.lf.hotelurbano.models.Hotel;

/**
 * Created by ferna on 17/01/2017.
 */

public class CatalogoHoteis {

    @SerializedName("hoteis")
    public List<Hotel> hoteis;
}
