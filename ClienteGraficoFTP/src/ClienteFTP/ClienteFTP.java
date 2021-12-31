package ClienteFTP;
/**
 * @author Robert G
 *
 */
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

@SuppressWarnings("serial")
public class ClienteFTP extends JFrame implements ActionListener {

	static FTPClient cliente;

	private JPanel contentPane;
	JTextField txtServidor, txtUsuario;
	private JLabel lblClave;
	private JPasswordField txtClave;

	JButton btnConexion = new JButton("Conectar");
	JButton btnSubirFichero = new JButton("Subir fichero");
	JButton btnDescargarFichero = new JButton("Descargar fichero");
	JButton btnEliminarFichero = new JButton("Eliminar fichero");
	JButton btnCrearCarpeta = new JButton("Crear carpeta");
	JButton btnEliminarCarpeta = new JButton("Eliminar carpeta");
	JButton btnSalir = new JButton("Salir");
	JTextArea textArea = new JTextArea();

	String usuario = "";
	String botonConex = "";
	JFileChooser jfc;
	boolean login = false;
	ImageIcon icon;
	String raiz = "/miscosas";
	FTPFile[] ficheros;
	JLabel lblInfo1, lblInfo2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClienteFTP frame = new ClienteFTP();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClienteFTP() {
		setTitle("CLIENTE FTP");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 535, 625);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblServidor = new JLabel("Servidor FTP:");
		lblServidor.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblServidor.setForeground(Color.BLUE);
		lblServidor.setBounds(10, 11, 87, 20);
		contentPane.add(lblServidor);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setForeground(Color.BLUE);
		lblUsuario.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblUsuario.setBounds(10, 42, 87, 20);
		contentPane.add(lblUsuario);

		txtServidor = new JTextField();
		txtServidor.setText("localhost");
		txtServidor.setForeground(Color.BLUE);
		txtServidor.setBounds(107, 12, 86, 25);
		contentPane.add(txtServidor);
		txtServidor.setColumns(10);

		txtUsuario = new JTextField();
		txtUsuario.setText("Usuario1");
		txtUsuario.setForeground(Color.BLUE);
		txtUsuario.setColumns(10);
		txtUsuario.setBounds(107, 43, 150, 25);
		contentPane.add(txtUsuario);

		lblClave = new JLabel("Clave:");
		lblClave.setForeground(Color.BLUE);
		lblClave.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblClave.setBounds(287, 42, 55, 20);
		contentPane.add(lblClave);

