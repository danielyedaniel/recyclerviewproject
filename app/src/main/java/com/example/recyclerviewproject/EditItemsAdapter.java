package com.example.recyclerviewproject;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewproject.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class EditItemsAdapter extends RecyclerView.Adapter<EditItemsAdapter.ItemViewHolder> {

    private static TextInputLayout inputLayout;
    private final List<String> items;

    private final Runnable onUpdate;

    public EditItemsAdapter(@NonNull List<String> items, @NonNull Runnable onUpdate)  {
        this.items = items;
        this.onUpdate = onUpdate;
        this.inputLayout = inputLayout;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.edit_item_row, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        String item = items.get(position);

        holder.bind(position, item);
    }

    public static void setLayout(String name){
        inputLayout.setHint(name);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void notifyItemAdded() {
        notifyItemChanged(items.size() - 1);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private final EditText editText;

        public  final TextInputLayout inputLayout;

        private TextWatcher textWatcher;

        public  void setLayout (String name){
            inputLayout.setHint(name);
        }

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            editText = itemView.findViewById(R.id.edtInput);
            inputLayout = itemView.findViewById(R.id.editItemRow_inputLayout);
        }

        public void bind(int position, @NonNull String item) {

            if (textWatcher != null) {
                editText.removeTextChangedListener(textWatcher);
            }

            // i didn't see any "update chart" button in you screenshots, so I assume
            // you update things using a textChangedListener

            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable text) {

                    items.set(position, text.toString());

                    onUpdate.run();
                }
            };

            editText.addTextChangedListener(textWatcher);

            inputLayout.setHint(".");
            editText.setText(item);
        }
    }

}
