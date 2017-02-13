package br.com.lf.hotelurbano;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.lf.hotelurbano.intefaces.RecyclerViewOnClickListenerHack;
import br.com.lf.hotelurbano.json.CatalogoDisponibilidades;
import br.com.lf.hotelurbano.json.CatalogoHoteis;
import br.com.lf.hotelurbano.json.IHotelService;
import br.com.lf.hotelurbano.models.Disponibilidade;
import br.com.lf.hotelurbano.models.Hotel;
import br.com.lf.hotelurbano.models.ListaHoteisAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // listViewBusca = (ListView) findViewById(R.id.listaHoteis);
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

       // ArrayAdapter<List<Hotel>> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, hoteis);

        //listViewBusca.setAdapter(adapter);

//        listViewBusca.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Hotel h = (Hotel) adapterView.getItemAtPosition(i);
//                Log.i("Hotel", h.codigo);
//                buscaJSON(h);
//            }
//        });
    }

    private void buscaJSON(final Hotel hotel) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IHotelService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

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
            }
            @Override
            public void onFailure(Call<CatalogoDisponibilidades> call, Throwable t) {

                Log.i("TAG", "ERRO: " + t);
            }
        });

    }


    @Override
    public void onClickListener(View view, int position) {

        Hotel h = hoteis.get(position);
        Log.i("Hotel", h.codigo);
        buscaJSON(h);
    }
}
