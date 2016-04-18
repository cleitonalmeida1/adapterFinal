package cleiton.agenda.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cleiton.agenda.R;
import cleiton.agenda.model.Agenda;
import cleiton.agenda.util.FireBaseUtil;

public class MainActivity extends AppCompatActivity {
    private Firebase firebase;

    @Bind(R.id.lista)
    ListView listView;

    private List<Agenda> agendas = new ArrayList<Agenda>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        firebase = FireBaseUtil.getFirebase();
    }



    @OnClick(R.id.fab)
    public void onFabCad(View view) {
        Intent i = new Intent(getBaseContext(), Update.class);
        startActivity(i);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Firebase mFirebase = firebase.child("agenda");
        /**
         * adapter personalisado para uso do firebase
         */
        FirebaseListAdapter<Agenda> adapter =
                new FirebaseListAdapter<Agenda>(this,
                        Agenda.class,
                        R.layout.item_layout,
                        mFirebase) {
                    @Override
                    protected void populateView(View convertView, Agenda l, int i) {
                        ViewHolder holder;

                        holder = new ViewHolder(convertView);

                        holder.nome.setText(l.getNome());

                        holder.telefone.setText(l.getTelefone());

                        String url = l.getImagem();
                        if (url != null) {
                            Bitmap imagemFoto = BitmapFactory.decodeFile(url);
                            holder.imagem.setImageBitmap(imagemFoto);
                            holder.imagem.setTag(url);
                        }

                    }
                };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("men", "update" + agendas.get(position).getNome());
                Intent i = new Intent(getBaseContext(), Update.class);
                i.putExtra("agenda", agendas.get(position));
                startActivity(i);
            }
        });

        /**
         * funcao para atualizar os telefonees na lista local
         */
        mFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    agendas.add(data.getValue(Agenda.class));
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     *  classe que referencia elemntos da listView
     */
    static class ViewHolder {
        @Bind(R.id.tvNome)
        TextView nome;
        @Bind(R.id.tvTelefone)
        TextView telefone;
        @Bind(R.id.imageView)
        ImageView imagem;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}