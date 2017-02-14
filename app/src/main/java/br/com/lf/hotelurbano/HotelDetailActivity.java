package br.com.lf.hotelurbano;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.lf.hotelurbano.models.Disponibilidade;
import br.com.lf.hotelurbano.models.Hotel;

public class HotelDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Hotel hotel;
    private List<Disponibilidade> disponibilidade;
    private TextView nome;
    private TextView cidade;
    private ListView listView;
    private Button imgPeriodo;
    Calendar dateTime = Calendar.getInstance();
    Boolean dateEntrada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        nome = (TextView) findViewById(R.id.txtnomes);
        cidade = (TextView) findViewById(R.id.txtcidade);
        listView = (ListView) findViewById(R.id.listaDatas);
        imgPeriodo = (Button) findViewById(R.id.btn_selecionar_datas);

        disponibilidade = (List<Disponibilidade>) getIntent().getSerializableExtra("disp");
        hotel = (Hotel) getIntent().getSerializableExtra("hotel");

        setTitle(hotel.nome);
        toolbar.setSubtitle(hotel.cidade);
        nome.setText(hotel.nome);
        cidade.setText(hotel.cidade);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, disponibilidade);

        listView.setAdapter(adapter); 
        imgPeriodo.setOnClickListener(this);

    }
    private void updateDate(){
        new DatePickerDialog(this, d,dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),
                dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfYear) {

            Date dat = new Date(year, monthOfYear, dayOfYear);

            if (dateEntrada)
            {

            }
            if (!dateEntrada)
            {

            }

        }
    };

    @Override
    public void onClick(View view) {

        updateDate();
    }
}
