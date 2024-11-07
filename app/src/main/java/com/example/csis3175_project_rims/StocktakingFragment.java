package com.example.csis3175_project_rims;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csis3175_project_rims.Helpers.ProductsDataAdapter;
import com.example.csis3175_project_rims.Helpers.ProductsHelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StocktakingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StocktakingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button btnFinalizeStocktake;
    TextView txtDiscrepancies;
    ArrayList<ProductsHelperClass> productList = new ArrayList<>();
    TableView tableProducts;
    String[] headers={"SKU","Name","Quantity","Is Match?"};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StocktakingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StocktakingFragment newInstance(String param1, String param2) {
        StocktakingFragment fragment = new StocktakingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stocktaking,container,false);
        tableProducts =  view.findViewById(R.id.table_data_view1);
        btnFinalizeStocktake = view.findViewById(R.id.btnFinalizeStocktake);
        txtDiscrepancies = view.findViewById(R.id.txtDiscrepancies);

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Products");
        Query referenceQuery = reference.orderByKey();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                int tmpInt =0;
                String tmpString="";
                for(DataSnapshot data : snapshot.getChildren()){
                    productList.add(data.getValue(ProductsHelperClass.class));
                    tmpString = data.child("isMatch").getValue().toString();
                    if(tmpString.equals("false")){
                        tmpInt++;
                    }
                }
                txtDiscrepancies.setText("Discrepancies: "+String.valueOf(tmpInt));
                //Toast.makeText(getActivity(),"Get "+tmpString, Toast.LENGTH_SHORT).show();
                tableProducts.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(),headers));
                tableProducts.setDataAdapter(new ProductsDataAdapter(getActivity(),productList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btnFinalizeStocktake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                DatabaseReference reference = rootNode.getReference("Products");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()){
                            String tmpKey = data.getKey();
                            reference.child(tmpKey).child("isMatch").setValue(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        tableProducts.addDataClickListener(new ProductClickListener());
        return view;
    }

    private class ProductClickListener implements TableDataClickListener<ProductsHelperClass> {
        @Override
        public void onDataClicked(int rowIndex, ProductsHelperClass clickedProduct) {
            //Toast.makeText(getActivity(), "rowIndex = "+rowIndex, Toast.LENGTH_SHORT).show();
            FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
            DatabaseReference reference = rootNode.getReference("Products");
            reference.child(clickedProduct.getSku()).child("isMatch").setValue(!clickedProduct.getIsMatch());
        }
    }

}