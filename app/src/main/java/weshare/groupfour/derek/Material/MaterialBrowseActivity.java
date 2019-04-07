package weshare.groupfour.derek.Material;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import weshare.groupfour.derek.R;

public class MaterialBrowseActivity extends AppCompatActivity {
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material__browse);
        RecyclerView recycler = findViewById(R.id.recyclerView);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        //recycler.setLayoutManager(layoutManager);


        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        recycler.setLayoutManager(staggeredGridLayoutManager);

        ImageView ivChange = findViewById(R.id.ivChange);
        ivChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (staggeredGridLayoutManager.getSpanCount()){
                    case 1:
                        staggeredGridLayoutManager.setSpanCount(2);
                        break;
                    case 2:
                        staggeredGridLayoutManager.setSpanCount(1);
                        break;
                }
            }
        });


    ImageView ivMyCart = findViewById(R.id.ivMyCart);
        ivMyCart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MaterialBrowseActivity.this,"查看我的購物車",Toast.LENGTH_LONG).show();
        }
    });

    final List<Material> materialList = new ArrayList<>();
        materialList.add(new Material(R.drawable.pencil,"百樂色色自動鉛筆","1000"));
        materialList.add(new Material(R.drawable.pencil2,"電容式觸控筆 懷舊鉛筆造型款","1200"));
        materialList.add(new Material(R.drawable.pencil,"百樂色色自動鉛筆","1000"));
        materialList.add(new Material(R.drawable.pencil2,"電容式觸控筆 懷舊鉛筆造型款","1200"));
        materialList.add(new Material(R.drawable.pencil,"百樂色色自動鉛筆","1000"));
        materialList.add(new Material(R.drawable.pencil2,"電容式觸控筆 懷舊鉛筆造型款","1200"));
        materialList.add(new Material(R.drawable.pencil,"百樂色色自動鉛筆","1000"));
        materialList.add(new Material(R.drawable.pencil2,"電容式觸控筆 懷舊鉛筆造型款","1200"));
        materialList.add(new Material(R.drawable.pencil,"百樂色色自動鉛筆","1000"));



        recycler.setAdapter(new CommondityAdapter(materialList));

}
private class CommondityAdapter extends RecyclerView.Adapter<CommondityAdapter.ViewHolder>{
    private List<Material> materialList;

    public CommondityAdapter(List<Material> materialList) { this.materialList = materialList; }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName,tvPrice;
        private ImageView ivIcon,ivCart,ivHeart;
        private int heart;
        public ViewHolder( View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvPrice = view.findViewById(R.id.tvPrice);
            ivIcon = view.findViewById(R.id.ivIcon);
            ivCart = view.findViewById(R.id.ivCart);
            ivHeart = view.findViewById(R.id.ivHeart);
            heart = 0;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Material material = materialList.get(position);
        holder.tvPrice.setText("特價 : "+ material.getPrice());
        holder.tvName.setText(material.getName());
        holder.ivIcon.setImageResource(material.getIcon());
        holder.ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MaterialBrowseActivity.this,"已加入購物車",Toast.LENGTH_LONG).show();
            }
        });
        holder.ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(holder.heart){
                    case 0:
                        holder.ivHeart.setImageResource(R.drawable.hearted);
                        Toast.makeText(MaterialBrowseActivity.this,"已加入收藏",Toast.LENGTH_SHORT).show();
                        holder.heart = 1;
                        break;
                    case 1:
                        holder.ivHeart.setImageResource(R.drawable.heart);
                        Toast.makeText(MaterialBrowseActivity.this,"已取消收藏",Toast.LENGTH_SHORT).show();
                        holder.heart = 0;
                        break;
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MaterialBrowseActivity.this,material.getName()+"查看詳情",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MaterialBrowseActivity.this, MaterialDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("material", material);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return materialList.size();}
}

}
