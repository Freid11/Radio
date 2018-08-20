package udacity.louco.com.radio.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.louco.com.radio.R;
import udacity.louco.com.radio.mvp.model.object.Radio;

public class RvAdapterRadioList extends RecyclerView.Adapter<RvAdapterRadioList.ViewHolder> {

    private List<Radio> radioList = new ArrayList<>();
    private final OnClickListenerRadio onClickListenerRadio;
    private Context context;

    public RvAdapterRadioList(OnClickListenerRadio onClickListenerRadio) {
        this.onClickListenerRadio = onClickListenerRadio;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_item_radio_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(radioList.get(position));
    }

    @Override
    public int getItemCount() {
        return radioList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_logo_radio)
        ImageView logoRadio;

        @BindView(R.id.tv_name_radio)
        TextView nameRadio;

        private Radio lastRadio;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> onClickListenerRadio.onClick(lastRadio));
        }

        private void bind(Radio radio){
            lastRadio = radio;
            nameRadio.setText(radio.getName());
            Glide.with(context)
                    .load(radio.getUrl_image())
                    .into(logoRadio);

        }
    }

    public void addRadio(Radio radio){
        radioList.add(radio);
        notifyDataSetChanged();
    }

    public void addListRadio(List<Radio> radioList){
        this.radioList = radioList;
        notifyDataSetChanged();
    }

    public interface OnClickListenerRadio{
        void onClick(Radio radio);
    }

}
