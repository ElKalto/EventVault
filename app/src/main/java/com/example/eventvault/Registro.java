package com.example.eventvault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
public class Registro extends AppCompatActivity {

    private EditText editTextMailReg, editTextPassword, edTextPassRepReg, edTextNomAsoReg;
    private CheckBox checkBoxReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar vistas
        editTextMailReg = findViewById(R.id.edTextMailReg);
        editTextPassword = findViewById(R.id.editTextPassword);
        edTextPassRepReg = findViewById(R.id.edTextPassRepReg);
        edTextNomAsoReg = findViewById(R.id.edTextNomAsoReg);
        checkBoxReg = findViewById(R.id.checkBoxReg);

        Button btnAcpReg = findViewById(R.id.btnAcpReg);
        final TextView textView4 = findViewById(R.id.txtViewReg5);
        final TextView textView7 = findViewById(R.id.txtViewReg6);

        // Inicialmente, ocultar editTextNomAsoReg
        edTextNomAsoReg.setVisibility(View.GONE);

        checkBoxReg.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Cambiar la visibilidad basada en el estado del CheckBox
            if (isChecked) {
                textView7.setVisibility(View.VISIBLE);
                edTextNomAsoReg.setVisibility(View.VISIBLE);
            } else {
                textView7.setVisibility(View.GONE);
                edTextNomAsoReg.setVisibility(View.GONE);
            }
        });

        btnAcpReg.setOnClickListener(view -> {
            // Realizar la validación antes de continuar
            if (validarCampos()) {
                // Guardar datos en el archivo XML (usuarios.xml)
                guardarDatos();

                // Redirigir al usuario según el tipo
                if (checkBoxReg.isChecked()) {
                    // Usuario Creador
                    startActivity(new Intent(Registro.this, PerfilCreador.class));
                } else {
                    // Usuario Básico
                    startActivity(new Intent(Registro.this, Perfil.class));
                }

                // Cierra la actividad actual (Registro)
                finish();
            }
        });

    }

    private boolean validarCampos() {
        // Validar que los campos obligatorios estén llenos
        if (editTextMailReg.getText().toString().isEmpty() ||
                editTextPassword.getText().toString().isEmpty() ||
                edTextPassRepReg.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar que las contraseñas coincidan
        if (!editTextPassword.getText().toString().equals(edTextPassRepReg.getText().toString())) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Si el checkbox está marcado, validar que se ingrese el nombre de la asociación
        if (checkBoxReg.isChecked() && edTextNomAsoReg.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingrese el nombre de la asociación o empresa", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void guardarDatos() {
        try {
            // Crear un nuevo documento XML
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // Crear el elemento raíz
            Element rootElement = document.createElement("usuarios");
            document.appendChild(rootElement);

            // Crear un elemento para el usuario actual
            Element usuarioElement = document.createElement("usuario");

            // Añadir elementos hijos al usuario (correo, contraseña, nombreAsociacion, tipoUsuario)
            usuarioElement.appendChild(crearElemento(document, "correo", editTextMailReg.getText().toString()));
            usuarioElement.appendChild(crearElemento(document, "contrasena", editTextPassword.getText().toString()));
            usuarioElement.appendChild(crearElemento(document, "nombreAsociacion", edTextNomAsoReg.getText().toString()));
            usuarioElement.appendChild(crearElemento(document, "tipoUsuario", checkBoxReg.isChecked() ? "Creador" : "Basico"));

            // Añadir el elemento de usuario al elemento raíz
            rootElement.appendChild(usuarioElement);

            // Guardar el documento en un archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);

            // Guardar el archivo en la memoria interna de la aplicación
            File file = new File(getFilesDir(), "Usuarios.xml");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            StreamResult result = new StreamResult(fileOutputStream);
            transformer.transform(source, result);

            // Cierra el flujo de salida
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Element crearElemento(Document document, String nombre, String valor) {
        Element elemento = document.createElement(nombre);
        elemento.appendChild(document.createTextNode(valor));
        return elemento;
    }
}
