package br.com.lf.hotelurbano;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
    private Button imgPeriodo;
    private Button btnSelecDateEntrada;
    private Button btnSelecDateSaida;
    private EditText edtDataEntrada;
    private EditText edtDataSaida;
    Calendar dateTime = Calendar.getInstance();
    Boolean dateEntrada = false;
    private int year, month, day;
    SimpleDateFormat format = new SimpleDateFormat("d/MM/yyyy");
    private Boolean dataSaída = true;
    Calendar preDateEntrada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nome = (TextView) findViewById(R.id.txtnomes);
        cidade = (TextView) findViewById(R.id.txtcidade);
        edtDataSaida = (EditText) findViewById(R.id.edit_data_saida);
        edtDataEntrada = (EditText) findViewById(R.id.edit_data_entrada);
        imgPeriodo = (Button) findViewById(R.id.btn_selecionar_datas);
        btnSelecDateSaida = (Button) findViewById(R.id.btn_data_saida);

        imgPeriodo = (Button) findViewById(R.id.btn_selecionar_datas);

        disponibilidade = (List<Disponibilidade>) getIntent().getSerializableExtra("disp");
        hotel = (Hotel) getIntent().getSerializableExtra("hotel");

        setTitle(hotel.nome);
        toolbar.setSubtitle(hotel.cidade);
        nome.setText(hotel.nome);
        cidade.setText(hotel.cidade);

        edtDataEntrada.setOnClickListener(this);

        btnSelecDateSaida.setOnClickListener(this);
        imgPeriodo.setOnClickListener(this);

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

            edtDataSaida.getText().clear();
            edtDataEntrada.setText(simpleDateFormat.format(date));
            preDateEntrada = Calendar.getInstance();
            preDateEntrada.set(year, monthOfYear, dayOfMonth);

        }
        else {
            edtDataSaida.setText(simpleDateFormat.format(date));
        }
    }

    @Override
    public void onClick(View view) {
        if (view == imgPeriodo){
            dataSaída = false;
            updateDate(disponibilidade);
        }
        if (view == btnSelecDateSaida){
            dataSaída = true;
            updateDate(disponibilidade);
        }
    }
}
