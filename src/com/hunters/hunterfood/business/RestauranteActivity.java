package com.hunters.hunterfood.business;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hunters.hunterfood.R;
import com.hunters.hunterfood.model.Prato;
import com.hunters.hunterfood.model.Restaurante;

/**
 * Classe responsavel por iniciar a activity_restaurante
 * @author BrunoMeira
 *
 */
public class RestauranteActivity extends Activity {
	
	private TextView nomeRestaurante;
	private TextView endereco;
	private TextView cidade;
	private TextView especialidadeGastronomica;
	private TextView horario;
	private Restaurante restaurante;
	
	private ListView lista;
	private ArrayList<Prato> pratos = new ArrayList<Prato>();

	/**
	 * Metodo responsavel por criar a tela
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurante);
		
		Intent intent = getIntent();
		restaurante = (Restaurante) intent.getSerializableExtra("restaurante");
		
		nomeRestaurante = (TextView) findViewById(R.id.nome_restaurante);
		endereco = (TextView) findViewById(R.id.endereco);
		cidade = (TextView) findViewById(R.id.cidade);
		especialidadeGastronomica = (TextView) findViewById(R.id.especialidade_gastronomica);
		horario = (TextView) findViewById(R.id.horario);
		
		nomeRestaurante.setText(getString(R.string._r+Integer.valueOf(restaurante.getIdRestaurante())));
		endereco.setText(restaurante.getEndereco());
		cidade.setText(restaurante.getCidade());
		especialidadeGastronomica.setText(R.string._e+Integer.valueOf(restaurante.getEspecialidadeGastronomica()));
		horario.setText(getString(R.string._h+Integer.valueOf(restaurante.getIdRestaurante())));
		
		lista = (ListView) findViewById(R.id.lista_cardapio);
		
		new PratosTask().execute();
		
		/*
		 * Chama a classe PratoActivity que inicializa a activity_pratos com as informações do prato clicado
		 */
		lista.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id) {
				
				Prato elemento = pratos.get(posicao);
				
				elemento.getIdRestaurante();
				
				Intent i = new Intent(RestauranteActivity.this, PratoActivity.class);
				i.putExtra("prato", elemento);
				
                startActivity(i);
				
			} 
		});
	
		Button botao = (Button) findViewById(R.id.botao);
		
		/*
		 * Chama a classe MapsActivity que inicializa a activity_maps
		 */
		botao.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Restaurante elemento = restaurante;
				Intent i = new Intent(RestauranteActivity.this, MapsActivity.class);
				i.putExtra("restaurante", elemento);
				
                startActivity(i);
			}
			});
		
		
	}

	/**
	 * Infla a barra de menu do Android
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.restaurante, menu);
		return true;
	}
	
	/**
	 * Acessa o WebService para obter a lista de pratos do restaurante selecionado
	 * @param url
	 * @return
	 */
	public String getRESTFileContent(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);

		try {
			HttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = new String(getBytes(instream));

				instream.close();
				return result;
			}
		} catch (Exception e) {
			Log.e("HUNTERS", "Falha ao acessar Web service", e);
		}
		return null;
	}
	
	/**
	 * Recebe o inputStream contendo as informações dos pratos enviados por Json
	 * @param is
	 * @return
	 */
	public byte[] getBytes(InputStream is) {
		try {
			int bytesLidos;
			ByteArrayOutputStream bigBuffer = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];

			while ((bytesLidos = is.read(buffer)) > 0) {
				bigBuffer.write(buffer, 0, bytesLidos);
			}

			return bigBuffer.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Classe interna que monta cada item da lista de pratos a partir de resultado da busca ao banco de dados
	 * @author BrunoMeira
	 *
	 */
	private class PratosTask extends AsyncTask<String, Void, String[]> {

		ProgressDialog dialog;
		
		/**
		 * Abre um ProgressDialog enquanto os restaurantes são carregados
		 */
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(RestauranteActivity.this);
			dialog.setMessage(getString(R.string.carregando));
			dialog.show();
		}
		
		/**
		 * Utiliza da Classe ArrayAdapter para montar cada item da lista de pratos
		 */
		@Override
		protected void onPostExecute(String[] result) {
			if(result != null){
					ArrayAdapter<String> adapter =	new ArrayAdapter<String>(getBaseContext(),	android.R.layout.simple_list_item_1, result);
					lista.setAdapter(adapter);
			}
			dialog.dismiss();
		}
		
		/**
		 * Faz a consulta no banco de dados para obter todos os pratos do restaurante selecionado
		 */
		@Override
		protected String[] doInBackground(String... params) { 
			try {
				
				String urlServidor = "http://hunter.jelasticlw.com.br/WebService/prato/restaurantePrato/" + restaurante.getIdRestaurante();
				String url = Uri.parse(urlServidor).toString();
				String conteudo = getRESTFileContent(url);
				
				
				// pegamos o resultado
				JSONObject jsonObject = new JSONObject(conteudo);
				JSONArray resultados = jsonObject.getJSONArray("prato");
				
				
				// montamos o resultado
				String[] listaPratos;
				
				
				listaPratos = new String[resultados.length()];
				
				for (int i = 0; i < resultados.length(); i++) {
					
					Prato prato = new Prato();
					
					JSONObject tweet = resultados.getJSONObject(i);
					
					prato.setNome(tweet.getString("nome"));
					prato.setDescricao(tweet.getString("descricao"));
					prato.setIdPrato(tweet.getString("idPrato"));
					prato.setIdRestaurante(tweet.getString("idRestaurante"));
					prato.setPreco(tweet.getString("preco"));
					
						
					pratos.add(prato);
					
					listaPratos[i] = getString(R.string._p+Integer.valueOf(prato.getIdPrato())) + " - R$ " + prato.getPreco();
						
				}
					
				return listaPratos;
				
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
		}
	
	}


}
