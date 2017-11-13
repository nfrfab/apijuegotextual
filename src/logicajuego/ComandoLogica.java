package logicajuego;

import java.awt.List;
import java.util.ArrayList;


public class ComandoLogica {
    //private String comando;
    private String codigoComando;///
    private String codigoMensaje;
    //private ArrayList<String> listaComandoTexto;
    private ArrayList<String> listaComandoTexto;//se utilizara List porque Hibernate utiliza las listas

    private Boolean activo;
    private ArrayList<Desencadenador> listaDesencadenador;

    public ComandoLogica() {
        listaComandoTexto = new ArrayList<>();
        
        activo = true;
        this.listaDesencadenador = new ArrayList<>();
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public ArrayList<Desencadenador> getListaDesencadenador() {
        return listaDesencadenador;
    }

    public void setListaDesencadenador(ArrayList<Desencadenador> listaDesencadenador) {
        this.listaDesencadenador = listaDesencadenador;
    }
    
    
    
    
    public void agregarDesencadenador(Desencadenador desencadenador) {
        listaDesencadenador.add(desencadenador);
    }

    public ArrayList<String> getListaComandoTexto() {
        return listaComandoTexto;
    }

    public ComandoLogica( String codigoComando, String comando, String codigoMensaje) {
        //this.comando = comando;//
        this.codigoComando = codigoComando;
        this.codigoMensaje = codigoMensaje;
        listaComandoTexto = new ArrayList<>();
        listaComandoTexto.add(comando);
        
        this.listaDesencadenador = new ArrayList<>();
        this.activo = true;
    }

    /*
    public String getComando() {
            return this.comando;
    }
    */

    public String getCodigoMensaje() {
            return this.codigoMensaje;
    }

    public void setCodigoComando(String codigoComando) {
        this.codigoComando = codigoComando;
    }

    public String getCodigoComando() {
        return codigoComando;
    }
    
    

    public void setCodigoMensaje(String codigoMensaje) {
        this.codigoMensaje = codigoMensaje;
    }
        
        

    public void agregarTextoComando(String textoComando) {
            listaComandoTexto.add(textoComando);
    }
    
    

    public boolean comandoValido(String textoComando) {
        boolean valido = false;
        /*
        int cantidadComandos = listaComandoTexto.size();
        for(int i = 0; i < cantidadComandos; i++) {
                String auxTextoComando = listaComandoTexto.get(i);
                if (auxTextoComando.equals(textoComando)) {
                        valido = true;
                        break;
                }
        }
        */
        
        if (activo) {
            for (String auxTextoComando : listaComandoTexto) {
            
                if (auxTextoComando.equalsIgnoreCase(textoComando)) {
                    valido = true;
                    break;
                }
            }
        }
        
        return valido;	

    }	
        
}