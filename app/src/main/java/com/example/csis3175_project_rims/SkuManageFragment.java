package com.example.csis3175_project_rims;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.Collections;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SkuManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkuManageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SkuManageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SkuManageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SkuManageFragment newInstance(String param1, String param2) {
        SkuManageFragment fragment = new SkuManageFragment();
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

        View view = inflater.inflate(R.layout.fragment_sku_manage,container,false);

        Button btnAddSku = (Button)view.findViewById(R.id.btnAddSku);
        TextView inputSkuCode = (TextView)view.findViewById(R.id.inputSkuCode);
        TextView inputProductName = (TextView)view.findViewById(R.id.inputProductName);

        addSKU("Z_DummySKU".toString(),"Z_DummyName");


        btnAddSku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"SKU "+inputSkuCode.getText().toString()+" is added!", Toast.LENGTH_SHORT).show();
                addSKU(inputSkuCode.getText().toString(),inputProductName.getText().toString());
            }
        });

        TableView tableProducts =  view.findViewById(R.id.table_data_view);
        String[] headers={"SKU","Name","Quantity"};

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Products");
        Query referenceQuery = reference.orderByKey();

        ArrayList<ProductsHelperClass> productList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    productList.add(data.getValue(ProductsHelperClass.class));
                }
                //String[][] arrayTmp = productList.stream()
                //        .map(p -> new String[] {p.getSku(), p.getName(), String.valueOf(p.getQuantity())})
                //        .toArray(String[][]::new);

                tableProducts.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(),headers));
                //tableProducts.setDataAdapter(new SimpleTableDataAdapter(getActivity(), arrayTmp));
                tableProducts.setDataAdapter(new ProductsDataAdapter(getActivity(),productList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tableProducts.addDataClickListener(new ProductClickListener());


        // Inflate the layout for this fragment
        return view;
    }

    private class ProductClickListener implements TableDataClickListener<ProductsHelperClass> {
        @Override
        public void onDataClicked(int rowIndex, ProductsHelperClass clickedProduct) {
            //String clickedProductString = clickedProduct.getSku() + " " + clickedProduct.getName();
            //Toast.makeText(getActivity(), "rowIndex = "+rowIndex, Toast.LENGTH_SHORT).show();
            delete(clickedProduct.getSku());
        }
    }

    private void addSKU(String sku, String name) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Products");

        ProductsHelperClass addSKU = new ProductsHelperClass(sku,name,"Testing desc of "+name,0);
        reference.child(sku).setValue(addSKU);
    }

    private void delete(String sku){
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Products");
        Query referenceQuery = reference.child(sku);
        referenceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}