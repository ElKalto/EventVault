package com.example.eventvault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class EventVault extends AppCompatActivity {

    // Variable de instancia para almacenar la lista de usuarios
    private List<Usuario> usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_vault);

        Button btnRegistro = findViewById(R.id.btnRegistro);
        Button btnContinuar = findViewById(R.id.button2);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent para iniciar la actividad Registro
                Intent intent = new Intent(EventVault.this, Registro.class);
                startActivity(intent);
            }
        });

        // Cargar los usuarios cuando se inicia la actividad
        usuarios = obtenerUsuarios();

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) findViewById(R.id.TxMail)).getText().toString();
                String password = ((EditText) findViewById(R.id.TxPass)).getText().toString();

                // Verificar las credenciales
                if (verificarCredenciales(email, password)) {
                    // Las credenciales son válidas, redirigir al usuario según el tipo
                    if (esUsuarioCreador(email)) {
                        startActivity(new Intent(EventVault.this, PerfilCreador.class));
                    } else {
                        startActivity(new Intent(EventVault.this, Perfil.class));
                    }

                    // Cierra la actividad actual (EventVault)
                    finish();
                } else {
                    // Mostrar mensaje de error si las credenciales no son válidas
                    Toast.makeText(EventVault.this, "Credenciales no válidas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean verificarCredenciales(String email, String password) {
        // Lógica para verificar las credenciales en la lista de usuarios
        // Retorna true si las credenciales son válidas, false en caso contrario
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email) && usuario.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private boolean esUsuarioCreador(String email) {
        // Lógica para determinar si un usuario es de tipo "Creador"
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email) && usuario.isCreador()) {
                return true;
            }
        }
        return false;
    }

    private List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();

        try {
            File file = new File(getFilesDir(), "Usuarios.xml");
            FileInputStream fis = new FileInputStream(file);

            // Crear un DocumentBuilder para parsear el XML
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // Parsear el archivo XML
            Document document = documentBuilder.parse(fis);

            // Obtener la lista de nodos de usuario
            NodeList usuarioNodes = document.getElementsByTagName("usuario");

            // Recorrer la lista de nodos y obtener la información de cada usuario
            for (int i = 0; i < usuarioNodes.getLength(); i++) {
                Node usuarioNode = usuarioNodes.item(i);

                if (usuarioNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element usuarioElement = (Element) usuarioNode;

                    String email = obtenerValorElemento(usuarioElement, "correo");
                    String password = obtenerValorElemento(usuarioElement, "contrasena");
                    String nombreAsociacion = obtenerValorElemento(usuarioElement, "nombreAsociacion");
                    boolean esCreador = "Creador".equals(obtenerValorElemento(usuarioElement, "tipoUsuario"));

                    usuarios.add(new Usuario(email, password, esCreador));
                }
            }

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    private String obtenerValorElemento(Element elemento, String nombre) {
        NodeList nodeList = elemento.getElementsByTagName(nombre);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}
