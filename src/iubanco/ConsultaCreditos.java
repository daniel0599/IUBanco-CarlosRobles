package iubanco;
// ConsultaCreditos.java
// Este programa lee un archivo en forma secuencial y muestra su contenido en un 
// �rea de texto, con base en el tipo de cuenta que el usuario solicita 
// (saldo con credito, saldo con debito o saldo en ceros).
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;

import iubanco.RegistroCuentas;

public class ConsultaCreditos extends JFrame
{  
private static final long serialVersionUID = 1L;
private JTextArea areaMostrarRegistros;
   private JButton botonAbrir, botonCredito, botonDebito, botonCeros;
   private JPanel panelBotones; 
        
   private ObjectInputStream entrada;
   private FileInputStream entradaArchivo;
   private File nombreArchivo;
   private String tipoCuenta="Saldos con credito";
   
   static private DecimalFormat dosDigitos = new DecimalFormat( "0.00" );

   // configurar GUI
   public ConsultaCreditos()
   {
      super( "Programa de consulta de creditos" );

      Container contenedor = getContentPane();

      panelBotones = new JPanel(); // configurar panel para agregarle botones
      
      // crear y configurar boton para abrir el archivos
      botonAbrir = new JButton( "Abrir archivo" );
      panelBotones.add( botonAbrir );

      // registrar componente de escucha de botonAbrir
      botonAbrir.addActionListener(

         // clase interna anonima para manejar evento de botonAbrir
         new ActionListener() {

            // abrir archivo para procesarlo
            public void actionPerformed( ActionEvent evento )
            {
               abrirArchivo();
            }

         } // fin de la clase interna anonima

      ); // fin de la llamada a addActionListener

      // crear y configurar boton para obtener cuentas con saldos con credito
      botonCredito = new JButton( "Saldos con credito" );
      panelBotones.add( botonCredito );
      botonCredito.addActionListener( new ManejadorBotones() );

      // crear y configurar boton para obtener cuentas con saldos con debito
      botonDebito = new JButton( "Saldos con debito" );
      panelBotones.add( botonDebito );
      botonDebito.addActionListener( new ManejadorBotones() );

      // crear y configurar boton para obtener cuentas con saldos en ceros
      botonCeros = new JButton( "Saldos en ceros" );
      panelBotones.add( botonCeros );
      botonCeros.addActionListener( new ManejadorBotones() );

      // establecer area para mostrar resultados
      areaMostrarRegistros = new JTextArea();
      JScrollPane desplazador = new JScrollPane( areaMostrarRegistros );

      // adjuntar componentes al panel de contenido
      contenedor.add( desplazador, BorderLayout.CENTER );
      contenedor.add( panelBotones, BorderLayout.SOUTH );

      botonCredito.setEnabled( false ); // deshabilitar botonCredito
      botonDebito.setEnabled( false );  // deshabilitar botonDebito
      botonCeros.setEnabled( false );   // deshabilitar botonCeros

      // registrar componente de escucha de ventana
      addWindowListener(

         // clase interna anonima para evento windowClosing
         new WindowAdapter() {

            // cerrar archivo y terminar el programa
            public void windowClosing( WindowEvent evento )
            {
               cerrarArchivo();
               System.exit( 0 );
            }

         } // fin de la clase interna anonima

      ); // fin de la llamada a addWindowListener

      pack(); // empaquetar componentes y mostrar ventana
      setSize( 600, 250 );
      setVisible( true );

   } // fin del constructor de ConsultaCreditos

