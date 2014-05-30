package com.hunters.hunterfood.model;

import java.io.Serializable;


public class Prato implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4119920616798476210L;
	
	private String idPrato;
	private String descricao;
	private String preco;
	private String nome;
	private String idRestaurante;
	
	/**
	 * @return the idPrato
	 */
	public String getIdPrato() {
		return idPrato;
	}
	/**
	 * @param idPrato the idPrato to set
	 */
	public void setIdPrato(String idPrato) {
		this.idPrato = idPrato;
	}
	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	/**
	 * @return the preco
	 */
	public String getPreco() {
		return preco;
	}
	/**
	 * @param preco the preco to set
	 */
	public void setPreco(String preco) {
		this.preco = preco;
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
	/**
	 * @return the idRestaurante
	 */
	public String getIdRestaurante() {
		return idRestaurante;
	}
	/**
	 * @param idRestaurante the idRestaurante to set
	 */
	public void setIdRestaurante(String idRestaurante) {
		this.idRestaurante = idRestaurante;
	}
}
	
	


