package com.example.csis3175_project_rims;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
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
    Button btnAddSku;
    Button btnSearch;
    TextView inputSkuCode;
    TextView inputProductName;
    TextView searchSku;
    ArrayList<ProductsHelperClass> productList = new ArrayList<>();
    TableView tableProducts;
    String[] headers={"SKU","Name","Quantity"};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SkuManageFragment() {
        // Required empty public constructor
    }

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
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Products");

        btnAddSku = (Button)view.findViewById(R.id.btnAddSku);
        btnSearch = (Button)view.findViewById(R.id.btnSearch);
        inputSkuCode = (TextView)view.findViewById(R.id.inputSkuCode);
        inputProductName = (TextView)view.findViewById(R.id.inputProductName);
        searchSku = (TextView)view.findViewById(R.id.searchSku);

        tableProducts =  view.findViewById(R.id.table_data_view);

        drawTable();
        //addSKU("Z_DummySKU","Z_DummyName");

        btnAddSku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"SKU "+inputSkuCode.getText().toString()+" is added!", Toast.LENGTH_SHORT).show();
                addSKU(inputSkuCode.getText().toString(),inputProductName.getText().toString());
                drawTable();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String target = searchSku.getText().toString();
                searchSku.setText("");
                Query referenceQuery = reference.orderByChild("sku").equalTo(target);

                referenceQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            productList.clear();
                            //Toast.makeText(getActivity(), "exists = "+productList, Toast.LENGTH_SHORT).show();
                            for(DataSnapshot data : snapshot.getChildren()){
                                productList.add(data.getValue(ProductsHelperClass.class));
                            }
                            tableProducts.setDataAdapter(new ProductsDataAdapter(getActivity(),productList));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        tableProducts.addDataClickListener(new ProductClickListener());
        tableProducts.addDataLongClickListener(new ProductLongClickListener());
        // Inflate the layout for this fragment
        return view;
    }

    private class ProductClickListener implements TableDataClickListener<ProductsHelperClass> {
        @Override
        public void onDataClicked(int rowIndex, ProductsHelperClass clickedProduct) {
            //Toast.makeText(getActivity(), "rowIndex = "+rowIndex, Toast.LENGTH_SHORT).show();
            FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
            DatabaseReference reference = rootNode.getReference("Products");
            int tmpQty = clickedProduct.getQuantity()+1;
            reference.child(clickedProduct.getSku()).child("quantity").setValue(tmpQty);
            //Toast.makeText(getActivity(), "tmpQty = "+tmpQty, Toast.LENGTH_SHORT).show();
            drawTable();
        }
    }

    private class ProductLongClickListener implements TableDataLongClickListener<ProductsHelperClass> {
        @Override
        public boolean onDataLongClicked(int rowIndex, ProductsHelperClass clickedProduct) {
            //Toast.makeText(getActivity(), "rowIndex = "+rowIndex, Toast.LENGTH_SHORT).show();
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            deleteSKU(clickedProduct.getSku());
                            drawTable();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Confirm to delete?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

            return true;
        }
    }

    private void drawTable(){
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Products");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    productList.add(data.getValue(ProductsHelperClass.class));
                }
                tableProducts.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(),headers));
                tableProducts.setDataAdapter(new ProductsDataAdapter(getActivity(),productList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addSKU(String sku, String name) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Products");

        ProductsHelperClass addSKU = new ProductsHelperClass(sku,name,"Testing desc of "+name,0);
        reference.child(sku).setValue(addSKU);
    }

    private void deleteSKU(String sku){
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