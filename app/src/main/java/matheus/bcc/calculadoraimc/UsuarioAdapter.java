package matheus.bcc.calculadoraimc; // Adicione esta linha
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public class UsuarioAdapter extends ArrayAdapter<Usuario> {
    private int resource;

    public UsuarioAdapter(@NonNull Context context, int resource, @NonNull List<Usuario> usuarioList) {
        super(context, resource, usuarioList);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource, parent, false);
        }

        // Obtém o objeto Usuario para esta posição
        Usuario usuario = getItem(position);


        TextView tvDataHist = convertView.findViewById(R.id.tvDataHist);
        TextView tvNomeHist = convertView.findViewById(R.id.tvNomeHist);
        TextView tvPesoHist = convertView.findViewById(R.id.tvPesoHist);
        TextView tvAlturaHist = convertView.findViewById(R.id.tvAlturaHist);
        TextView tvSexoHist = convertView.findViewById(R.id.tvSexoHist);
        TextView tvImcHist = convertView.findViewById(R.id.tvImcHist);
        TextView tvCondFisHist = convertView.findViewById(R.id.tvCondFisHist);
        Button btExcluir = convertView.findViewById(R.id.btExcluir);

        // Preenche os TextViews com os dados do objeto Usuario
        if (usuario != null) {
            tvDataHist.setText(usuario.getData());
            tvNomeHist.setText(usuario.getNome());
            tvPesoHist.setText(String.format("%.1f", (double) usuario.getPeso()));
            tvAlturaHist.setText(String.format("%.2f", usuario.getAltura()));
            tvSexoHist.setText(usuario.getSexo() == 'm' ? "Masculino" : "Feminino");
            tvImcHist.setText(String.format("%.2f", usuario.getIMC()));
            tvCondFisHist.setText(usuario.getCondFisica());
        }

        //botão de exclusão
        btExcluir.setOnClickListener(v -> {
            remove(usuario);
            notifyDataSetChanged();
        });

        return convertView;
    }
}
