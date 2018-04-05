package com.sadda.adda.panchratan.saddaadda.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sadda.adda.panchratan.saddaadda.R;
import com.sadda.adda.panchratan.saddaadda.database.DBHelper;
import com.sadda.adda.panchratan.saddaadda.objects.Item;
import com.sadda.adda.panchratan.saddaadda.util.Utils;

import java.util.List;

/**
 * Created by user on 14-10-2017.
 */

public class ShowStatisticsFragment extends Fragment {
    private TextView txt_bhuvnesh, txt_kamlesh, txt_akshay, txt_vinay, txt_jatin, txt_sandeep,txt_total_amount;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_statistics,container,false);
        txt_bhuvnesh = (TextView) view.findViewById(R.id.txt_bhuvnesh_amount);
        txt_kamlesh = (TextView) view.findViewById(R.id.txt_kamlesh_amount);
        txt_akshay = (TextView) view.findViewById(R.id.txt_akshay_amount);
        txt_vinay = (TextView) view.findViewById(R.id.txt_vinay_amount);
        txt_jatin= (TextView) view.findViewById(R.id.txt_jatin_amount);
        txt_total_amount = (TextView) view.findViewById(R.id.txt_total_stat_amount);
        txt_sandeep= (TextView) view.findViewById(R.id.txt_sandeep_amount);
        DBHelper dbHelper = new DBHelper(getActivity());
        List<Item> itemList = dbHelper.getAllItems();
        loadAllData(itemList);
        return view;
    }

    private void loadAllData(List<Item> itemList) {
        int[] array = Utils.getIndvidualAmounts(itemList);
        txt_bhuvnesh.setText(Integer.toString(array[0]));
        txt_kamlesh.setText(Integer.toString(array[1]));
        txt_akshay.setText(Integer.toString(array[2]));
        txt_vinay.setText(Integer.toString(array[3]));
        txt_jatin.setText(Integer.toString(array[4]));
        txt_sandeep.setText(Integer.toString(array[5]));
        txt_total_amount.setText(Integer.toString(Utils.getTotalAmount(itemList)));
    }
}