		btnConexion.addActionListener(this);
		btnConexion.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnConexion.setBounds(287, 11, 201, 23);
		contentPane.add(btnConexion);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 73, 332, 425);
		contentPane.add(scrollPane);
		textArea.setForeground(SystemColor.textHighlight);
		textArea.setEditable(false);
		textArea.setSelectedTextColor(Color.BLACK);
		textArea.setBorder(new MatteBorder(1, 1, 1, 1, (Color) SystemColor.activeCaption));
		scrollPane.setViewportView(textArea);

		btnSubirFichero.addActionListener(this);
		btnSubirFichero.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnSubirFichero.setBounds(354, 110, 153, 30);
		contentPane.add(btnSubirFichero);

		btnDescargarFichero.addActionListener(this);
		btnDescargarFichero.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnDescargarFichero.setBounds(354, 163, 153, 30);
		contentPane.add(btnDescargarFichero);

		btnEliminarFichero.addActionListener(this);
		btnEliminarFichero.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnEliminarFichero.setBounds(354, 215, 153, 30);
		contentPane.add(btnEliminarFichero);

		btnCrearCarpeta.addActionListener(this);
		btnCrearCarpeta.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCrearCarpeta.setBounds(354, 268, 153, 30);
		contentPane.add(btnCrearCarpeta);

		btnEliminarCarpeta.addActionListener(this);
		btnEliminarCarpeta.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnEliminarCarpeta.setBounds(354, 320, 153, 30);
		contentPane.add(btnEliminarCarpeta);

		btnSalir.addActionListener(this);
		btnSalir.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnSalir.setBounds(354, 376, 153, 30);
		contentPane.add(btnSalir);

		txtClave = new JPasswordField();
		txtClave.setText("usu1");
		txtClave.setBounds(331, 43, 150, 25);
		contentPane.add(txtClave);

		lblInfo1 = new JLabel("");
		lblInfo1.setFont(new Font("Dialog", Font.BOLD, 14));
		lblInfo1.setForeground(Color.BLUE);
		lblInfo1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) SystemColor.activeCaption));
		lblInfo1.setBounds(12, 510, 495, 30);
		contentPane.add(lblInfo1);

		lblInfo2 = new JLabel("");
		lblInfo2.setFont(new Font("Dialog", Font.BOLD, 14));
		lblInfo2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) SystemColor.activeCaption));
		lblInfo2.setBounds(12, 544, 495, 30);
		contentPane.add(lblInfo2);
	}// FIN Frame

	@Override
	public void actionPerformed(ActionEvent e) {
		//////////// BOTÓN CONEXIÓN/////////////
		if (e.getSource() == btnConexion) {
			botonConex = btnConexion.getText();
			if (botonConex.contains("Conectar")) {
				// Nos conectamos
				textArea.setText("");
				cliente = new FTPClient();
				String servFTP = txtServidor.getText();

				usuario = txtUsuario.getText();
				String clave = txtClave.getText();

				try {
					cliente.connect(servFTP);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					textArea.append("ERROR DE CONEXIÓN\n");
					System.out.println("ERROR DE CONEXIÓN");
					textArea.append("Arranca el servicio FTP e intenta conectar de nuevo\n");
					System.err.println("Arranca el servicio FTP e intenta conectar de nuevo");
					// e2.printStackTrace();
				}
				try {
					login = cliente.login(usuario, clave);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					textArea.append("La conexión no esta abierta\n");
					System.err.println("La conexión no esta abierta");
					// e1.printStackTrace();
				}
				if (login == true) {
					JOptionPane.showMessageDialog(null, "CONEXIÓN REALIZADA CON ÉXITO", "Entrando al servidor",
							JOptionPane.INFORMATION_MESSAGE);
					System.out.println("Usuario y clave correctos");
					btnConexion.setText("Desconectar");
					textArea.append("Conectado a servidor: " + servFTP + "\n");
					System.out.println("Nos conectamos al servidor: " + servFTP);
					try {// Nos situamos en la raiz del equipo remoto
						cliente.changeWorkingDirectory(raiz);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						textArea.append("/\n");
						textArea.append("(RAÍZ) " + cliente.printWorkingDirectory() + "\n");
						System.out.println("Directorio actual: " + cliente.printWorkingDirectory());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} // pwd en cmd
					ficheros = null;
					try {
						ficheros = cliente.listFiles();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					textArea.append("Total Ficheros/Directorios en " + raiz + ": " + ficheros.length + "\n");
					textArea.append("***************LISTADO***************\n");
					lblInfo1.setText(" << ARBOL DE DIRECTORIOS CONSTRUIDO >> ");
					lblInfo2.setText("");
					try {
						verficheros(ficheros);// Voy al método externo
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					textArea.append("***********FIN DEL LISTADO***********\n");
				} else {
					textArea.append("Error en usuario y/o clave\n");
					System.out.println("Error en usuario y/o clave");

					int respuesta = cliente.getReplyCode();
					System.out.println("La respuesta es: " + respuesta);

					try {
						cliente.disconnect();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "Servidor, Usuario o clave incorrectos", "Error",
							JOptionPane.ERROR_MESSAGE);
					textArea.append("Intentalo de nuevo\n");
					System.out.println("Intentalo de nuevo");
				}
			} else if (botonConex.contains("Desconectar")) {
				int codigo = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres desconectar?", "Desconectar",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (codigo == JOptionPane.YES_OPTION) {
					boolean logout = false;
					try {
						lblInfo2.setText("");
						btnConexion.setText("Conectar");
						logout = cliente.logout();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (logout) {
						System.out.println("Logout...");
						icon = new ImageIcon("src/images/bye.png");
						JOptionPane.showMessageDialog(null, "Esperamos que vuelvas pronto", "Sentimos que te vayas",
								JOptionPane.INFORMATION_MESSAGE, icon);
						login = false;// Dejo login de nuevo en false
					} else {
						System.err.println("Error!!");
					}
					try {
						cliente.disconnect();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					textArea.append("Conexión finalizada\n");
					System.out.println("Conexión finalizada");
					lblInfo1.setText(" << HAS DESCONECTADO DEL SERVIDOR >> ");
					lblInfo2.setText(" << AHORA PUEDES PULSAR EL BOTÓN SALIR >> ");
				} else if (codigo == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(null, "No te vayas todavía!!", "Esta bien!!",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} // FIN btnConexion

		//////////// BOTÓN SUBIR FICHERO/////////////
		if (e.getSource() == btnSubirFichero) {
			// Compruebo que estamos conectados
			if (login == true) {
				// Establecemos el tipo de archivo que se va a subir
				try {
					cliente.setFileType(FTP.BINARY_FILE_TYPE);// que permite ficheros de cualquier tipo
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// Seleccionamos el fichero que queremos subir
				String origen = "D:\\Subidas";
				// JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc = new JFileChooser(origen);
				jfc.setDialogTitle("Selecciona el Fichero a SUBIR AL SERVIDOR FTP");
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					// Almaceno el nombre del fichero a subir en sf
					String sf = selectedFile.getName();
					// Crear el Stream de entrada para el fichero
					BufferedInputStream in = null;
					try {
						in = new BufferedInputStream(new FileInputStream(selectedFile));

					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// Subir el fichero al servidor
					try {// Este es el nombre del fichero como queda después de subirlo
							// if (cliente.storeFile("fichero.txt", in)) {
						if (cliente.storeFile(sf, in)) {
							icon = new ImageIcon("src/images/exito.jpg");
							JOptionPane.showMessageDialog(null,
									"Ya esta el fichero " + "***" + sf + "***" + " en el servidor",
									"Fichero subido con éxito", JOptionPane.INFORMATION_MESSAGE, icon);
							info();
						} else {
							sinpermiso();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// Cerramos el flujo
					try {
						in.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} // Fin if APPROVE_OPTION
			} else {
				sinconexion();
			}
		} // FIN btnSubirFichero

		//////////// BOTÓN DESCARGAR FICHERO/////////////
		if (e.getSource() == btnDescargarFichero) {
			// Compruebo que estamos conectados
			if (login == true) {
				// workfolder();// Nos situamos en el trayecto FTP
				// Establecemos el tipo de archivo que se va a descargar
				try {
					cliente.setFileType(FTP.BINARY_FILE_TYPE);// que permite ficheros de cualquier tipo
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// Elegimos el fichero que vamos a descargar del servidor
				jfc = new JFileChooser(workfolder());
				jfc.setDialogTitle("Selecciona el Fichero a DESCARGAR DEL SERVIDOR FTP");
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					// Almaceno el nombre del fichero a descargar en sf
					String sf = selectedFile.getName();

					// Crear el Stream de salida para el fichero
					String descarga = "D:\\Descargas\\" + sf;// Ponemos el destino de descarga
					// junto con el fichero seleccionado que descargamos del servidor
					BufferedOutputStream out = null;
					try {
						out = new BufferedOutputStream(new FileOutputStream(descarga));
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					// Descargar el fichero a nuestro equipo
					try {
						if (cliente.retrieveFile(sf, out)) {
							icon = new ImageIcon("src/images/exito.jpg");
							JOptionPane.showMessageDialog(null,
									"Ya esta el fichero " + "***" + sf + "***" + " en tu equipo",
									"Fichero descargado con éxito", JOptionPane.INFORMATION_MESSAGE, icon);
						} else {
							sinpermiso();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// Cerramos el flujo
					try {
						out.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} // Fin if APPROVE_OPTION
			} else {
				sinconexion();
			}
		} // Fin btnDescargarFichero

		//////////// BOTÓN ELIMINAR FICHERO/////////////
		if (e.getSource() == btnEliminarFichero) {
			// Compruebo que estamos conectados
			if (login == true) {
				// workfolder();// Nos situamos en el trayecto FTP
				// Establecemos el tipo de archivo que se va a descargar
				try {
					cliente.setFileType(FTP.BINARY_FILE_TYPE);// que permite ficheros de cualquier tipo
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// Elegimos el fichero que vamos a eliminar del servidor
				// jfc = new JFileChooser("c:\\xampp\\htdocs\\"+usuario);
				jfc = new JFileChooser(workfolder());
				jfc.setDialogTitle("Selecciona el Fichero a ELIMINAR DEL SERVIDOR FTP");
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					// Almaceno el nombre del fichero a descargar en sf
					String sf = selectedFile.getName();

					int codigo = JOptionPane.showConfirmDialog(null, "¿Seguro que le quieres eliminar?", "Eliminar",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (codigo == JOptionPane.YES_OPTION) {
						// Borramos el fichero del servidor FTP
						try {
							if (cliente.deleteFile(sf)) {
								icon = new ImageIcon("src/images/exito.jpg");
								JOptionPane.showMessageDialog(null,
										"El fichero " + "***" + sf + "***" + " ha sido borrado del servidor FTP",
										"Fichero borrado con éxito", JOptionPane.INFORMATION_MESSAGE, icon);
								info();
							} else {
								sinpermiso();
							}
						} catch (HeadlessException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (codigo == JOptionPane.NO_OPTION) {
						JOptionPane.showMessageDialog(null, "No le borro todavía!!", "Esta bien!!",
								JOptionPane.INFORMATION_MESSAGE);
					} else if (codigo == JOptionPane.CANCEL_OPTION) {
					}
				} // Fin if APPROVE_OPTION
			} else {
				sinconexion();
			}
		} // Fin btnEliminarFichero

		//////////// BOTÓN CREAR CARPETA/////////////
		if (e.getSource() == btnCrearCarpeta) {
			// Compruebo que estamos conectados
			if (login == true) {
				workfolder();// Nos situamos en el trayecto FTP
				String direc = JOptionPane.showInputDialog(null, "Introduce el nombre del directorio a crear");

				int codigo = JOptionPane.showConfirmDialog(null, "¿Seguro que le quieres crear?", "Crear",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (codigo == JOptionPane.YES_OPTION) {
					if (!direc.isEmpty()) {// Compruebo que el campo no esté vacío
						try {
							if (cliente.makeDirectory(direc)) {
								icon = new ImageIcon("src/images/exito.jpg");
								JOptionPane.showMessageDialog(null,
										"El directorio " + "***" + direc + "***" + " ha sido creado con éxito",
										"Directorio creado con éxito", JOptionPane.INFORMATION_MESSAGE, icon);
								info();
							} else {
								JOptionPane.showMessageDialog(null, "¡¡ERROR!! La carpeta ya existe", "No puedes crear",
										JOptionPane.ERROR_MESSAGE);
							}
						} catch (HeadlessException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {// Fin distinto de vacío
						JOptionPane.showMessageDialog(null, "¡¡ERROR!! No me has dicho que carpeta quieres crear",
								"Campo vacío", JOptionPane.ERROR_MESSAGE);
					}
				} else if (codigo == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(null, "No le creo todavía!!", "Esta bien!!",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (codigo == JOptionPane.CANCEL_OPTION) {
				}
			} else {
				sinconexion();
			}
		} // Fin btnCrearCarpeta

		//////////// BOTÓN ELIMINAR CARPETA/////////////
		if (e.getSource() == btnEliminarCarpeta) {
			// Compruebo que estamos conectados
			if (login == true) {
				workfolder();// Nos situamos en el trayecto FTP
				String direc = JOptionPane.showInputDialog(null, "Introduce el nombre del directorio a borrar");

				int codigo = JOptionPane.showConfirmDialog(null, "¿Seguro que le quieres eliminar?", "Eliminar",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (codigo == JOptionPane.YES_OPTION) {
					if (!direc.isEmpty()) {// Compruebo que el campo no esté vacío
						try {
							if (cliente.removeDirectory(direc)) {
								icon = new ImageIcon("src/images/exito.jpg");
								JOptionPane.showMessageDialog(null,
										"El directorio " + "***" + direc + "***" + " ha sido borrado con éxito",
										"Directorio borrado con éxito", JOptionPane.INFORMATION_MESSAGE, icon);
								info();
							} else {
								JOptionPane.showMessageDialog(null, "¡¡ERROR!! La carpeta no existe o esta llena",
										"No puedes borrar", JOptionPane.ERROR_MESSAGE);
							}
						} catch (HeadlessException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {// Fin distinto de vacío
						JOptionPane.showMessageDialog(null, "¡¡ERROR!! No me has dicho que carpeta quieres borrar",
								"Campo vacío", JOptionPane.ERROR_MESSAGE);
					}
				} else if (codigo == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(null, "No le borro todavía!!", "Esta bien!!",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (codigo == JOptionPane.CANCEL_OPTION) {
				}
			} else {
				sinconexion();
			}
		} // Fin btnEliminarCarpeta

		//////////// BOTÓN SALIR/////////////
		if (e.getSource() == btnSalir) {
			if (login == true) {
				JOptionPane.showMessageDialog(null, "Primero quiero que desconectes!!!", "Estas conectado",
						JOptionPane.ERROR_MESSAGE);
			} else {
				System.exit(1);
			}
		} // FIN btnSalir
	}// FIN actionPerformed

	// Método Aviso sin permiso
	private void sinpermiso() {
		JOptionPane.showMessageDialog(null, "¡¡ERROR!! Estas fuera de RAÍZ", "No tienes permiso",
				JOptionPane.ERROR_MESSAGE);
	}

	// Método Aviso sin conexión
	private void sinconexion() {
		JOptionPane.showMessageDialog(null, "Primero tienes que conectar!!!", "No hay conexión",
				JOptionPane.ERROR_MESSAGE);
	}

	// Método info
	private void info() throws IOException {
		ficheros = cliente.listFiles();
		// ACTUALIZO DATOS CADA VEZ QUE SUBO UN FICHERO//
		textArea.append("/\n");
		textArea.append("(RAÍZ) " + cliente.printWorkingDirectory() + "\n");
		textArea.append("Total Ficheros/Directorios en " + raiz + ": " + ficheros.length + "\n");
		textArea.append("***************LISTADO***************\n");
		verficheros(ficheros);// Voy al método externo
		textArea.append("***********FIN DEL LISTADO***********\n");
	}

	// Método para situarnos en un punto concreto del FTP
	public String workfolder() {// Nos situamos en la carpeta de trabajo
		// Con la siguiente linea en localhost - Aquí he puesto la ruta del disco local
		// donde estoy
		String directorio = "C:\\xampp\\htdocs\\" + usuario + "\\miscosas";

		// Con la siguiente linea en servidor - Aquí he puesto la ruta del equipo en red
		// en mi casa
		// MUY IMPORTANTE Salon tiene que tener 4 barras - 2 de red y otras 2 de carpeta
		 //String directorio = "\\\\Salon\\c\\xampp\\htdocs\\" + usuario + "\\miscosas";
		return directorio;
	}

	// Método para verficheros
	private void verficheros(FTPFile[] ficheros) throws IOException {
		String tipo = "";

		for (int i = 0; i < ficheros.length; i++) {

			if (ficheros[i].getType() == 0) {
				tipo = "Fichero";
				imprimir(ficheros[i], cliente);// Le paso al método el ftp y el cliente

			} else if (ficheros[i].getType() == 1) {
				tipo = "Directorio";
//				System.out.println("----------------------------------");
//				System.out.println("ESTA ES LA INFORMACIÓN DEL DIRECTORIO");
//				System.out.println("----------------------------------");
				textArea.append("DIRECT: " + ficheros[i].getName() + "\n");
				cliente.changeWorkingDirectory(ficheros[i].getName());
				FTPFile[] ficherosdirectorio = cliente.listFiles();
				verficheros(ficherosdirectorio);
				// System.out.println("Vamos a un nivel inferior");
				cliente.changeWorkingDirectory("..");// cd.. de siempre en ms-dos
			} else
				tipo = "Enlace";
			// System.out.println(ficheros[i].getName() + "=>" + tipo);
		}
	}// FIN verficheros

	// Método para imprimir ficheros
	private void imprimir(FTPFile ftpFile, FTPClient cliente2) throws IOException {
		/////////////// Uso File para poder usar getAbsolutePath///////////////////
		File fichero = new File(ftpFile.getName());

		textArea.append("FICHERO: " + fichero.getName() + "\n");

	}// FIN imprimir
}// FIN Clase ClienteFTP
