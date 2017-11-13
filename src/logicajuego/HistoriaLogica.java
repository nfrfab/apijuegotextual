/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicajuego;

import dato.ComandoDato;
import dato.HistoriaDato;
import dato.MensajeDato;
import dato.TareaDato;
import dato.TextoComandoDato;
import dato.TextoMensajeDato;
import entidad.Comando;
import entidad.Historia;
import entidad.Mensaje;
import entidad.Tarea;
import logicajuego.TareaLogica;
import entidad.TextoComando;
import entidad.TextoMensaje;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import utils.AdministradorArchivo;

/**
 *
 * @author fabian
 */
public class HistoriaLogica {
    private NarracionLogica narracion;
    
    private Integer idHistoria;
    private Integer idIdioma;
    private String titulo;
    private String descripcion;
    private String codigoMensajeInicio;
    
    public HistoriaLogica() {
        this.narracion = new NarracionLogica();
        idIdioma = 1;
    }
    
    public MensajeLogica getMensaje() {
        return this.narracion.getMensaje();
    }
    
    public TareaLogica obtenerPrimerTareaPendiente() {
        return this.narracion.obtenerPrimerTareaPendiente();
    }
    
    public Boolean procesarTextoComando(String textoComando) {
        return this.narracion.procesarTextoComando(textoComando);
    }
    ////////////////////////////////////////////////
    public Integer insertarHistoriaCompleta() {
        Map<String, MensajeLogica> listaMensajes = this.narracion.getListaMensajes();
        
        Historia historia = new Historia();
        historia.setIdIdioma(idIdioma);
        historia.setTitulo(titulo);
        historia.setDescripcion(descripcion);
        historia.setCodigoMensajeInicio(codigoMensajeInicio);
        HistoriaDato historiaDato = new HistoriaDato();
        this.idHistoria = historiaDato.insertarHistoria(historia);
        
        for (Entry<String, MensajeLogica> mensajeLogicaCompuesto : listaMensajes.entrySet()) {
            MensajeLogica mensajeLogica = mensajeLogicaCompuesto.getValue();
            Mensaje mensaje = new Mensaje();
            mensaje.setIdHistoria(idHistoria);
            mensaje.setCodigoMensaje(mensajeLogica.getCodigoMensaje());
            mensaje.setObligatorio(mensajeLogica.getObligatorio());
            mensaje.setMostrarMensaje(mensajeLogica.getMostrarMensaje());
            mensaje.setMensajePopUp(mensajeLogica.getMensajePopUp());
            mensaje.setCodigoMensajeSiguiente(mensajeLogica.getCodigoMensajeSiguiente());
            
            MensajeDato mensajeDato = new MensajeDato();
            Integer idMensaje = mensajeDato.insertarMensaje(mensaje);
            
            ArrayList<String> listatextoMensaje = mensajeLogica.getListaMensajeTexto();
            for (String texto : listatextoMensaje) {
                TextoMensaje textoMensaje = new TextoMensaje();
                textoMensaje.setIdMensaje(idMensaje);
                textoMensaje.setIdIdioma(idIdioma);
                textoMensaje.setTextoMensaje(texto);
                TextoMensajeDato textoMensajeDato = new TextoMensajeDato();
                textoMensajeDato.insertarTextoMensaje(textoMensaje);
                
            }
            
            ArrayList<ComandoLogica> listaComandos = mensajeLogica.getComandos();
            for (ComandoLogica comandoLogica : listaComandos) {
                Comando comando = new Comando();
                comando.setIdMensaje(idMensaje);
                comando.setCodigoComando(comandoLogica.getCodigoComando());
                comando.setCodigoMensajeSiguiente(comandoLogica.getCodigoMensaje());
                ComandoDato comandoDato = new ComandoDato();
                Integer idComando = comandoDato.insertarComando(comando);
                
                ArrayList<String> listatextoComando = comandoLogica.getListaComandoTexto();
                for (String texto : listatextoComando) {
                    TextoComando textoComando = new TextoComando();
                    textoComando.setIdIdioma(idIdioma);
                    textoComando.setIdComando(idComando);
                    textoComando.setTextoComando(texto);
                    TextoComandoDato textoComandoDato = new TextoComandoDato();
                    textoComandoDato.insertarTextoComando(textoComando);
                    
                }
                
            }
            
            if (mensajeLogica.getObligatorio()) {
                TareaDato tareaDato = new TareaDato();
                Tarea tarea = new Tarea();
                tarea.setIdMensaje(idMensaje);
                tarea.setNumeroTarea(mensajeLogica.getNumeroTarea());
                tarea.setDetalleTarea(mensajeLogica.getDetalleTarea());
                tareaDato.insertarTarea(tarea);
                
            }
        }
        return this.idHistoria;
    }
    
