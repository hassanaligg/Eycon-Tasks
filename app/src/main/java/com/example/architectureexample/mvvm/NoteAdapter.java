package com.example.architectureexample.mvvm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.architectureexample.R;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private List<Note> notes = new ArrayList<>();
    private OnItemClickListener listener;
    //LayoutInflater inflater;
    public boolean isAllChecked = false;
    public boolean singleclickenable = false;
    private int counter = 0;


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.bind(notes.get(position));
        // Note currentNote = notes.get(position);
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position) {
        return notes.get(position);
    }

    public ArrayList<Note> getSelected() {
        ArrayList<Note> selected = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).isSelected()) {
                selected.add(notes.get(i));
            }
        }
        return selected;
    }

    public void setAllChecked(boolean isAllChecked) {
        //ArrayList<Note> select = new ArrayList<>();
        for (Note note : notes) {
            note.setSelected(isAllChecked);

            counter = notes.size()+1;
            if(note.isSelected()){
                counter--;
            }

            if (isAllChecked == true) {
                singleclickenable = true;
            } else if (isAllChecked == false) {
                singleclickenable = false;

            }


        }

        this.isAllChecked = isAllChecked;
        notifyDataSetChanged();
    }
//    public void selectAll() {
//
//        //isSelectedAll=true;
//        notifyDataSetChanged();
//    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;
        private RelativeLayout relativeLayout;


        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_View_title);
            textViewDescription = itemView.findViewById(R.id.text_View_discription);
            textViewPriority = itemView.findViewById(R.id.text_View_priority);
            relativeLayout = itemView.findViewById(R.id.relav);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setSelectionCount(notes.get(getLayoutPosition()).toggleSelected());
                    notifyItemChanged(getLayoutPosition());
                    singleclickenable = true;

                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (singleclickenable == true) {
                        setSelectionCount(notes.get(getLayoutPosition()).toggleSelected());

                        notifyItemChanged(getLayoutPosition());

                    } else {
                        int position = getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION) {

                            listener.onItemClick(notes.get(position));
                        }
                    }
                }
            });
        }

        public void bind(Note note) {
            textViewTitle.setText(note.getTitle());
            textViewDescription.setText(note.getDiscription());
            textViewPriority.setText(note.getPriority() + "");

            if (note.isSelected()) {
                relativeLayout.setBackgroundColor(ContextCompat.getColor(relativeLayout.getContext(), R.color.greencolor));
            } else {
                relativeLayout.setBackgroundColor(ContextCompat.getColor(relativeLayout.getContext(), R.color.white));
            }
        }
    }

    private void setSelectionCount(boolean increase) {
        if (increase) {
            counter++;
        } else {
            counter--;
        }
        if (counter == 0) {
            singleclickenable = false;
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
