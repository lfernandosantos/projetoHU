package br.com.lf.hotelurbano;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.lf.hotelurbano.json.CatalogoHoteis;
import br.com.lf.hotelurbano.intefaces.IHotelService;
import br.com.lf.hotelurbano.models.Hotel;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HoteisActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnBuscar;
    private EditText editTextCidadeHotel;
    private EditText editTextDataEntrada;
    private EditText editTextDataSaida;
    private CheckBox checkBoxData;
    private TextView textViewCheckBox;
    ProgressDialog progressDialog;
    DateFormat format = DateFormat.getDateInstance();
    Calendar dateTime = Calendar.getInstance();
    Boolean dateEntrada = false;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoteis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buscando...");

        btnBuscar = (Button) findViewById(R.id.btn_buscar);
        editTextCidadeHotel = (EditText) findViewById(R.id.edit_local);
        editTextDataEntrada = (EditText) findViewById(R.id.editDataEntrada);
        editTextDataSaida = (EditText) findViewById(R.id.editDataSaida);
        checkBoxData = (CheckBox) findViewById(R.id.checkboxData);
        textViewCheckBox = (TextView) findViewById(R.id.textViewCheckBox);

        checkBoxData.setChecked(true);
        textViewCheckBox.setOnClickListener(this);
        btnBuscar.setOnClickListener(this);
        editTextDataSaida.setOnClickListener(this);
        editTextDataEntrada.setOnClickListener(this);

        esconderTeclado();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfYear) {

            Date dat = new Date(year, monthOfYear, dayOfYear);

            if (dateEntrada)
            {
                editTextDataEntrada.getText().clear();
                editTextDataEntrada.setText(simpleDateFormat.format(dat));
            }
            if (!dateEntrada)
            {
                editTextDataSaida.getText().clear();
                editTextDataSaida.setText(simpleDateFormat.format(dat));
            }

        }
    };

    private void updateDate(){
       DatePickerDialog datePiker = new DatePickerDialog(this, d,dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),
                dateTime.get(Calendar.DAY_OF_MONTH));

        datePiker.show();
    }

    @Override
    public void onClick(View view) {

        esconderTeclado();

        if (view == btnBuscar){
            if (!editTextCidadeHotel.getText().toString().trim().equals("")){

                if (checkBoxData.isChecked()) {
                    progressDialog.show();
                    buscaJSON(editTextCidadeHotel.getText().toString().trim());
                }else {
                    if (!editTextDataEntrada.getText().toString().isEmpty()){

                        if (!editTextDataSaida.getText().toString().isEmpty()) {
                            progressDialog.show();
                            buscaJSON(editTextCidadeHotel.getText().toString().trim());
                        }else {
                            editTextDataSaida.requestFocus();
                            Snackbar.make(editTextCidadeHotel, "Informe a da data de saída!", Snackbar.LENGTH_SHORT).show();
                        }
                    }else {
                        editTextDataEntrada.requestFocus();
                        Snackbar.make(editTextCidadeHotel, "Informe a data de entrada!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }else {
                editTextCidadeHotel.requestFocus();
                Snackbar.make(editTextCidadeHotel, "Informe a cidade ou hotel!", Snackbar.LENGTH_SHORT).show();
            }
        }

        if (view == editTextDataEntrada){
            dateEntrada = true;
            updateDate();
        }

        if (view == editTextDataSaida){
            dateEntrada = false;
            updateDate();
        }
        if (view == textViewCheckBox){
            if (checkBoxData.isChecked()){
                checkBoxData.setChecked(false);
            }else checkBoxData.setChecked(true);
        }
    }

    private void esconderTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(editTextCidadeHotel.getWindowToken(), 0);
    }

    private void buscaJSON(final String nome) {

        OkHttpClient okHttpClient = getRequestHeader();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IHotelService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        IHotelService jsonPostos = retrofit.create(IHotelService.class);

        Call<CatalogoHoteis> listaRequest = jsonPostos.listaHotel(nome);
        listaRequest.enqueue(new Callback<CatalogoHoteis>() {
            @Override
            public void onResponse(Call<CatalogoHoteis> call, Response<CatalogoHoteis> response) {
                if (!response.isSuccessful()){
                    Log.i("TAG", "ERRO: " + response.code());
                    progressDialog.dismiss();

                    new AlertDialog.Builder(HoteisActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Erro de Comunicação")
                            .setMessage("Erro ao realizar a busca. Verifique sua conexão e tente novamente!")
                            .setNeutralButton("OK", null)
                            .show();

                }else{
                    Log.i("TAG", "OK: " + response.body());

                    CatalogoHoteis catalogo = response.body();

                    List<Hotel> hoteis = catalogo.hoteis;

                    progressDialog.dismiss();
                    if (hoteis.size()>1){
                        Intent goBusca =  new Intent(HoteisActivity.this, BuscaActivity.class);
                        goBusca.putExtra("busca", nome);
                        goBusca.putExtra("hoteis", (Serializable) hoteis);
                        goBusca.putExtra("periodo", editTextDataEntrada.getText().toString().trim()
                        +" - " + editTextDataSaida.getText().toString().trim());
                        startActivity(goBusca);
                    }
                }
            }
            @Override
            public void onFailure(Call<CatalogoHoteis> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("TAG", "ERRO: " + t);
                new AlertDialog.Builder(HoteisActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Erro de Comunicação")
                        .setMessage("Erro ao realizar a busca. Verifique sua conexão e tente novamente!")
                        .setNeutralButton("OK", null)
                        .show();
            }
        });

    }

    private OkHttpClient getRequestHeader() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        return okHttpClient;
    }
}
