package com.op23singh.bookstore2;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class booksRecViewAdapter extends RecyclerView.Adapter<booksRecViewAdapter.ViewHolder>{
    private activity_book activity_bookobj;
    ArrayList<Book> books=new ArrayList<>();
    private Context context;
    private String parentActivity;
    protected Book bb;

    public booksRecViewAdapter(Context context, String parentActivity) {
        this.context = context;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_book,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Log.d("TAG","OnBindViewHolder: Called");
        holder.bookname.setText(books.get(position).getName());
        holder.txtAuthor.setText(books.get(position).getAuthor().toString());
        holder.txtDescription.setText(books.get(position).getShortDesc().toString());
        Glide
                .with(context)
                .load(books.get(position).getImageUrl().toString())
                .into(holder.imgbook);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book newbook=books.get(position);
                Intent intent =new Intent(context,activity_book.class);
                intent.putExtra("B00k",newbook);
                context.startActivity(intent);

            }
        });
        holder.btnDownArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book=books.get(position);
                String x=String.valueOf(!Boolean.getBoolean(book.getExpanded()));
                book.setExpanded(x);
                if(parentActivity.equals("AllBooks")){
                    holder.txtdelete.setVisibility(View.GONE);
                }
                else{
                    holder.txtdelete.setVisibility(View.VISIBLE);
                }
                TransitionManager.beginDelayedTransition(holder.parent);
                holder.ExpandedRelLayout.setVisibility(View.VISIBLE);
                holder.btnUpArrow.setVisibility(View.VISIBLE);
                holder.btnDownArrow.setVisibility(View.GONE);
                notifyDataSetChanged();
            }
        });
        holder.btnUpArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(parentActivity.equals("AllBooks")){
                    holder.txtdelete.setVisibility(View.GONE);
                }
                else{
                    holder.txtdelete.setVisibility(View.VISIBLE);
                }
                Book bb=books.get(position);
                bb.setExpanded(String.valueOf(!Boolean.getBoolean(bb.getExpanded())));
                notifyItemChanged(position);
                TransitionManager.beginDelayedTransition(holder.parent);
                holder.btnDownArrow.setVisibility(View.VISIBLE);
                holder.ExpandedRelLayout.setVisibility(View.GONE);
                holder.btnUpArrow.setVisibility(View.GONE);
            }
        });
        holder.txtdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(parentActivity.equals("AllBooks")){
                    holder.txtdelete.setVisibility(View.GONE);
                }
                else if(parentActivity.equals("alreadyread")){
                    String s= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users").child(s).child("Alreadyread");
                    String temp;
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot b:snapshot.getChildren()){
                                if((b.getValue(Book.class).getId().toString()).equals(books.get(position).getId())){
                                    ref.child(b.getKey().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(context,"book successfully removed",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(context,"something went wrong",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if(parentActivity.equals("currentlyreading")){
                    String s= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users").child(s).child("Currentlyreading");
                    String temp;
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot b:snapshot.getChildren()){
                                if((b.getValue(Book.class).getId().toString()).equals(books.get(position).getId())){
                                    ref.child(b.getKey().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(context,"book successfully removed",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(context,"something went wrong",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if(parentActivity.equals("wanttoread")){
                    String s= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                    DatabaseReference ref1=FirebaseDatabase.getInstance().getReference("Users").child(s).child("Wanttoread");
                    ref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot b:snapshot.getChildren()){
                                if((b.getValue(Book.class).getId().toString()).equals(books.get(position).getId())){
                                    ref1.child(b.getKey().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(context,"book successfully removed",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(context,"something went wrong",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else if(parentActivity.equals("favourites")){
                    String s= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users").child(s).child("Favourties");
                    String temp;
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot b:snapshot.getChildren()){
                                if((b.getValue(Book.class).getId().toString()).equals(books.get(position).getId())){
                                    ref.child(b.getKey().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(context,"book successfully removed",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(context,"something went wrong",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
    public void setBooks(ArrayList <Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private CardView parent;
        private TextView bookname,txtdelete;
        private ImageView imgbook;
        RelativeLayout ExpandedRelLayout,CollapsedRelLayout;
        TextView txtAuthor,txtDescription;
        ImageView btnDownArrow,btnUpArrow;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            btnDownArrow=itemView.findViewById(R.id.btnDownArrow);
            btnUpArrow=itemView.findViewById(R.id.btnUpArrow);
            ExpandedRelLayout=itemView.findViewById(R.id.ExpandedRelLayout);
            CollapsedRelLayout=itemView.findViewById(R.id.collapsedRelLayout);
            parent=itemView.findViewById(R.id.parent);
            bookname=itemView.findViewById(R.id.bookname);
            imgbook=itemView.findViewById(R.id.imgbook);
            txtAuthor=itemView.findViewById(R.id.txtauthor);
            txtDescription=itemView.findViewById((R.id.txtshortDesc));
            txtdelete=itemView.findViewById(R.id.txtdelete);
            txtdelete.setVisibility(View.GONE);
        }
    }
}
