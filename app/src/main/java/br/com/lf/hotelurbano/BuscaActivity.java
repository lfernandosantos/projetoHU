package br.com.lf.hotelurbano;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.lf.hotelurbano.intefaces.RecyclerViewOnClickListenerHack;
import br.com.lf.hotelurbano.json.CatalogoDisponibilidades;
import br.com.lf.hotelurbano.intefaces.IHotelService;
import br.com.lf.hotelurbano.models.Disponibilidade;
import br.com.lf.hotelurbano.models.Hotel;
import br.com.lf.hotelurbano.models.ListaHoteisAdapter;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuscaActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    private ListView listViewBusca;
    private TextView textViewDadosBusca;
    private ImageView imageViewPeriodo;
    private RecyclerView recyclerViewHoteis;
    ListaHoteisAdapter hoteisAdapter;
    List<Hotel> hoteis;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        textViewDadosBusca = (TextView) findViewById(R.id.textViewPeriodo);
        imageViewPeriodo = (ImageView) findViewById(R.id.imgPeriodo);
        recyclerViewHoteis = (RecyclerView) findViewById(R.id.listaHoteis);
        recyclerViewHoteis.setHasFixedSize(true);

        hoteis = (List<Hotel>) this.getIntent().getSerializableExtra("hoteis");
        String busca = (String) getIntent().getSerializableExtra("busca");
        String periodo = getIntent().getStringExtra("periodo");

        setTitle(busca);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewHoteis.setLayoutManager(llm);

        hoteisAdapter = new ListaHoteisAdapter(this, hoteis);
        hoteisAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewHoteis.setAdapter(hoteisAdapter);

        if (periodo.equals(" - ")){
            textViewDadosBusca.setVisibility(View.GONE);
            imageViewPeriodo.setVisibility(View.GONE);
        }
        textViewDadosBusca.setText(periodo);

    }

    private void buscaJSON(final Hotel hotel) {

        OkHttpClient okHttpClient = getRequestHeader();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IHotelService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        IHotelService jsonPostos = retrofit.create(IHotelService.class);

        Call<CatalogoDisponibilidades> listaRequest = jsonPostos.listaDisponibilidade(hotel.codigo);
        listaRequest.enqueue(new Callback<CatalogoDisponibilidades>() {
            @Override
            public void onResponse(Call<CatalogoDisponibilidades> call, Response<CatalogoDisponibilidades> response) {
                if (!response.isSuccessful()){
                    Log.i("TAG", "ERRO: " + response.code());


                }else{
                    Log.i("TAG", "OK: " + response.body());

                    CatalogoDisponibilidades catalogo = response.body();

                    List<Disponibilidade> disponibilidades = catalogo.disponibilidades;

                    Log.i("Disp", disponibilidades.get(0).hotel);
                    Intent goHotel =  new Intent(BuscaActivity.this, HotelDetailActivity.class);
                    goHotel.putExtra("disp", (Serializable) disponibilidades);
                    goHotel.putExtra("hotel", hotel);
                    startActivity(goHotel);

                }

                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<CatalogoDisponibilidades> call, Throwable t) {

                progressDialog.dismiss();
                Log.i("TAG", "ERRO: " + t);
            }
        });

    }


    @Override
    public void onClickListener(View view, int position) {
        Hotel h = hoteis.get(position);
        Log.i("Hotel", h.codigo);

        progressDialog.setMessage("Aguarde...");
        progressDialog.show();
        buscaJSON(h);
    }
    private OkHttpClient getRequestHeader() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        return okHttpClient;
    }
}
