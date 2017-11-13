/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicajuego;

/**
 *
 * @author fabian
 */
public class Desencadenador {
    private String tipo;//seria mejor si se utiliza un enum
    //TIPOS
    //mensaje_setMensajeSiguiente
    //comando_modificarEstado
    //comando_setMensajeSiguiente
    
    private String codigoMensaje;
    private String codigoComando;
    private String codigoMensajeSiguiente;
    private Boolean estado;

    public Desencadenador() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodigoMensaje() {
        return codigoMensaje;
    }

    public void setCodigoMensaje(String codigoMensaje) {
        this.codigoMensaje = codigoMensaje;
    }

    public String getCodigoComando() {
        return codigoComando;
    }

    public void setCodigoComando(String codigoComando) {
        this.codigoComando = codigoComando;
    }

    public String getCodigoMensajeSiguiente() {
        return codigoMensajeSiguiente;
    }

    public void setCodigoMensajeSiguiente(String codigoMensajeSiguiente) {
        this.codigoMensajeSiguiente = codigoMensajeSiguiente;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
    
    
    
}
