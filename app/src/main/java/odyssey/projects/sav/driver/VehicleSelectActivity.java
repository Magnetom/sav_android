package odyssey.projects.sav.driver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import odyssey.projects.adapter.VehiclesCursorAdapter;
import odyssey.projects.db.DbProcessor;
import odyssey.projects.db.VehiclesViewer;
import odyssey.projects.intf.VehicleSelectedCallback;
import odyssey.projects.pref.LocalSettings;

public class VehicleSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_select);

        final Context context = this;

        final VehiclesViewer viewer = new VehiclesViewer(this, new VehicleSelectedCallback() {
            @Override
            public void onSelected(String vehicle) {
                // Готовим данные для возврата их в родительскую активити.
                Intent intent = new Intent();
                intent.putExtra("VEHICLE", vehicle);
                setResult(RESULT_OK, intent);

                // Завершаем текущую активити.
                finish();
            }
        });

        // Настраиваем кнопку ДОБАВИТЬ ГОСНОМЕР
        View btn = findViewById(R.id.addNewVehicleBtn);
        if (btn != null)
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View editLayout = getLayoutInflater().inflate(R.layout.new_vehicle_layout,null);

                final TextView vid = editLayout.findViewById(R.id.vehicleNewIdView);

                // Настраиваем диалоговое окно ввода госномера.
                new AlertDialog.Builder(context)
                        .setView(editLayout)
                        .setPositiveButton("Готово", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                if (!vid.getText().toString().equals("")){

                                    // Готовим данные для возврата их в родительскую активити.
                                    Intent intent = new Intent();
                                    // Переводим госномер в верхний регистр.
                                    vid.setText(vid.getText().toString().toUpperCase());

                                    //////////////////////////////////////////////////////////////
                                    // РЕАЛИЗАЦИЯ СКРЫТЫХ СЕРВИСНЫХ ФУНКЦИ!
                                    // Очистка локальной БД.
                                    //if (vid.getText().toString().equals("М750АМ750")){
                                    if (vid.getText().toString().equals("123456")){
                                        // Стираем все отметки.
                                        //viewer.clearTableMarks();
                                        Boolean result = DbProcessor.getInstance(context).clearTableMarks();
                                        // Стираем все госномера.
                                        viewer.removeAllVehicles();
                                        // Стираем текущее ТС.
                                        LocalSettings.getInstance(context).saveText(LocalSettings.SP_VEHICLE, "");
                                        return;
                                    }
                                    //////////////////////////////////////////////////////////////

                                    intent.putExtra("VEHICLE", vid.getText().toString());
                                    setResult(RESULT_OK, intent);

                                    // Закрываем текущее диалоговое окно.
                                    dialog.cancel();
                                    // Завершаем текущую активити.
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }
}