    public void obtenerHistoriaCompletaPorId(Integer idHistoria, Integer idIdioma, String codigoMensajeInicio) {
        this.idIdioma = idIdioma;
        this.narracion = new NarracionLogica();
        
        MensajeDato mensajeDato = new MensajeDato();
        ArrayList<Mensaje> listaMensaje = mensajeDato.obtenerListaMensajesPorIdHistoria(idHistoria);
        for (Mensaje mensaje : listaMensaje) {
            MensajeLogica mensajeLogica = new MensajeLogica();
            mensajeLogica.setCodigoMensaje(mensaje.getCodigoMensaje());
            mensajeLogica.setIndiceTexto(-1);//no es necesario
            mensajeLogica.setObligatorio(mensaje.getObligatorio());
            mensajeLogica.setMostrarMensaje(mensaje.getMostrarMensaje());
            mensajeLogica.setCodigoMensajeSiguiente(mensaje.getCodigoMensajeSiguiente());
            mensajeLogica.setMensajePopUp(mensaje.getMensajePopUp());
            
            TextoMensajeDato textoMensajeDato = new TextoMensajeDato();
            ArrayList<TextoMensaje> listaTextoMensajje = textoMensajeDato.obtenerListaTextoMensajePorIdMensajeIdIdioma(mensaje.getIdMensaje(), idIdioma);
            for (TextoMensaje textoMensaje : listaTextoMensajje) {
                mensajeLogica.agregarMensajeTexto(textoMensaje.getTextoMensaje());
            }
            
            boolean contieneComando = false;
            ComandoDato comandoDato = new ComandoDato();
            ArrayList<Comando> listaComandos = comandoDato.obtenerListaComandosDeIdMensaje(mensaje.getIdMensaje());
            for (Comando comando : listaComandos) {
                contieneComando = true;
                
                ComandoLogica comandoLogica = new ComandoLogica();
                comandoLogica.setCodigoComando(comando.getCodigoComando());
                comandoLogica.setCodigoMensaje(comando.getCodigoMensajeSiguiente());
                
                TextoComandoDato textoComandoDato = new TextoComandoDato();
                ArrayList<TextoComando> listaTextoComando = textoComandoDato.obtenerListaTextoComando(comando.getIdComando(), idIdioma);
                for (TextoComando textoComando : listaTextoComando) {
                    comandoLogica.agregarTextoComando(textoComando.getTextoComando());
                }
                
                mensajeLogica.agregarComando(comandoLogica);
            }
            mensajeLogica.setProcesarRespuesta(contieneComando);
            
            //hay que modificar la estructura y logica de las tareas...
            TareaDato tareaDato = new TareaDato();
            ArrayList<Tarea> listaTareas = tareaDato.obtenerListaTareaPorIdMensaje(mensaje.getIdMensaje());
            for (Tarea tarea : listaTareas) {
                
                mensajeLogica.setObligatorio(true);
                mensajeLogica.setNumeroTarea(tarea.getNumeroTarea());
                mensajeLogica.setDetalleTarea(tarea.getDetalleTarea());
                mensajeLogica.setMensajeMostrado(false);
            }
            
            this.narracion.agregarMensaje(mensajeLogica);
        }
        this.narracion.setProximoMensajeDeNarracion(codigoMensajeInicio);
    }
    
    public ArrayList<Historia> obtenerListaHistorias() {
        ArrayList<Historia> listaHistoria = new ArrayList<>();
        return listaHistoria;
    }
    
    public EstadoScript cargarHistoriaDesceScript(String archivo, Integer idIdioma, String titulo, String descripcion) {
        EstadoScript estadoScript = new EstadoScript();
        NarracionLogica narracionLogica = new NarracionLogica();
        Boolean scriptValido = false;
        Boolean contieneInicioNarracion = false;
        if (AdministradorArchivo.archivoExiste(archivo)) {
            ArrayList<String> lineas = AdministradorArchivo.leerlineas(archivo);
            for(int i=0; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                //String[] parametros = linea.split("\t");
                String[] parametros = linea.split("\", \"");
                String comando = parametros[0];
                
                switch (comando) {
                    case "agregarMensaje":
                        int cantidadParametros = parametros.length;
                        switch (cantidadParametros) {
                            case 3:
                                narracionLogica.agregarMensaje(parametros[1], parametros[2]);
                                scriptValido = true;
                                break;
                            case 4:
                                narracionLogica.agregarMensaje(parametros[1], parametros[2], parametros[3]);
                                scriptValido = true;
                                break;
                            default:
                                estadoScript.setDetalle("Cantidad de parametros no valido");
                                estadoScript.setLineaInvalida(linea);
                                scriptValido = false;
                        }
                        break;
                    case "agregarTarea":
                        if (parametros.length == 4) {
                            narracionLogica.agregarTarea(parametros[1], Integer.valueOf(parametros[2]), parametros[3]);
                            scriptValido = true;
                        } else {
                            estadoScript.setDetalle("Cantidad de parametros no valido");
                            estadoScript.setLineaInvalida(linea);
                            scriptValido = false;
                        }
                        break;
                    case "agregarMensajePopUp":
                        if (parametros.length == 3) {
                            narracionLogica.agregarMensajePopUp(parametros[1], parametros[2]);
                            scriptValido = true;
                        } else {
                            estadoScript.setDetalle("Cantidad de parametros no valido");
                            estadoScript.setLineaInvalida(linea);
                            scriptValido = false;
                        }
                        break;
                    case "agregarComandoAMensaje":
                        if (parametros.length == 5) {
                            narracionLogica.agregarComandoAMensaje(parametros[1], parametros[2], parametros[3], parametros[4]);
                            scriptValido = true;
                        } else {
                            estadoScript.setDetalle("Cantidad de parametros no valido");
                            estadoScript.setLineaInvalida(linea);
                            scriptValido = false;
                        }
                        break;
                    case "setProximoMensajeDeMensajeEnLista":
                        if (parametros.length == 3) {
                            narracionLogica.setProximoMensajeDeMensajeEnLista(parametros[1], parametros[2]);
                            scriptValido = true;
                        }
                        break;
                    case "setProximoMensajeDeNarracion":
                        if (parametros.length == 2) {
                            narracionLogica.setProximoMensajeDeNarracion(parametros[1]);
                            contieneInicioNarracion = true;
                            scriptValido = true;
                        } else {
                            estadoScript.setDetalle("Cantidad de parametros no valido");
                            estadoScript.setLineaInvalida(linea);
                            scriptValido = false;
                        }
                        break;
                    default:
                        estadoScript.setDetalle("no se reconoce el comando");
                        estadoScript.setLineaInvalida(linea);
                        scriptValido = false;
                        break;
                        
                }
                if (!scriptValido) {
                    break;
                }
                    
            }
        }
        if (scriptValido) {
            if (contieneInicioNarracion) {
                this.narracion = narracionLogica;
                this.codigoMensajeInicio = this.narracion.getCodigoMensajeProximo();
                this.idIdioma = idIdioma;
                this.titulo = titulo;
                this.descripcion = descripcion;
                estadoScript.setScriptValido(true);
            } else {
                estadoScript.setDetalle("Script incompleto. No se encontro inicio de la historia...");
                estadoScript.setLineaInvalida("-");
                estadoScript.setScriptValido(false);
            }
            
        }
        return estadoScript;
    }
    ////////////
    
