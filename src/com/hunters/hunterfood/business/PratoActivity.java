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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hunters.hunterfood.R;
import com.hunters.hunterfood.model.Ingrediente;
import com.hunters.hunterfood.model.Prato;

/**
 * Classe responsavel por iniciar a activity_prato
 * @author BrunoMeira
 *
 */
public class PratoActivity extends Activity {
	
	private TextView nomePrato;
	private TextView descricao;
	private TextView preco;
	private Prato prato;
	
	private ListView lista;
	private ArrayList<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
	
	/**
	 * Metodo responsavel por criar a tela
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prato);
		
		Intent intent = getIntent();
		prato = (Prato) intent.getSerializableExtra("prato");
		
		nomePrato = (TextView) findViewById(R.id.nome_prato);
		descricao = (TextView) findViewById(R.id.descricao_prato);
		preco = (TextView) findViewById(R.id.preco);
		
		nomePrato.setText(getString(R.string._p+Integer.valueOf(prato.getIdPrato())));
		descricao.setText(prato.getDescricao());
		preco.setText("R$ " + prato.getPreco());
		
		lista = (ListView) findViewById(R.id.lista_ingrediente);
		
		new IngredienteTask().execute();
		
	}

	/**
	 * Infla a barra de menu do Android
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.prato, menu);
		return true;
	}
	
	/**
	 * Acessa o WebService para obter a lista de ingredientes do prato selecionado
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
	 * Classe interna que monta cada item da lista de ingredientes a partir de resultado da busca ao banco de dados
	 * @author BrunoMeira
	 *
	 */
	private class IngredienteTask extends AsyncTask<String, Void, String[]> {

		ProgressDialog dialog;
		
		/**
		 * Abre um ProgressDialog enquanto os restaurantes são carregados
		 */
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(PratoActivity.this);
			dialog.setMessage(getString(R.string.carregando));
			dialog.show();
		}
		
		/**
		 * Utiliza da Classe ArrayAdapter para montar cada item da lista de ingredientes
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
		 * Faz a consulta no banco de dados para obter todos os ingredientes do prato selecionado
		 */
		@Override
		protected String[] doInBackground(String... params) { 
			try {
				
				String urlServidor = "http://hunter.jelasticlw.com.br/WebService/ingredientePrato/" + prato.getIdPrato();
				String url = Uri.parse(urlServidor).toString();
				String conteudo = getRESTFileContent(url);
				
				
				// pegamos o resultado em Json
				JSONObject jsonObject = new JSONObject(conteudo);
				JSONArray resultados = jsonObject.getJSONArray("ingredientePrato");
				
				
				// montamos o resultado
				String[] listaIngredientes;
				
				
				listaIngredientes = new String[resultados.length()];
				
				for (int i = 0; i < resultados.length(); i++) {
					
					Ingrediente ingrediente = new Ingrediente();
					
					JSONObject tweet = resultados.getJSONObject(i);
					
					ingrediente.setNome(tweet.getString("nome"));
					ingrediente.setIdIngredientes(tweet.getString("idIngrediente"));
						
					ingredientes.add(ingrediente);
					
					listaIngredientes[i] = getString(R.string._i+Integer.valueOf(ingrediente.getIdIngredientes()));
						
				}
					
				return listaIngredientes;
				
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
		}
	
	}


}
