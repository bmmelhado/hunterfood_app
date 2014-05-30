package com.hunters.hunterfood.business;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hunters.hunterfood.R;
import com.hunters.hunterfood.model.Restaurante;

 
/**
 * Classe responsavel por iniciar tela de mapa, que mostra a rota do ponto onde se encontra o 
 * usuário até o restaurante selecionado
 * @author BrunoMeira
 *
 */
public class MapsActivity extends Activity {
	
	private WebView webView;
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
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        
        LM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		bestProvider= LM.getBestProvider(new Criteria(),true);
		l = LM.getLastKnownLocation(bestProvider);
		latitude = String.valueOf(l.getLatitude());
		longitude = String.valueOf(l.getLongitude());
        
        
        Intent intent = getIntent();
		Restaurante restaurante = (Restaurante) intent.getSerializableExtra("restaurante");
 
        webView = (WebView) findViewById(R.id.mapa);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        String destino = restaurante.getEndereco().replace(" ", "+");
     
        //Passa as informações de localização para o google maps, que retorna a web view com o mapa
        webView.loadUrl("http://maps.google.com/maps?" + "saddr=" + latitude + "," + longitude + "&daddr=" + destino);      
        
    }
 
}