    public void cargarHistoriaPorDefecto() {
        this.narracion = new NarracionLogica();
        this.narracion.agregarMensaje("men1", "El interior del bar estaba un poco oscuro...");
        this.narracion.agregarMensaje("men2", "...A pesar de la oscuridad se puede observar una barra hacia el norte y una puerta con un cartel que dice Baño hacia el este. Que queres hacer? Podes caminar hacia el norte o el sur", "men1");
        this.narracion.agregarMensaje("men3", "En la barra hay una copa y dos botellas, una de agua y otra de cerveza. Que queres hacer? Podes beber algo o regresar a la entrada...");
        this.narracion.agregarMensaje("men4", "El baño es un verdadero asco..., nadie en su sano juicio lo utilizaria para hacer sus necesidades pero parece que tambien servia como deposito porque se ve en una esquina varias cajas con papeles. Que queres hacer? Podes caminar hacia el norte para revisar los papeles o regresar a la entrada...");
        this.narracion.agregarMensaje("men5", "El agua no esta fria pero te calma un poco la sed pero tambien te dan ganas de ir al baño...Te vas al Baño.");//falta setear cual es el mensaje siguiente...
        this.narracion.agregarMensaje("men6", "Bebes un poco de cerbeza pero como esta caliente te da asco y te dan ganas de vomitar...Te vas al Baño para vomitar.");//falta setear cual es el mensaje siguiente...
        this.narracion.agregarMensaje("acercarseNorteParaLeerPapeles", "Caminas hacia el norte...tu intuicion te dice que vas a encontrar alguna prueba. Que queres hacer? Podes leer los papeles o regresar a la entrada del Baño");
        this.narracion.agregarMensaje("lecturaPapeles", "Empiezas a leer los papeles...Despues de 15min encuentras una nota que dice: vecinoxxx queres robarte mis propiedades...");
        this.narracion.agregarTarea("lecturaPapeles", 1, "Encontrar nota con claves para traducir el lenguaje xxxx");
        this.narracion.agregarMensaje("conclusionLecturaPapeles", "Despues de leer esa nota llegas a la conclusion de que el asesino es xxxx. Solo te falta buscar las pruebas...", "lecturaPapeles");
        this.narracion.agregarMensaje("salirBarConSospechozo", "Encontraste un sospechoso, ahora a buscar las pruebas....", "conclusionLecturaPapeles");
        
        
        this.narracion.agregarMensajePopUp("descripcionBarra", "La barra esta muy gastada....");
        this.narracion.agregarMensajePopUp("descripcionCopa", "La copa esta llena de polvo...");
        

        //se agregan comandos a los mensajes para que el jugador pueda decidir que es lo que quiere hacer...
        this.narracion.agregarComandoAMensaje("men2", "caminarNorte", "caminar norte", "men3");
        this.narracion.agregarComandoAMensaje("men2", "caminarEste", "caminar este", "men4");

        this.narracion.agregarComandoAMensaje("men3", "beberAgua", "beber agua", "men5");
        this.narracion.agregarComandoAMensaje("men3", "beberCerbeza", "beber cerbeza", "men6");  
        this.narracion.agregarComandoAMensaje("men3", "CmdDescripcionBarra", "describir barra", "descripcionBarra");
        this.narracion.agregarComandoAMensaje("men3", "CmdDescripcionCopa", "describir copa", "descripcionCopa");
        
        this.narracion.setProximoMensajeDeMensajeEnLista("men5", "men4");
        this.narracion.setProximoMensajeDeMensajeEnLista("men6", "men4");      

        this.narracion.agregarComandoAMensaje("men4", "CmdAcercarseNorteParaLeerPapeles", "caminar norte", "acercarseNorteParaLeerPapeles");
        
        this.narracion.agregarComandoAMensaje("acercarseNorteParaLeerPapeles", "CmdLecturaPapeles", "leer papeles", "lecturaPapeles");
        //
        //SE CARGA EL RESTO DE LA HISTORIA JUNTO CON LA LOGICA...
        this.narracion.agregarMensaje("intro_manejasAuto", "Manejas tu viejo Renault 9 color azul noche, como siempre con la radio apagada, nada se iguala a sentir el viento pasando por la ventana. El sonido del motor, el caucho girando sobre el asfalto y manejar en camino de piedra es uufff, simplemente sublime.");
        this.narracion.agregarObjetoXAMensaje("intro_manejasAuto", "candadoValor1", "valor candado 1", true, null, null);
        this.narracion.agregarMensaje("intro_manejasAut2", "Mañana cumples seis años de servicio. Acaricias el volante, tiene un forro de cuero, ya gastado, mirás el torpedo", "intro_manejasAuto");
        this.narracion.agregarMensaje("intro_manejasAut3", "También cumplimos seis años juntos", "intro_manejasAut2");
        this.narracion.agregarMensaje("intro_manejasAut4", "Aunque cuando se conocieron él lucía un bordo gastado y sin brillo, el motor sonaba parejo pero muy fuerte debido al mal estado del escape. Con el tiempo y los viajes, obtuvo nuevas llantas, con estilo.", "intro_manejasAut3");
        this.narracion.agregarMensaje("intro_manejasAut5", "Pasaron por varios colores hasta encontrar el hermoso azul noche. A los dos les queda muy bien el azul.", "intro_manejasAut4");
        this.narracion.agregarMensaje("intro_manejasAut6", "Hace un buen tiempo que manejas por una ruta algo olvidada, con más pozos que carteles.", "intro_manejasAut5");
        this.narracion.agregarMensaje("intro_manejasAut7", "Pasaron muchos años desde que no visitaste estos parajes.", "intro_manejasAut6");
        this.narracion.agregarMensaje("intro_manejasAut8", "Ya no recordas bien el camino y se aproxima un cruce, el tema es...", "intro_manejasAut7");
        this.narracion.agregarMensaje("intro_manejasAut9", "¿había con salirse de la ruta en este cruce o en el próximo?", "intro_manejasAut8");
        this.narracion.agregarMensaje("intro_manejasAut10", "¿había que ir a la izquierda o a la derecha?", "intro_manejasAut9");
        this.narracion.agregarMensaje("intro_manejasAut11", "¿Qué hacemos amigo, para donde agarramos?- Le preguntas al viejo Renault 9 azul noche. Es una vieja costumbre que practicas desde que se conocieron.", "intro_manejasAut10");
        this.narracion.agregarMensaje("intro_manejasAut11b", "Podes DOBLAR IZQUIERDA, DOBLAR DERECHA o SEGUIR ADELANTE", "intro_manejasAut11");
        
        
        this.narracion.agregarMensaje("intro_manejasAut12", "Es un camino de tierra, sorprende el contraste entre su buen estado y lo crecidos que están los matorrales.");
        this.narracion.agregarMensaje("intro_manejasAut13", "El camino da una inmensa vuelta y te deja en el un lago....", "intro_manejasAut12");
        this.narracion.agregarMensaje("intro_manejasAut14", "...Te acuerdas de tus tiempos de adolescencia, todos los buenos momentos que pasaste en ese lago...Medidas un poco y finalmente vuelves por donde viniste llegando de nuevo al croce.", "intro_manejasAut13");
        this.narracion.agregarMensaje("intro_manejasAut15", "Que hacemos esta vez? Le vuelves a preguntar a tu viejo auto", "intro_manejasAut14");
        this.narracion.agregarMensaje("intro_manejasAut16", "Podes SEGUIR ADELANTE o DOBLAR IZQUIERDA", "intro_manejasAut15");
        
        this.narracion.agregarMensaje("intro_manejasAut17", "Después de más de una hora de viaje llegas a un pueblo, es evidente que te pasaste el cruce donde debías salir de la ruta. Igualmente paras en un café al costado de la ruta.");
        this.narracion.agregarMensaje("intro_manejasAut18", "Que queres hacer? Podes VOLVER AL CRUCE, o podes ENTRAR AL CAFE, te vendria bien estirar un poco las piernas y tal vez consigas una buena indicación.", "intro_manejasAut17");
        
        this.narracion.agregarMensaje("intro_entrarCafe", "Entras, es un lugar con decoración algo vieja pero agradable, te sientas en la barra. La camarera te saluda con amabilidad, su sonrisa es pura energía.");
        this.narracion.agregarMensaje("intro_entrarCafe2", "Le pides a la camarera un café bien cargado, te sonríe otra vez y enseguida te sirve de la jarra. Le ponés dos de azúcar, tras un par de sorbos te preguntas, la camarera ¿podrá darme alguna indicación sobre el cruce que busco?", "intro_entrarCafe");
        this.narracion.agregarMensaje("intro_entrarCafe3", "Piensas un poco, podes PEDIR INDICACION sobre el cruce o solo PAGAR e irte", "intro_entrarCafe2");
        
        
        this.narracion.agregarMensaje("intro_pedirInformacion", "Tomás otro poco de café y le preguntas si conoce el cruce que buscas.");
        this.narracion.agregarMensaje("intro_pedirInformacion2", "Se le va la sonrisa de la cara", "intro_pedirInformacion");
        this.narracion.agregarMensaje("intro_pedirInformacion3", "-ah, ese lugar buscabas, debí imaginarlo- Frunce el ceño- Está más atrás, te pasaste varios kilómetros¿ no lo viste?-", "intro_pedirInformacion2");
        this.narracion.agregarMensaje("intro_pedirInformacion4", "-No la verdad que no- Le respondes intrigado por el gesto de la camarera", "intro_pedirInformacion3");
        this.narracion.agregarMensaje("intro_pedirInformacion5", "-Si vas para alla te aconsejo que estés más despierto y prestes más atención. No es de mi incumbencia pero¿ a qué va?-", "intro_pedirInformacion4");
        this.narracion.agregarMensaje("intro_pedirInformacion6", "Le podrias RESPONDER para ver que te dice, aunque tampoco es mala idea AGRADECER e irte.", "intro_pedirInformacion5");
        
        this.narracion.agregarMensaje("intro_pedirInformacionAgradecer", "Solo voy de visita - Le decis mientras haces tiempo esperando una respuesta.");
        this.narracion.agregarMensaje("intro_pedirInformacionAgradecer2", "- No sé a quién irá a ver, hace rato que no se sabe nada de alla. Pero insisto en sea precavido, la gente comenta solo cosas raras de aquel lugar- Señala hacia afuera con la vista levantando las cejas.", "intro_pedirInformacionAgradecer");
        this.narracion.agregarMensaje("intro_pedirInformacionAgradecer3", "Pagas, le das las gracias y volver a tu viejo Renault 9 azul noche.", "intro_pedirInformacionAgradecer2");
        
        this.narracion.agregarMensaje("intro_pedirInformacionResponder", "- Estoy buscando a un familiar que hace mucho tiempo que no se comunica conmigo - Le decis mientras haces tiempo esperando una respuesta");
        this.narracion.agregarMensaje("intro_pedirInformacionResponder2", "- Hace rato que no se sabe nada de alla. Pero insisto en sea precavido, la gente comenta solo cosas raras de aquel lugar- Señala hacia afuera con la vista levantando las cejas.", "intro_pedirInformacionResponder");
        
        //para el comando PAGAR solo se debe de setear el proximo mensaje...
        
        this.narracion.agregarMensaje("intro_manejasAut19", "Volves por el mismo camino por el que llegaste, después de un buen viaje llegas al cruce.");
        this.narracion.agregarMensaje("intro_manejasAut20", "Que queres hacer? Podes DOBLAR IZQUIERDA o DOBLAR DERECHA", "intro_manejasAut19");
        
        this.narracion.setProximoMensajeDeMensajeEnLista("intro_pedirInformacionResponder2", "intro_pedirInformacionAgradecer3");
        this.narracion.setProximoMensajeDeMensajeEnLista("intro_pedirInformacionAgradecer3", "intro_manejasAut19");
        
        this.narracion.agregarMensaje("intro_manejasCaminoPueblo", "Es un camino de grava. Pensas es el camino ideal, es una buena señal nada malo puede venir de un camino de piedras.");
        this.narracion.agregarMensaje("intro_manejasCaminoPueblo2", "-O no?- Le preguntas en voz alta a tu compañero, el viejo Renault 9 azul marino, mirando el reloj que indica que tenes medio tanque de nafta.", "intro_manejasCaminoPueblo");
        this.narracion.agregarMensaje("intro_manejasCaminoPueblo3", "El camino de grava es largo, no da indicios de llevar a algún lado, no hay carteles ni marcas en el camino que puedan indicar que alguien paso por alli. Al menos recientemente.", "intro_manejasCaminoPueblo2");
        this.narracion.agregarMensaje("intro_manejasCaminoPueblo4", "Tampoco hay cercos a los lados del camino, solo tierra, pasto y algunas yerbas malas decoran el paisaje.", "intro_manejasCaminoPueblo3");
        this.narracion.agregarMensaje("intro_manejasCaminoPueblo5", "Con varios kilómetros de aquel camino tras la espalda llegas a un cartel, es chico, de madera. Esta colgando de uno de sus lados, desde el auto podes leer:", "intro_manejasCaminoPueblo4");
        this.narracion.agregarMensaje("intro_manejasCaminoPueblo6", "Bienvenido a L...", "intro_manejasCaminoPueblo5");
        this.narracion.agregarMensaje("intro_manejasCaminoPueblo7", "Falta un pedazo pero no está a la vista. Deseas bajar del auto y buscar un poco más? Podes responder SI o NO", "intro_manejasCaminoPueblo6");
        
        
        this.narracion.agregarMensaje("intro_manejasCaminoPueblo8", "Buscas un poco pero no hay muchos lugares donde mirar y terminas sin encontrar nada. subis al auto.");
        
        this.narracion.agregarMensaje("intro_manejasCaminoPueblo9", "Seguis camino, del lado izquierdo hay una casa, al menos eso parece ya que está bastante lejos.");
        this.narracion.agregarMensaje("intro_manejasCaminoPueblo10", "Desde el viejo Renault 9 azul noche, se puede ver que la casa está casi tapada por el agua.", "intro_manejasCaminoPueblo9");
        this.narracion.agregarMensaje("intro_manejasCaminoPueblo11", "Más adelante se ven varias edificaciones, podría ser todo un pueblo.", "intro_manejasCaminoPueblo10");
        this.narracion.agregarMensaje("intro_manejasCaminoPueblo12", "Mejor dicho... Podría ser el pueblo que abandonaste hace tantos años?", "intro_manejasCaminoPueblo11");
        this.narracion.agregarMensaje("intro_manejasCaminoPueblo13", "Por fortuna el camino esta bastante mas alto que el nivel del agua.", "intro_manejasCaminoPueblo12");
        this.narracion.agregarMensaje("intro_manejasCaminoPueblo14", "Casi dos kilómetros después termina el camino de piedras en lo que bien podría ser la calle central del pueblo.", "intro_manejasCaminoPueblo13");
        this.narracion.agregarMensaje("intro_callePueblo", "La calle está asfaltada, muy húmeda, hay charcos de distintos tamaños dispersos por todos lados.", "intro_manejasCaminoPueblo14");
        this.narracion.agregarMensaje("intro_callePueblo2", "Desde aca podes ver algunas de las casas y el bar. El resto parece estar inundado.", "intro_callePueblo");
        this.narracion.agregarMensaje("intro_callePueblo3", "El pueblo, tu pueblo cambió demasiado. Cuando lo dejaste era un lugar alegre, donde todos trabajaban mucho y colaboraban con cualquiera que necesitara una mano. Siempre estaban dispuestos a hacer cualquier cosa que beneficiara al pueblo y a sus habitantes.", "intro_callePueblo2");
        this.narracion.agregarMensaje("intro_callePueblo4", "Pero eso fue hace mucho tiempo.", "intro_callePueblo3");
        this.narracion.agregarMensaje("intro_callePueblo5", "A primera vista parece no haber nadie. No escuchas ruido alguno. No sale música del bar como sucedió siempre. Ni los pájaros cantan.", "intro_callePueblo4");
        this.narracion.agregarMensaje("intro_callePueblo6", "A tu izquierda hay una casa, a la derecha hay otra casa y frente a vos la calle principal.", "intro_callePueblo5");
        this.narracion.agregarMensaje("intro_callePueblo7", "qué deseas hacer? Podes CAMINAR IZQUIERDA, CAMINAR DERECHA, CAMINAR ADELANTE o ENTRAR AL BAR", "intro_callePueblo6");
       
                // mensajes de casa1 y 2 y recuerdo1 es lo nuevo
            
        this.narracion.agregarMensaje("casa1a", "Entreas a la casa a tu izquierda. Se ve vieja, humeda y descuidada. Buscas por todos lados pero  lo único que llama tu atención es un viejo cuaderno. Es un diario íntimo. Pertenece a Doña Aurora." );
        this.narracion.agregarMensaje("casa1b", "Cuando dejaste el pueblo ya era una señora grande, algo desequilibrada. Tenía por costumbre regalar higos pero para que fueran gratis debias pedirlo en la lengua que hablaba su abuelo.", "casa1a");
        this.narracion.agregarMensaje("casa1c", " Ya no te acordas que idioma hablaba aquel hombre, que claro está usabas para pedir higos gratis todos los días. Doña Aurora te escuchaba y gustosa te regalaba dos o tres porque lo hablabas muy bien.", "casa1b");
        this.narracion.agregarMensaje("casa1d", "El diario íntimo tiene las hojas del medio arrancadas. Las primeras páginas hablan de cosas sin importancia. Y las ultimas estan llenas de garabatos. Da la impresión que Doña Aurora se terminó de volver loca. O no? Acaso podría ser una pista escondida?.","casa1c" );
        this.narracion.agregarMensaje("casa1e", "La vieja se hizo pasar por loca y escribió todo en ese idioma casi muerto?\n" + "Miras de nuevo el diario, y siguen pareciendo garabatos. Aunque algunos dibujos podrían ser letras o Numeros.", "casa1d");        
        this.narracion.agregarMensaje("casa1f","Mirando el diario con mayor atencion vez, debajo de todos los garabatos, un garabato mucho mas grande. Lo reconoces es el equivalente al numero 3. Es otra cosa para averiguar." , "casa1e" );        
        this.narracion.agregarMensaje("casa1g", "salis de la casa de Aurora. La verdad todo se ve igual, avandonado, absolutamente todo esta humedo, icluso con moho.", "casa1f");
        this.narracion.agregarMensaje("casa1h", "Donde ir ahora? una buena pregunta. Podes CAMINAR ADELANTE y entrar a la otra casa, DOBLAR IZQUIERDA e ir a la calle princial", "casa1g");

        this.narracion.agregarMensaje("casa2", "Entras a la casa. Esta todo desordenado, cubierto de polvo. Hay un olor muy fuerte, es una pésima señal, una vez que se huele ese hedor nunca más se olvida.", "casa1h");
        this.narracion.agregarMensaje("casa2a", "\n En el living hay un sillón de tres cuerpos que parece no estar en el sitio que le corresponde. Detras del sillon parece haber un vulto.", "casa2");
        this.narracion.agregarMensaje("casa2b", "Te acercas un poco y enseguida te das cuenta de que es un cuerpo.\n" + "Te acercas más y es notorio que esa persona lleva bastante tiempo muerta. Lo reconoces fácilmente. Es Don Mateo.", "casa2a");
        this.narracion.agregarMensaje("casa2c", "En la mitad de la espalda hay una gran mancha de sangre. Fue atacado por la espalda. con un cuchillo grande o bien un machete.","casa2b" );
        this.narracion.agregarMensaje("casa2d", "El pecho se te comprime, casi de golpe. Don Mateo era un Hombre con mucha historia, era dueño de un inmenso y muy particular humor serio, como él solía decir. ", "casa2c");
        this.narracion.agregarMensaje("casa2e", "El arma que se llevó su vida no está. Al menos en esa casa, eso es obvio. \n" + "La sangre en el suelo se secó hace rato ya. Solo queda la marca en el suelo de madera. ", "casa2d");
        this.narracion.agregarMensaje("casa2f", "El cadáver tiene su mano derecha en un bolsillo. ¿Queres sacarle la mano del bolsillo? SI o NO ", "casa2e");
        this.narracion.agregarMensaje("casa2g", "Sacas la mano del cadáver del bolsillo. Aun agarra con fuerza unos papeles. Los sacas de la mano. \n" + "Es un boleto de compraventa, está roto en dos pedazos.", "casa2f");
        this.narracion.agregarMensaje("casa2h","Lo revisas un rato, una empresa quería comprarle la casa a Don Mateo, “Aceites del Sud SA”.\n" + "El nombre de la empresa no te suena para nada. Pero te preguntas ¿Porque  dejaron el cuerpo ahí?¿porque no lo enterraron? ", "casa2g");
        this.narracion.agregarMensaje("casa2i", "¿Quién lo mató?¿Tanta impunidad tiene que es capaz de dejar el cuerpo ahí tirado, cuando es notorio que se trata de un asesinato?", "casa2h"); 
        this.narracion.agregarMensaje("casa2j", "Revisas un poco más la casa pero no encontraba nada que de algún indicio de lo que sucedió.\n" + "Salís de la casa con una mezcla de sentimientos, tristeza, bronca, indignación pero por sobre todas ellas, el deseo. El deseo de agarrar al culpable y hacerlo pagar.","casa2i");
        this.narracion.agregarMensaje("casa2k", "Te preguntas ¿acaso voy a encontrar a todo el pueblo muerto en sus casas? ", "casa2j");
        
        this.narracion.agregarMensaje("recuerdo1","Un escalofrío recorre todo tu cuerpo. De inmediato pensas en tus papás y en Enrique, tu hermano. La última vez que lo viste solo tenía dos años y vos 17.", "casa2k");        
        this.narracion.agregarMensaje("recuerdo2", "Tus padres se venían venir la charla. \n " + "Papá, mamá- les dirías mientras ellos te mirarían espectantes pero sabiendo muy bien cuáles serían tus próximas palabras. \n" + " Quiero estudiar en la capital- harías una pausa. \n " + " Para volver como un gran policía y de esa forma ayudar al pueblo-", "recuerdo1");
        this.narracion.agregarMensaje("recuerdo3", "Ellos llorarían y así lo hicieron. Te abrazarían como si fuese la última vez que te verían y así lo hicieron. \n "+" Una pregunta se te clava en el pecho, bien adentro.", "recuerdo2");
        this.narracion.agregarMensaje("recuerdo4", "¿será esa, aquella, la última vez que nos  viéramos? \n" + "¿será aquel abrazo el último ? \n" + "Caes de rodillas al piso. \n" + "No - te decís casi gritando.", "recuerdo3");
        this.narracion.agregarMensaje("recuerdo5", "Esa no será la última vez, no puede, no lo vas a permitir. Vas a hacer todo lo posible  para evitarlo. \n" + " Con mucho esfuerzo te levantas, respiras hondo y salis a la calle", "recuerdo4");
              
        this.narracion.agregarMensaje("callePrincipal", " Es la calle principal del pueblo, hay plantas que crecen desparramadas en la calle, la verda, hay grandes charcos, papeles y basura por todos lados.", "recuerdo5");
        this.narracion.agregarMensaje("callePrincipal1", "A unos metros hay 1 poste de luz con un cartel pegado. Te acercas para ver lo que dice. Esta viejo con algunas letras gastadas.", "callePrincipal1");
        this.narracion.agregarMensaje("callePrincipal2","Es un pedazo de plastico que dice: \n" + " Venda su propiedad a TIEMPO, no espere a que se desvalorice \n" + "\n" + "Mas abajo hay un logo pero esta gastado y no llegas a entenderlo"  ,"callePrincipal1");
                       
        ////////////////////////////////////////////////////////////
        //SE AGREGAN LOS COMANDOS...
        this.narracion.agregarComandoAMensaje("intro_manejasAut11b", "doblarIzquierda", "DOBLAR IZQUIERDA", "intro_manejasAut12");
        this.narracion.agregarComandoAMensaje("intro_manejasAut11b", "doblarDerecha", "DOBLAR DERECHA", "intro_manejasCaminoPueblo");
        this.narracion.agregarComandoAMensaje("intro_manejasAut11b", "seguirAdelante", "SEGUIR ADELANTE", "intro_manejasAut17");
        
        this.narracion.agregarComandoAMensaje("intro_manejasAut16", "doblarIzquierda", "DOBLAR IZQUIERDA", "intro_manejasAut17");
        this.narracion.agregarComandoAMensaje("intro_manejasAut16", "seguirAdelante", "SEGUIR ADELANTE", "intro_manejasCaminoPueblo");
        
        this.narracion.agregarComandoAMensaje("intro_manejasAut18", "volverCruce", "VOLVER AL CRUCE", "intro_manejasAut19");
        this.narracion.agregarComandoAMensaje("intro_manejasAut18", "entrarCafe", "ENTRAR AL CAFE", "intro_entrarCafe");
        
        this.narracion.agregarComandoAMensaje("intro_entrarCafe3", "pedirIndicacion", "PEDIR INDICACION", "intro_pedirInformacion");
        this.narracion.agregarComandoAMensaje("intro_entrarCafe3", "pagar", "PAGAR", "intro_pedirInformacionAgradecer3");
        
        this.narracion.agregarComandoAMensaje("intro_pedirInformacion6", "responder", "RESPONDER", "intro_pedirInformacionResponder");
        this.narracion.agregarComandoAMensaje("intro_pedirInformacion6", "agradecer", "AGRADECER", "intro_pedirInformacionAgradecer");
        
        this.narracion.agregarComandoAMensaje("intro_manejasAut20", "doblarIzquierda", "DOBLAR IZQUIERDA", "intro_manejasCaminoPueblo");
        this.narracion.agregarComandoAMensaje("intro_manejasAut20", "doblarDerecha", "DOBLAR DERECHA", "intro_manejasAut12");
        
        this.narracion.agregarComandoAMensaje("intro_manejasCaminoPueblo7", "si", "SI", "intro_manejasCaminoPueblo8");
        this.narracion.agregarComandoAMensaje("intro_manejasCaminoPueblo7", "no", "NO", "intro_manejasCaminoPueblo9");
        
        this.narracion.setProximoMensajeDeMensajeEnLista("intro_manejasCaminoPueblo8", "intro_manejasCaminoPueblo9");
        
        
        this.narracion.agregarComandoAMensaje("intro_callePueblo7", "casa1", "CAMINAR IZQUIERDA", "casa1");
       
         // estos 3 comandos hay que revisarlo no se si estara bien.
        this.narracion.agregarComandoAMensaje("casa1h", "seguirAdelante", "CCAMINAR ADELANTE","casa2");
        this.narracion.agregarComandoAMensaje("casa2f", "si", "SI", "casa2g");
        this.narracion.agregarComandoAMensaje("casa2f", "no", "no", "casa2i");
        
        
        
        this.narracion.agregarComandoAMensaje("intro_callePueblo7", "doblarDerecha", "CAMINAR DERECHA", "cass2");
        this.narracion.agregarComandoAMensaje("intro_callePueblo7", "seguirAdelante", "CAMINAR ADELANTE", "callePrincipalSector1");
        this.narracion.agregarComandoAMensaje("intro_callePueblo7", "caminarAlBar", "ENTRAR AL BAR", "men1");
       
                  
        //**************
        
        
        //para prueba de disparadoes
        this.narracion.agregarMensaje("intro_manejasAut3Alternativo", "se conocieron cuando el recien habia salido de la fabrica...");
        this.narracion.setProximoMensajeDeMensajeEnLista("intro_manejasAut3Alternativo", "intro_manejasAut4");
        this.narracion.agregarDisparadorA_Mensaje_MensajeSiguienteDeMensaje("intro_manejasAuto", "intro_manejasAut2", "intro_manejasAut3Alternativo");
        //this.narracion.agregarDisparadorA_Mensaje_setEstadoDeComando("men1", "men3", "CmdDescripcionCopa", false);
        this.narracion.agregarDisparadorA_Mensaje_MensajeSiguienteDeComando("men1", "men3", "CmdDescripcionBarra", "intro_manejasAut12");
        
        
        this.narracion.agregarDisparadorA_Comando_setEstadoDeComando("men3", "CmdDescripcionCopa", "men3", "CmdDescripcionCopa", false);
        this.narracion.agregarDisparadorA_Comando_MensajeSiguienteDeComando("men3", "CmdDescripcionCopa", "men3", "beberAgua", "intro_manejasAuto");
        this.narracion.agregarDisparadorA_Comando_MensajeSiguienteDeMensaje("men3", "CmdDescripcionCopa", "intro_manejasAut2", "intro_manejasAut6");
        
        //...
        //se establece cual es el principio de la historia...HAY QUE ANALIZAR ESTA LOGICA
        this.narracion.setProximoMensajeDeNarracion("men1");//men1 //intro_manejasAuto
        
        this.codigoMensajeInicio = "intro_manejasAuto";
        this.idIdioma = 1;
        this.titulo = "primer historia...";
        this.descripcion = "un hombre encuentra el pueblo donde nacio abandonado...";
        //
    }
    
    
}
