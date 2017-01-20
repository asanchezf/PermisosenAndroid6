package com.example.antonio.permisosenandroid6;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private View vista;
    private static final int SOLICITUD_PERMISO_WRITE_CALL_LOG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vista = findViewById(R.id.activity_main);


    }

    public void llamada_onClick(View view) {
        borrarLlamada();

    }

  private void borrarLlamada() {

      //1- Se verifican los permisos...

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {//PERMISSION_GRANTED==>Tiene permisos
      //2-Tiene concedidos los permisos, procedemos a ejecutar el proceso solicitado

            getContentResolver().delete(CallLog.Calls.CONTENT_URI,
                    "number='555555555'", null);
            Snackbar.make(vista, "Llamadas borradas del registro.",
                    Snackbar.LENGTH_SHORT).show();

        }

        else {
        //3-NO tiene permisos. Solicitamos permisos
            solicitarPermisoBorrarLlamada();
        }

    }

    private void solicitarPermisoBorrarLlamada() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_CALL_LOG)) {
            //4-Pequeña explicación de para qué queremos los permisos
            Snackbar.make(vista, "Sin el permiso administrar llamadas no puedo"
                    +" borrar llamadas del registro.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{ Manifest.permission. WRITE_CALL_LOG},
                                    SOLICITUD_PERMISO_WRITE_CALL_LOG);
                        }
                    })
                    .show();
        } else {
            //5-Se muetra cuadro de diálogo predeterminado del sistema para que concedamos o denegemos el permiso
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALL_LOG},
                    SOLICITUD_PERMISO_WRITE_CALL_LOG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        //6-Se ha concedido los permisos... procedemos a ejecutar el proceso
        //Si se preguntara por más permisos el resultado se gestionaría desde aquí.
        if (requestCode == SOLICITUD_PERMISO_WRITE_CALL_LOG) {
            if (grantResults.length== 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                borrarLlamada();
            } else {
        //7-NO se han concedido los permisos. No se puede ejecutar el proceso. Se le informa de ello al usuario.
                Snackbar.make(vista, "Sin el permiso, no puedo realizar la" +
                        "acción", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

}
