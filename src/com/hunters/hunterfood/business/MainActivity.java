package com.hunters.hunterfood.business;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hunters.hunterfood.R;
import com.hunters.hunterfood.model.Restaurante;
import com.hunters.hunterfood.util.RestauranteListAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

/**
 * Classe responsavel por iniciar a activity_main
 * @author BrunoMeira
 *
 */
public class MainActivity extends Activity {
	
	private ListView lista;
	private ArrayList<Restaurante> restaurantes = new ArrayList<Restaurante>();
	
	private Spinner spnEspecialidades;
	private List<String> especialidades = new ArrayList<String>();
	private String especialidade;
	
	LocationManager LM;
	String bestProvider ;
	Location l ;
	
	String latitude ;
	String longitude;
 
	/**
	 * Metodo responsavel por criar a tela
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			LM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			bestProvider= LM.getBestProvider(new Criteria(),true);
			l = LM.getLastKnownLocation(bestProvider);
			latitude = String.valueOf(l.getLatitude());
			longitude = String.valueOf(l.getLongitude());
			
		} catch (Exception e) {
			Log.e("HUNTERS", "Falha ao pegar possição", e);
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Popula spinner Especialidades Gastronômicas
		especialidades.add(getString(R.string._e1));
		especialidades.add(getString(R.string._e2));
		especialidades.add(getString(R.string._e3));
		especialidades.add(getString(R.string._e4));
		especialidades.add(getString(R.string._e5));
		especialidades.add(getString(R.string._e6));
		especialidades.add(getString(R.string._e7));
		especialidades.add(getString(R.string._e8));
		
		//Identifica o Spinner no layout
		spnEspecialidades = (Spinner) findViewById(R.id.especialidade_gastronomica);
		
		//Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, especialidades);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spnEspecialidades.setAdapter(spinnerArrayAdapter);
		
		//Método do Spinner para capturar o item selecionado
		spnEspecialidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
 
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
				//pega nome pela posição
				especialidade = parent.getItemAtPosition(posicao).toString();
				
				new RestaurantesTask().execute();
				
			}
 
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
 
			}
		});
		
		lista = (ListView) findViewById(R.id.lista_restaurantes);
		
		/*
		 * Chama a classe RestauranteActivity que inicializa a activity_restaurante com as informações do restaurante clicado
		 */
		lista.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id) {
				
				Restaurante elemento = restaurantes.get(posicao);
				
				elemento.getIdRestaurante();
				
				Intent i = new Intent(MainActivity.this, RestauranteActivity.class);
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
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	/**
	 * Acessa o WebService para obter a lista de restaurantes
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
	 * Recebe o inputStream contendo as informações dos restaurantes enviados por Json
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
	 * Classe interna que monta cada item da lista de restaurante a partir de resultado da busca ao banco de dados
	 * @author BrunoMeira
	 *
	 */
	private class RestaurantesTask extends AsyncTask<String, Void, String[]> {

		ProgressDialog dialog;
		
		/**
		 * Abre um ProgressDialog enquanto os restaurantes são carregados
		 */
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage(getString(R.string.carregando));
			dialog.show();
		}
		
		/**
		 * Utiliza da Classe RestauranteListAdapter para montar cada item da lista de restaurantes
		 */
		@Override
		protected void onPostExecute(String[] result) {
			if(restaurantes != null){
					RestauranteListAdapter restAdapter = new RestauranteListAdapter(getBaseContext(), R.layout.item_list, restaurantes);
					lista.setAdapter(restAdapter);
					
			}
			dialog.dismiss();
		}
		
		/**
		 * Faz a consulta no banco de dados para obter todos os restaurantes
		 */
		@Override
		protected String[] doInBackground(String... params) { 
			try {
				
				String urlServidor = "http://hunter.jelasticlw.com.br/WebService/restaurante/buscarTodos";
				String url = Uri.parse(urlServidor).toString();
				String conteudo = getRESTFileContent(url);				
				
				// pegamos o resultado em Json
				JSONObject jsonObject = new JSONObject(conteudo);
				JSONArray resultados = jsonObject.getJSONArray("restaurante");
				
				// montamos o resultado
				if(especialidade.compareToIgnoreCase(getString(R.string._e1)) == 0){
				
					restaurantes.clear();
					for (int i = 0; i < resultados.length(); i++) {
						
						String endereco = new String();
						String cidade = new String();
						Restaurante restaurante = new Restaurante();
						
						JSONObject tweet = resultados.getJSONObject(i);
						
						restaurante.setDescricao(tweet.getString("descricao"));
						restaurante.setEndereco(tweet.getString("endereco"));
						restaurante.setCidade(tweet.getString("cidade"));
						restaurante.setEspecialidadeGastronomica(tweet.getString("especialidadeGastronomica"));
						restaurante.setHorario(tweet.getString("horario"));
						restaurante.setIdRestaurante(tweet.getString("idRestaurante"));
						restaurante.setNome(getString(R.string._r+Integer.valueOf(restaurante.getIdRestaurante())));
						
						//Faz a busca da distância entre o ponto atual e o restaurante e adiciona esses valores para apresentação em tela
						endereco = tweet.getString("endereco");
						cidade = tweet.getString("cidade");
						String enderecoCompleto = endereco.replace(" ","+") + "+" + cidade.replace(" ", "+");
				
						String urlMapa = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + latitude + "," + longitude + "&destinations=" + enderecoCompleto + "&mode=driving&language=pt-BR&sensor=false";
						String mapa = Uri.parse(urlMapa).toString();
						String distancia = getRESTFileContent(mapa);
						
						JSONObject jsonObjectEnd = new JSONObject(distancia);
	
						JSONArray completoRows = jsonObjectEnd.getJSONArray("rows");
						JSONObject rows = completoRows.getJSONObject(0);
						
						JSONArray completoElements = rows.getJSONArray("elements");
						JSONObject elements = completoElements.getJSONObject(0);
						
						JSONObject distance = elements.getJSONObject("distance");
						
						String  distanciaKM = distance.getString("text");
						Integer metros = Integer.valueOf(distance.getString("value"));
						
						restaurante.setMetros(metros);
						restaurante.setDistancia(distanciaKM);
						
						restaurantes.add(restaurante);
						
					}
					
				// Monta os resultados caso tenha sido selecionado o filtro de especialidades gastronomicas
				}else{
					
					restaurantes.clear();
					
					for(int i = 0; i < resultados.length(); i++){
						
						Restaurante restaurante = new Restaurante();
						
						JSONObject tweet = resultados.getJSONObject(i);
						int esp = Integer.valueOf(tweet.getString("especialidadeGastronomica"));
						
						if((getString(R.string._e+esp)).compareToIgnoreCase(especialidade) == 0){
							
							restaurante.setNome(tweet.getString("nome"));
							restaurante.setDescricao(tweet.getString("descricao"));
							restaurante.setEndereco(tweet.getString("endereco"));
							restaurante.setCidade(tweet.getString("cidade"));
							restaurante.setEspecialidadeGastronomica(tweet.getString("especialidadeGastronomica"));
							restaurante.setHorario(tweet.getString("horario"));
							restaurante.setIdRestaurante(tweet.getString("idRestaurante"));
							
							restaurantes.add(restaurante);
							
						}
						
					}
					
					for(int i = 0; i < restaurantes.size(); i++){
						
						String endereco = new String();
						String cidade = new String();
						
						//Faz a busca da distância entre o ponto atual e o restaurante
						endereco = restaurantes.get(i).getEndereco();
						cidade = restaurantes.get(i).getCidade();
						String enderecoCompleto = endereco.replace(" ","+") + "+" + cidade.replace(" ", "+");
						
						LocationManager LM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
						String bestProvider = LM.getBestProvider(new Criteria(),true);
						Location l = LM.getLastKnownLocation(bestProvider);
						
						 String latitude = String.valueOf(l.getLatitude());
						 String longitude = String.valueOf(l.getLongitude());
				
						String urlMapa = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + latitude + "," + longitude + "&destinations=" + enderecoCompleto + "&mode=driving&language=pt-BR&sensor=false";
						String mapa = Uri.parse(urlMapa).toString();
						String distancia = getRESTFileContent(mapa);
						
						JSONObject jsonObjectEnd = new JSONObject(distancia);
	
						JSONArray completoRows = jsonObjectEnd.getJSONArray("rows");
						JSONObject rows = completoRows.getJSONObject(0);
						
						JSONArray completoElements = rows.getJSONArray("elements");
						JSONObject elements = completoElements.getJSONObject(0);
						
						JSONObject distance = elements.getJSONObject("distance");						
						String  distanciaKM = distance.getString("text");
						Integer metros = Integer.valueOf(distance.getString("value"));
						
						restaurantes.get(i).setMetros(metros);
						restaurantes.get(i).setDistancia(distanciaKM);
						restaurantes.get(i).setNome(getString(R.string._r+Integer.valueOf(restaurantes.get(i).getIdRestaurante())));
						
					}
					
				}
				
				return null;
				
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
		}
	
	}
	

}
