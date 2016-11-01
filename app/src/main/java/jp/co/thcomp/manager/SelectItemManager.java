package jp.co.thcomp.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.thcomp.android_selectitemmanager.MasterData;

public class SelectItemManager {
    public interface MasterDataItem{
        public String getGroupId();
        public String getItemId();
    }

    public interface MasterDataItemProvider{
        public int getMasterDataItemCount();
        public int getGroupCount();
        public int getGroupItemCount(int groupPosition);
        public MasterDataItem getMasterDataItem(int groupPosition, int itemPosition);
        public MasterDataItem getMasterDataItem(String groupId, String itemId);
    }

    private MasterDataItemProvider mMasterDataItemProvider;
    private HashMap<String, HashMap<String, MasterDataItem>> mSelectItemMap = new HashMap<String, HashMap<String, MasterDataItem>>();
    private HashMap<String, HashMap<String, MasterDataItem>> mUnSelectItemMap = new HashMap<String, HashMap<String, MasterDataItem>>();
    private HashMap<String, HashMap<String, MasterDataItem>> mTargetItemMap = mSelectItemMap;

    public SelectItemManager(MasterDataItemProvider provider){
        if(provider == null){
            throw new NullPointerException("provider == null");
        }

        mMasterDataItemProvider = provider;
    }

    public synchronized void selectItem(String groupId, String itemId){
        if (mTargetItemMap == mSelectItemMap) {
            HashMap<String, MasterDataItem> targetGroupMap = mTargetItemMap.get(groupId);

            if(targetGroupMap == null){
                targetGroupMap = new HashMap<String, MasterDataItem>();
                mTargetItemMap.put(groupId, targetGroupMap);
            }

            targetGroupMap.put(itemId, mMasterDataItemProvider.getMasterDataItem(groupId, itemId));
        } else {
            // 非選択になるので、削除
            HashMap<String, MasterDataItem> targetGroupMap = mTargetItemMap.get(groupId);
            if(targetGroupMap != null) {
                targetGroupMap.remove(itemId);
            }
        }
    }

    public synchronized void unselectItem(String groupId, String itemId){
        if (mTargetItemMap == mSelectItemMap) {
            HashMap<String, MasterDataItem> targetGroupMap = mTargetItemMap.get(groupId);
            if(targetGroupMap != null) {
                targetGroupMap.remove(itemId);
            }
        } else {
            // 非選択になるので、追加
            HashMap<String, MasterDataItem> targetGroupMap = mTargetItemMap.get(groupId);
            if(targetGroupMap == null) {
                targetGroupMap = new HashMap<String, MasterDataItem>();
                mTargetItemMap.put(groupId, targetGroupMap);
            }

            targetGroupMap.put(itemId, mMasterDataItemProvider.getMasterDataItem(groupId, itemId));
        }
    }

    public synchronized void selectAllItem(){
        mSelectItemMap.clear();
        mUnSelectItemMap.clear();

        // 全選択されたので、これからは選択されていないアイテムを管理していく
        mTargetItemMap = mUnSelectItemMap;
    }

    public synchronized void unselectAllItem(){
        mSelectItemMap.clear();
        mUnSelectItemMap.clear();

        // 全選択されたので、これからは選択されているアイテムを管理していく
        mTargetItemMap = mSelectItemMap;
    }

    public synchronized boolean isSelected(String groupId, String itemId){
        boolean ret = false;

        if(mTargetItemMap == mSelectItemMap){
            HashMap<String, MasterDataItem> itemMap = mTargetItemMap.get(groupId);
            if(itemMap != null && itemMap.size() > 0){
                ret = itemMap.containsKey(itemId);
            }
        }else{
            HashMap<String, MasterDataItem> itemMap = mTargetItemMap.get(groupId);
            if(itemMap != null && itemMap.size() > 0){
                // 非選択アイテムとして管理されている場合は選択されていないことになるので、containsの逆を返却
                ret = !itemMap.containsKey(itemId);
            }else{
                // 非選択アイテムとして管理されていないので選択されていることになる
                ret = true;
            }
        }

        return ret;
    }

    public synchronized List<MasterDataItem> getAllSelectItem(){
        ArrayList<MasterDataItem> selectItemList = new ArrayList<MasterDataItem>();

        if(mTargetItemMap == mSelectItemMap){
            // mTargetItemMapに設定されているものをそのまま返却
            Set<Map.Entry<String, HashMap<String, MasterDataItem>>> itemMapEntrySet = mTargetItemMap.entrySet();
            Iterator<Map.Entry<String, HashMap<String, MasterDataItem>>> itemMapIterator = itemMapEntrySet.iterator();

            while (itemMapIterator.hasNext()){
                HashMap<String, MasterDataItem> itemMap = itemMapIterator.next().getValue();
                if(itemMap != null){
                    Set<Map.Entry<String, MasterDataItem>> itemEntrySet = itemMap.entrySet();
                    Iterator<Map.Entry<String, MasterDataItem>> itemIterator = itemEntrySet.iterator();

                    while (itemIterator.hasNext()){
                        selectItemList.add(itemIterator.next().getValue());
                    }
                }
            }
        }else{
            // mTargetItemMapに設定されていないものを返却
            for(int groupIndex=0, groupCount=mMasterDataItemProvider.getGroupCount(); groupIndex<groupCount; groupIndex++){
                for(int itemIndex=0, itemCount=mMasterDataItemProvider.getGroupItemCount(groupIndex); itemIndex<itemCount; itemIndex++){
                    MasterDataItem tempItem = mMasterDataItemProvider.getMasterDataItem(groupIndex, itemIndex);
                    String groupId = tempItem.getGroupId();
                    String itemId = tempItem.getItemId();

                    HashMap<String, MasterDataItem> tempItemMap = mTargetItemMap.get(groupId);
                    if(tempItemMap != null && tempItemMap.size() > 0){
                        if(!tempItemMap.containsKey(itemId)){
                            // mUnSelectItemMapに設定されていないので、返却するデータとして追加
                            selectItemList.add(tempItem);
                        }
                    }else{
                        selectItemList.add(tempItem);
                    }
                }
            }
        }

        return selectItemList;
    }
}
