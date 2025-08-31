package matheus.bcc.calculadoraimc;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String nome, data, condFisica;
    private char sexo;
    private int peso;
    private double altura;
    private double IMC;

    public Usuario(String nome, String data, String condFisica, char sexo, int peso, double altura, double IMC) {
        this.nome = nome;
        this.data = data;
        this.condFisica = condFisica;
        this.sexo = sexo;
        this.peso = peso;
        this.altura = altura;
        this.IMC = IMC;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCondFisica() {
        return condFisica;
    }

    public void setCondFisica(String condFisica) {
        this.condFisica = condFisica;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getIMC() {
        return IMC;
    }

    public void setIMC(double IMC) {
        this.IMC = IMC;
    }
}
