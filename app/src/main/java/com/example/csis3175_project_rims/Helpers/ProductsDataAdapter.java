package com.example.csis3175_project_rims.Helpers;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class ProductsDataAdapter extends TableDataAdapter<ProductsHelperClass> {

    public ProductsDataAdapter(Context context, List<ProductsHelperClass> data){
        super(context,data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ProductsHelperClass productsHelperClass = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex){
            case 0:
                renderedView = renderedSKU(productsHelperClass);
                break;
            case 1:
                renderedView = renderedName(productsHelperClass);
                break;
            case 2:
                renderedView = renderedQuantity(productsHelperClass);
                break;
            case 3:
                renderedView = renderedIsMatch(productsHelperClass);
        }
        return renderedView;
    }

    private View renderedSKU(final ProductsHelperClass productsHelperClass){
        return renderString(productsHelperClass.getSku());
    }

    private View renderedName(final ProductsHelperClass productsHelperClass){
        return renderString(productsHelperClass.getName());
    }

    private View renderedQuantity(final ProductsHelperClass productsHelperClass){
        return renderString(String.valueOf(productsHelperClass.getQuantity()));
    }


    private View renderedIsMatch(final ProductsHelperClass productsHelperClass){
        return productsHelperClass.getIsMatch()? renderString("Yes"):renderString("No");
    }
    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setTextColor(Color.BLACK);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(16);
        return textView;
    }
}
