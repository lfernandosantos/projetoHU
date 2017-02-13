package br.com.lf.hotelurbano.models;

import java.io.Serializable;

/**
 * Created by ferna on 04/02/2017.
 */

public class Disponibilidade implements Serializable{
    public String hotel;
    public String data;
    public String disponinilidade;
    public String minimoDeNoites;

    @Override
    public String toString() {
        return data;
    }
}
