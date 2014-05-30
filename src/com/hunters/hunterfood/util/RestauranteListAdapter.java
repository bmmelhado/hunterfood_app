package com.hunters.hunterfood.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunters.hunterfood.R;
import com.hunters.hunterfood.model.Restaurante;

/**
 * Classe responsavel por organizar as informações do restaurante em uma lista
 * @author BrunoMeira
 *
 */
public class RestauranteListAdapter extends ArrayAdapter<Restaurante> {
	
	private List<Restaurante> mRestauranteList;
	private int mLayout;
	@SuppressWarnings("unused")
	private Context mContext;
	private ArrayList<Integer> imagens = new ArrayList<Integer>();
	
	static class ViewHolder {
		private TextView mNome;
		private TextView mEndereco;
		private TextView mCidade;
		private TextView mDistancia;
		private ImageView mEspecialidade;
	}
	
	/**
	 * Verifica o tamanho da lista de restaurantes
	 */
	@Override
	public int getCount() {
		return mRestauranteList.size();
	}
	
	/**
	 * Metodo responsavel por fazer o parse das informações da lista de restaurante e os itens da tela
	 */
	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		
		View view = null;
		ViewHolder viewHolder = null;
		
		if (contentView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(mLayout, null);
			
			if (view != null) {
				viewHolder = new ViewHolder();
				viewHolder.mNome = (TextView) view.findViewById(R.id.nome_item);
				viewHolder.mEndereco = (TextView) view.findViewById(R.id.endereco_item);
				viewHolder.mCidade = (TextView) view.findViewById(R.id.cidade_item);
				viewHolder.mDistancia = (TextView) view.findViewById(R.id.distancia_item);
				viewHolder.mEspecialidade = (ImageView) view.findViewById(R.id.icone_item);
				view.setTag(viewHolder);
			}
		} else {
			view = contentView;
			viewHolder = (ViewHolder) contentView.getTag();
		}
		
		if (viewHolder != null) {
			Restaurante restaurante = mRestauranteList.get(position);
			if (restaurante != null) {
				try {
					viewHolder.mNome.setText(restaurante.getNome());
					viewHolder.mEndereco.setText(restaurante.getEndereco());
					viewHolder.mCidade.setText(restaurante.getCidade());
					viewHolder.mDistancia.setText(restaurante.getDistancia());
					viewHolder.mEspecialidade.setImageResource(imagens.get(Integer.valueOf(restaurante.getEspecialidadeGastronomica())-1));
				} catch (Exception e) {
					Log.e("HUNTERS", "Falha ao acessar Web service", e);
				}
				
			}
		}

		return view;
	}

	/**
	 * Metodo responsavel por atribuir uma imagem para cada especialidade gastronomica da lista(imagens diferentes para cada especialidade)
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public RestauranteListAdapter(Context context, int textViewResourceId,
			List<Restaurante> objects) {
		super(context, textViewResourceId, objects);
		imagens.add(1);
		imagens.add(R.drawable.e2);
		imagens.add(R.drawable.e3);
		imagens.add(R.drawable.e4);
		imagens.add(R.drawable.e5);
		imagens.add(R.drawable.e6);
		imagens.add(R.drawable.e7);
		imagens.add(R.drawable.e8);
		
		this.mRestauranteList = objects;
		this.mLayout = textViewResourceId;
		this.mContext = context;
		
	}

}
