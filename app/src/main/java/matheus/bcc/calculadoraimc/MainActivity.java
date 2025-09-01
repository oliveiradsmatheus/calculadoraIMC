package matheus.bcc.calculadoraimc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextInputEditText tiNome;
    TextView tvPeso, tvAltura;
    SeekBar sbPeso, sbAltura;
    Button btCalcular;
    RadioGroup rgSexo;
    RadioButton rbMasc, rbFem;
    String nome;
    char sexo;
    int peso;
    double altura;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().setTitle("Menu");
        Window janela = this.getWindow();
        janela.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(this, R.color.fundo))
        );

        tiNome = findViewById(R.id.tiNome);
        sbPeso = findViewById(R.id.sbPeso);
        sbAltura = findViewById(R.id.sbAltura);
        tvPeso = findViewById(R.id.tvPeso);
        tvAltura = findViewById(R.id.tvAltura);
        rgSexo = findViewById(R.id.rgSexo);
        rbMasc = findViewById(R.id.rbMasc);
        rbFem = findViewById(R.id.rbFem);
        btCalcular = findViewById(R.id.btCalcular);


        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        nome = sharedPreferences.getString("nome", "");
        tiNome.setText(nome);
        sexo = (char) sharedPreferences.getInt("sexo", 'm');
        if (sexo == 'm')
            rbMasc.setChecked(true);
        else
            rbFem.setChecked(true);

        peso = sharedPreferences.getInt("peso", 50);
        sbPeso.setProgress(peso * 10);
        tvPeso.setText(String.format(Locale.getDefault(), "%d", peso));

        altura = sharedPreferences.getFloat("altura", 1.70f);
        sbAltura.setProgress((int)(altura*100));
        tvAltura.setText(String.format(Locale.getDefault(), "%.2f", altura));

        sbPeso.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvPeso.setText(String.format(Locale.getDefault(), "%d", i/10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbAltura.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvAltura.setText(String.format("%s", i / 100.0));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        rgSexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull RadioGroup group, int id) {
                if (id == R.id.rbMasc)
                    sexo = 'm';
                else if (id == R.id.rbFem)
                    sexo = 'f';
            }
        });

        btCalcular.setOnClickListener(e -> trocarActivity());
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Pega os valores mais recentes da tela
        nome = tiNome.getText().toString();
        peso = Integer.parseInt(tvPeso.getText().toString());
        altura = Float.parseFloat(tvAltura.getText().toString());
        // Sexo já é atualizado pelo RadioGroup

        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nome", nome);
        editor.putInt("sexo", sexo);
        editor.putInt("peso", peso);
        editor.putFloat("altura", (float) altura);
        editor.apply(); // Interface diz que .commit está depreciado.
        try {
            FileOutputStream fileOutputStream;
            ObjectOutputStream objectOutputStream;
            fileOutputStream = openFileOutput("dados.dat", MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(Singleton.historicoList);
            objectOutputStream.close();
        }catch (Exception  e){
            Log.e("erroxxx",e.getMessage());
        }
    }

    private void trocarActivity() {
        Intent intent = new Intent(this, IMCActivity.class);
        double imc = calcularIMC();
        intent.putExtra("nome", String.valueOf(tiNome.getText()));
        intent.putExtra("sexo", sexo);
        intent.putExtra("peso",peso);
        intent.putExtra("altura",altura);
        intent.putExtra("imc", imc);
        startActivity(intent);
    }

    private double calcularIMC() {
        peso = sbPeso.getProgress()/10;
        altura = sbAltura.getProgress()/100.0;

        return peso/(altura*altura);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.resetar) {
            Toast.makeText(this, "Dados resetados!", Toast.LENGTH_LONG).show();
            resetarFormulario();
        } else if (item.getItemId() == R.id.historico) {
            visualizarHistorico();
        } else if (item.getItemId() == R.id.sair)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void resetarFormulario() {
        tiNome.setText("");
        tvPeso.setText(String.format(Locale.getDefault(), "%d", 50));
        sbPeso.setProgress(500);
        tvAltura.setText(String.format("%s", 1.50));
        sbAltura.setProgress(150);
        rbMasc.setChecked(false);
        rbFem.setChecked(false);
    }

    private void visualizarHistorico() {
        Intent intent = new Intent(this, HistoricoActivity.class);
        startActivity(intent);
    }
}