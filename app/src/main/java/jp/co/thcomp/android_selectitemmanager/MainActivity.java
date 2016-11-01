package jp.co.thcomp.android_selectitemmanager;

import android.content.DialogInterface;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import jp.co.thcomp.manager.SelectItemManager;

public class MainActivity extends AppCompatActivity {
    private static final int ViewTagGroupId = "ViewTagGroupId".hashCode();
    private static final int ViewTagItemId = "ViewTagItemId".hashCode();
    private MasterData mMasterData = new MasterData();
    private SelectItemManager mSelectItemManager = new SelectItemManager(mMasterData);
    private int mLastGroupId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.b_select_all).setOnClickListener(mClickListener);
        findViewById(R.id.b_unselect_all).setOnClickListener(mClickListener);
        findViewById(R.id.b_update_select_item_list).setOnClickListener(mClickListener);

        showGroupList();
        showGroupItem(0);
    }

    private void showGroupList(){
        LinearLayout llGroupList = (LinearLayout) findViewById(R.id.ll_group_list);
        llGroupList.removeAllViews();

        for(int i=0, size=MasterData.sMasterDataArray.length; i<size; i++){
            TextView tempTextView = new TextView(this);
            tempTextView.setText("group " + i);
            tempTextView.setTag(ViewTagGroupId, i);
            tempTextView.setOnClickListener(mGroupClickListener);
            llGroupList.addView(tempTextView);
        }
    }

    private void showGroupItem(int groupId){
        LinearLayout llItemList = (LinearLayout) findViewById(R.id.ll_item_list);
        llItemList.removeAllViews();

        for(int i=0, size=MasterData.sMasterDataArray[groupId].length; i<size; i++){
            SwitchCompat tempSwitch = new SwitchCompat(this);
            tempSwitch.setText(MasterData.sMasterDataArray[groupId][i].getContent());
            tempSwitch.setTag(ViewTagGroupId, groupId);
            tempSwitch.setTag(ViewTagItemId, i);
            tempSwitch.setChecked(mSelectItemManager.isSelected(String.valueOf(groupId), String.valueOf(i)));
            tempSwitch.setOnCheckedChangeListener(mSwitchChangedListener);
            llItemList.addView(tempSwitch);
        }
    }

    private void showSelectItemList(){
        LinearLayout llSelectItemList = (LinearLayout) findViewById(R.id.ll_select_item_list);
        llSelectItemList.removeAllViews();

        List<SelectItemManager.MasterDataItem> selectItemList = mSelectItemManager.getAllSelectItem();
        if(selectItemList != null && selectItemList.size() > 0){
            for(SelectItemManager.MasterDataItem selectItem : selectItemList){
                TextView itemTextView = new TextView(this);
                itemTextView.setText(((MasterData.SubMasterData)selectItem).getContent());
                llSelectItemList.addView(itemTextView);
            }
        }
    }

    private CompoundButton.OnCheckedChangeListener mSwitchChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int groupId = (int) buttonView.getTag(ViewTagGroupId);
            int itemId = (int) buttonView.getTag(ViewTagItemId);

            if(isChecked){
                mSelectItemManager.selectItem(String.valueOf(groupId), String.valueOf(itemId));
            }else{
                mSelectItemManager.unselectItem(String.valueOf(groupId), String.valueOf(itemId));
            }
        }
    };

    private View.OnClickListener mGroupClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLastGroupId = (int) v.getTag(ViewTagGroupId);
            showGroupItem(mLastGroupId);
        }
    };

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            switch (id){
                case R.id.b_select_all:
                    mSelectItemManager.selectAllItem();
                    showGroupItem(mLastGroupId);
                    break;
                case R.id.b_unselect_all:
                    mSelectItemManager.unselectAllItem();
                    showGroupItem(mLastGroupId);
                    break;
                case R.id.b_update_select_item_list:
                    showSelectItemList();
                    break;
            }
        }
    };
}
