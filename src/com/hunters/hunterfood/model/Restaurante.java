package com.hunters.hunterfood.model;

import java.io.Serializable;




public class Restaurante implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2453917363154639986L;
	private String idRestaurante;
	private String nome;
	private String descricao;
	private String endereco;
	private String cidade;
	private String latitude;
	private String longitude;
	private String especialidadeGastronomica;
	private String horario;
	private int metros;
	private String distancia;
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
	 * @return the endereco
	 */
	public String getEndereco() {
		return endereco;
	}
	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	/**
	 * @return the cidade
	 */
	public String getCidade() {
		return cidade;
	}
	/**
	 * @param cidade the cidade to set
	 */
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the especialidadeGastronomica
	 */
	public String getEspecialidadeGastronomica() {
		return especialidadeGastronomica;
	}
	/**
	 * @param especialidadeGastronomica the especialidadeGastronomica to set
	 */
	public void setEspecialidadeGastronomica(String especialidadeGastronomica) {
		this.especialidadeGastronomica = especialidadeGastronomica;
	}
	/**
	 * @return the horario
	 */
	public String getHorario() {
		return horario;
	}
	/**
	 * @param horario the horario to set
	 */
	public void setHorario(String horario) {
		this.horario = horario;
	}
	/**
	 * @return the metros
	 */
	public int getMetros() {
		return metros;
	}
	/**
	 * @param metros the metros to set
	 */
	public void setMetros(int metros) {
		this.metros = metros;
	}
	/**
	 * @return the distancia
	 */
	public String getDistancia() {
		return distancia;
	}
	/**
	 * @param distancia the distancia to set
	 */
	public void setDistancia(String distancia) {
		this.distancia = distancia;
	}

}
