package br.com.lf.hotelurbano.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.com.lf.hotelurbano.models.Disponibilidade;
import br.com.lf.hotelurbano.models.Hotel;

/**
 * Created by ferna on 17/01/2017.
 */

public class CatalogoDisponibilidades {

    @SerializedName("disponibilidade")
    public List<Disponibilidade> disponibilidades;
}
