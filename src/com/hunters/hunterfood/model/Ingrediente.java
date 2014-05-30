package com.hunters.hunterfood.model;

import java.io.Serializable;


public class Ingrediente implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4127312976211730236L;
	private String idIngrediente;
	private String nome;
	
	/**
	 * @return the idIngredientes
	 */
	public String getIdIngredientes() {
		return idIngrediente;
	}
	/**
	 * @param idIngredientes the idIngredientes to set
	 */
	public void setIdIngredientes(String idIngredientes) {
		this.idIngrediente = idIngredientes;
	}
	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	

}
