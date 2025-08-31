package matheus.bcc.calculadoraimc;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.List;

public class HistoricoActivity extends AppCompatActivity {

    private ListView listView;
    private Button btExcluirTudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historico);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        listView = findViewById(R.id.listView);
        btExcluirTudo = findViewById(R.id.btExcluirTudo);

        carregarHistorico();
        exibirHistorico();

        btExcluirTudo.setOnClickListener(e -> {
            excluirRegistros();
            exibirHistorico(); // Atualiza a ListView após a exclusão
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        salvarHistorico();
    }

    private void carregarHistorico() {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        try{
            fileInputStream=openFileInput("dados.dat");
            objectInputStream=new ObjectInputStream(fileInputStream);
            Singleton.historicoList=(List<Usuario>)objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (Exception e){
            Log.e("erroxxx",e.getMessage());
        }
    }

    private void salvarHistorico() {
        try {
            FileOutputStream fileOutputStream = openFileOutput("dados.dat", MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(Singleton.historicoList);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void excluirRegistros() {
        Singleton.historicoList.clear();
        salvarHistorico();
    }
    private void exibirHistorico() {
        listView.setAdapter(new UsuarioAdapter(this, R.layout.item_layout, Singleton.historicoList));
    }
}