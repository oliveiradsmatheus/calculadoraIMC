package matheus.bcc.calculadoraimc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class IMCActivity extends AppCompatActivity {
    TextView tvNome, tvIMC, tvCondicao;
    String nome;
    char sexo;
    double imc, altura;

    int peso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_imc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Window janela = this.getWindow();
        janela.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        tvNome = findViewById(R.id.tvNome);
        tvIMC = findViewById(R.id.tvIMC);
        tvCondicao = findViewById(R.id.tvCondicao);

        nome = getIntent().getStringExtra("nome");
        sexo = getIntent().getCharExtra("sexo", 'm');
        peso = getIntent().getIntExtra("peso",0);
        altura = getIntent().getDoubleExtra("altura",0);
        imc = getIntent().getDoubleExtra("imc",0);

        tvNome.setText(String.format("Olá " + nome));
        tvIMC.setText(String.format(Locale.getDefault(), "Seu IMC é %.2f", imc));
        String texto;
        if (sexo == 'm')
            if (imc < 20.7)
                texto = "Você está abaixo do peso";
            else if (imc < 26.4)
                texto = "Você está no peso normal";
            else if (imc < 27.8)
                texto = "Você está marginalmente acima do peso";
            else if (imc < 31.1)
                texto = "Você está acima do peso ideal";
            else
                texto = "Você está obeso";
        else
            if (imc < 19.1)
                texto = "Você está abaixo do peso";
            else if (imc < 25.8)
                texto = "Você está no peso normal";
            else if (imc < 27.3)
                texto = "Você está marginalmente acima do peso";
            else if (imc < 32.3)
                texto = "Você está acima do peso ideal";
            else
                texto = "Você está obeso";
        tvCondicao.setText(texto);

        armazenarConsulta();
        salvarHistorico();
    }

    private void armazenarConsulta() {
        // Pega a data atual
        LocalDate dataAtual = LocalDate.now();

        // Formata data
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Singleton.historicoList.add(new Usuario(
                nome,
                dataAtual.format(formato),
                tvCondicao.getText().toString(),
                sexo,
                peso,
                altura,
                imc));
    }
    private void salvarHistorico() {
        try {
            FileOutputStream fileOutputStream = openFileOutput("dados.dat", MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(Singleton.historicoList);
            objectOutputStream.close();
        } catch (Exception e) {
            Log.e("Erro de gravação: ", e.getMessage());
        }
    }
}