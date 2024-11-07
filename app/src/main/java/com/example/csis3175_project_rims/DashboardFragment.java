package com.example.csis3175_project_rims;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<ProductsHelperClass> productList = new ArrayList<>();
    TableView tableProducts;
    String[] headers={"SKU","Name","Quantity"};
    TextView txtTotalView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard,container,false);
        tableProducts =  view.findViewById(R.id.table_data_view2);
        txtTotalView = view.findViewById(R.id.txtTotalView);
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Products");
        Query referenceQuery = reference.orderByChild("quantity").endAt(9);

        referenceQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    productList.clear();
                    //Toast.makeText(getActivity(), "exists = "+productList, Toast.LENGTH_SHORT).show();
                    for(DataSnapshot data : snapshot.getChildren()){
                        productList.add(data.getValue(ProductsHelperClass.class));
                    }
                    tableProducts.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(),headers));
                    tableProducts.setDataAdapter(new ProductsDataAdapter(getActivity(),productList));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //productList.clear();
                int tmpInt =0;
                for(DataSnapshot data : snapshot.getChildren()){
                    tmpInt += data.getValue(ProductsHelperClass.class).getQuantity();
                }
                txtTotalView.setText(String.valueOf(tmpInt));
                //Toast.makeText(getActivity(),"Get "+tmpString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }
}