   // permitir al usuario seleccionar el archivo a abrir
   private void abrirArchivo()
   {
      // mostrar cuadro de dialogo, para que el usuario pueda seleccionar el archivo
      JFileChooser selectorArchivo = new JFileChooser();
      selectorArchivo.setFileSelectionMode( JFileChooser.FILES_ONLY );

      int resultado = selectorArchivo.showOpenDialog( this );

      // si el usuario hizo clic en el boton Cancelar del cuadro de dialogo, regresar
      if ( resultado == JFileChooser.CANCEL_OPTION )
         return;

      nombreArchivo = selectorArchivo.getSelectedFile(); // obtener archivo seleccionado

      // mostrar error si el nombre de archivo es incorrecto
      if ( nombreArchivo == null || nombreArchivo.getName().equals( "" ) )
         JOptionPane.showMessageDialog( this, "Nombre de archivo incorrecto", 
            "Nombre de archivo incorrecto", JOptionPane.ERROR_MESSAGE );

      // abrir el archivo
      try {

         // cerrar archivo de la operacion anterior
         if ( entrada != null )  
            entrada.close();   

         entradaArchivo = new FileInputStream( nombreArchivo );
         entrada = new ObjectInputStream( entradaArchivo );
         botonAbrir.setEnabled( false );
         botonCredito.setEnabled( true );
         botonDebito.setEnabled( true );
         botonCeros.setEnabled( true );
      }

      // atrapar problemas que pueden ocurrir al manipular el archivo
      catch ( IOException excepcionES ) {
         JOptionPane.showMessageDialog( this, "El archivo no existe", 
            "Nombre de archivo incorrecto", JOptionPane.ERROR_MESSAGE );
      }

   } // fin del metodo abrirArchivo
  
   // cerrar archivo antes de que termine la aplicacion
   private void cerrarArchivo()
   {
      // cerrar el archivo
      try {
          if ( entrada != null )
             entrada.close();
      }

      // procesar excepciones que puedan ocurrir al cerrar el archivo
      catch ( IOException excepcionES ) {
         JOptionPane.showMessageDialog( this, "Error al cerrar el archivo",
            "Error", JOptionPane.ERROR_MESSAGE );

         System.exit( 1 );
      }

   } // fin del metodo cerrarArchivo

   // leer registros del archivo y mostrar solo los registros del tipo apropiado
   private void leerRegistros()
   {      
      RegistroCuentas registro;
      
      // leer registros
      try {
          
          if ( entrada != null )
             entrada.close();
      
         entradaArchivo = new FileInputStream( nombreArchivo );
         entrada = new ObjectInputStream( entradaArchivo );

         areaMostrarRegistros.setText( "Las cuentas son:\n" );

         // recibir como entrada los valores del archivo
         while ( true ) {

            // leer un RegistroCuentas
            registro = ( RegistroCuentas ) entrada.readObject();

            // si es el tipo de cuenta apropiado, mostrar el registro
            if ( debeMostrarse( registro.obtenerSaldo() ) )
               areaMostrarRegistros.append( registro.obtenerCuenta() + "\t" + 
                  registro.obtenerPrimerNombre() + "\t" + registro.obtenerApellidoPaterno() + 
                  "\t" + dosDigitos.format( registro.obtenerSaldo() ) + "\n" );
         }  
         
      } // fin del bloque try

      // cerrar archivo cuando se llega al fin de archivo
      catch ( EOFException excepcionEOF ) {
         cerrarArchivo();
      }

      // mostrar error si no se puede leer el objeto por no encontrar la clase
      catch ( ClassNotFoundException claseNoEncontrada ) {
         JOptionPane.showMessageDialog( this, "No se pudo crear el objeto",
            "Clase no encontrada", JOptionPane.ERROR_MESSAGE );
      }

      // mostrar error si no se puede leer debido a un problema con el archivo
      catch ( IOException excepcionES ) {
         JOptionPane.showMessageDialog( this, "Error al leer del archivo",
            "Error", JOptionPane.ERROR_MESSAGE );
      }

   } // fin del metodo leerRegistros

   // use el tipo del registro para determinar si el registro debe mostrarse
   public boolean debeMostrarse( double saldo )
   {
      if ( tipoCuenta.equals( "Saldos con credito" ) && saldo < 0 )
         return true;

      else if ( tipoCuenta.equals( "Saldos con debito" ) && saldo > 0 )
         return true;

      else if ( tipoCuenta.equals( "Saldos en ceros" ) && saldo == 0 )
         return true;

      return false;
   }
   
   public static void main( String args[] )
   {
      new ConsultaCreditos();
   }
 
   // clase para el manejo de eventos de botonCredito, botonDebito y botonCeros
   private class ManejadorBotones implements ActionListener {

      // leer registros del archivo
      public void actionPerformed( ActionEvent evento )
      {
         tipoCuenta = evento.getActionCommand();
         leerRegistros();
      }

   } // fin de la clase ManejadorBotones

} // fin de la clase ConsultaCreditos