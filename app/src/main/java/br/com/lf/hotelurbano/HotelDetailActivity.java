package br.com.lf.hotelurbano;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.lf.hotelurbano.models.Disponibilidade;
import br.com.lf.hotelurbano.models.Hotel;

public class HotelDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, DialogInterface.OnCancelListener, View.OnClickListener {

    private Hotel hotel;
    private List<Disponibilidade> disponibilidade;
    private TextView nome;
    private TextView cidade;
    private ImageView imgCalendarEntrada;
    private ImageView imgCalendarSaida;
    private TextView textVDataEntrada;
    private TextView textViewEntrada;
    private TextView textVDataSaida;
    private TextView textViewSaida;
    private ImageView imgHotel;
    private Button btnReservar;
    private int year, month, day;
    SimpleDateFormat format = new SimpleDateFormat("d/MM/yyyy");
    private Boolean dataSaída = true;
    Calendar preDateEntrada;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //finds
        nome = (TextView) findViewById(R.id.txtnomes);
        cidade = (TextView) findViewById(R.id.txtcidade);
        imgCalendarEntrada = (ImageView) findViewById(R.id.img_calendar_entrada);
        textViewEntrada = (TextView) findViewById(R.id.textViewEntrada);
        btnReservar = (Button) findViewById(R.id.btn_reservar);

        imgCalendarSaida = (ImageView) findViewById(R.id.img_calendar_saida);
        textViewSaida = (TextView) findViewById(R.id.textViewSaida);

        textVDataSaida = (TextView) findViewById(R.id.edit_data_saida);
        textVDataEntrada = (TextView) findViewById(R.id.edit_data_entrada);
        imgHotel = (ImageView) findViewById(R.id.img_hotel);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        disponibilidade = (List<Disponibilidade>) getIntent().getSerializableExtra("disp");
        hotel = (Hotel) getIntent().getSerializableExtra("hotel");

        mCollapsingToolbarLayout.setTitle(hotel.nome);

        toolbar.setSubtitle(hotel.cidade);
        nome.setText(hotel.nome);
        cidade.setText(hotel.cidade);
        Glide.with(this)
                .load("https://s3.amazonaws.com/ah.sized.images/desktop/luxury-hotels-rio-de-janeiro-belmond-copacabana-palace-slide-8_lg.jpg")
                .into(imgHotel);

        imgCalendarEntrada.setOnClickListener(this);
        textVDataEntrada.setOnClickListener(this);
        textViewEntrada.setOnClickListener(this);

        imgCalendarSaida.setOnClickListener(this);
        textVDataSaida.setOnClickListener(this);
        textViewSaida.setOnClickListener(this);
        btnReservar.setOnClickListener(this);



    }
    private void updateDate(List<Disponibilidade> disp){

        initDateTimeData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(year, month, day);

        DatePickerDialog pickerDialog = new DatePickerDialog().newInstance(this, cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),
                cDefault.get(Calendar.DAY_OF_MONTH));


        Calendar cMin = Calendar.getInstance();
        Calendar cMax = Calendar.getInstance();
        cMax.set(cMax.get(Calendar.YEAR), 11, 31);
        cMin.set(cMin.get(Calendar.YEAR), cMin.get(Calendar.MONTH), cMin.get(Calendar.DAY_OF_MONTH )- 1);
        pickerDialog.setMinDate(cMin);
        pickerDialog.setMaxDate(cMax);

        List<Calendar> daysList = new LinkedList<>();
        Calendar [] daysArray;
        //Calendar cAux = Calendar.getInstance();


        for (Disponibilidade d : disp){
            Calendar calDispHotel = Calendar.getInstance();
            try {
                    calDispHotel.setTime(format.parse(d.data));
                    Log.i("DIA", "format " + calDispHotel.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            if (preDateEntrada != null) {
                if (dataSaída) {
                    pickerDialog.setMinDate(preDateEntrada);
                }
                if (calDispHotel.get(Calendar.DAY_OF_MONTH) != preDateEntrada.get(Calendar.DAY_OF_MONTH)) {
                    daysList.add(calDispHotel);
                } else {
                    if (calDispHotel.get(Calendar.MONTH) != preDateEntrada.get(Calendar.MONTH)){
                        daysList.add(calDispHotel);
                    }
                }
            }else { daysList.add(calDispHotel);}
    }

        daysArray = new Calendar[ daysList.size()];
        for (int i = 0; i < daysArray.length; i++){
            daysArray[i] = daysList.get(i);
//            Calendar calDispHotel = Calendar.getInstance();
//            try {
//                calDispHotel.setTime(format.parse(disp.get(i).data));
//                Log.i("DIA", "format " + calDispHotel.toString());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            if (preDateEntrada != null) {
//                if (calDispHotel.get(Calendar.DAY_OF_MONTH) != preDateEntrada.get(Calendar.DAY_OF_MONTH)) {
//                    daysArray[i] = calDispHotel;
//                } else {
//                    if (calDispHotel.get(Calendar.MONTH) != preDateEntrada.get(Calendar.MONTH)){
//                        daysArray[i] = calDispHotel;
//                    }
//                }
//            }
        }

//        while (cAux.getTimeInMillis() <= cMax.getTimeInMillis()) {
//
//            for (Disponibilidade d : disp) {
//
//                Calendar calDispHotel = Calendar.getInstance();
//
//                try {
//                    calDispHotel.setTime(format.parse(d.data));
//                    Log.i("DIA", "format " + d.data);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                if (cAux.get(Calendar.MONTH) == calDispHotel.get(Calendar.MONTH)
//                        && cAux.get(Calendar.DAY_OF_MONTH) == calDispHotel.get(Calendar.DAY_OF_MONTH)) {
//                    Calendar c = Calendar.getInstance();
//                    c.setTimeInMillis(cAux.getTimeInMillis());
//                    daysList.add(c);
//                    Log.i("DIA", "Aux " + cAux.getTime().toString());
//                    Log.i("DIA", "hotel " + calDispHotel.getTime().toString());
//                }
//            }
//
//            cAux.setTimeInMillis(cAux.getTimeInMillis() + (24 * 60 * 60 * 1000));
//
//        }

//        daysArray = new Calendar[ daysList.size()];
//        for (int i = 0; i < daysArray.length; i++){
//            daysArray[i] = daysList.get(i);
//        }

        pickerDialog.setSelectableDays(daysArray);
        pickerDialog.setOnCancelListener(this);
        pickerDialog.show(getFragmentManager(), "DatePikerDialog" );

    }

    private void initDateTimeData(){
        if (year == 0){
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
    }




    @Override
    public void onCancel(DialogInterface dialogInterface) {
        year = month = day = 0;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd - MMM");
        Date date = new Date(year, monthOfYear, dayOfMonth);
        if (!dataSaída){

            textVDataSaida.clearComposingText();
            textVDataEntrada.setText(simpleDateFormat.format(date));
            preDateEntrada = Calendar.getInstance();
            preDateEntrada.set(year, monthOfYear, dayOfMonth);

        }
        else {
            textVDataSaida.setText(simpleDateFormat.format(date));
        }
    }

    @Override
    public void onClick(View view) {
        if (view == imgCalendarEntrada || view == textVDataEntrada || view == textViewEntrada){
            dataSaída = false;
            updateDate(disponibilidade);
        }
        if (view == imgCalendarSaida || view == textVDataSaida || view == textViewSaida){
            dataSaída = true;
            updateDate(disponibilidade);
        }
        if (view == btnReservar){
            Snackbar.make(btnReservar, "Reverva realizada!", Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.colorAccent))
                    .show();
        }
    }
}
