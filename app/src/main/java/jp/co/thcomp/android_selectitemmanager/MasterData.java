package jp.co.thcomp.android_selectitemmanager;

import jp.co.thcomp.manager.SelectItemManager;

/**
 * Created by H_Tatsuguchi on 2016/11/01.
 */

public class MasterData implements SelectItemManager.MasterDataItemProvider{
    public static class SubMasterData implements SelectItemManager.MasterDataItem{
        private int mGroupId;
        private int mItemId;
        private String mContent;

        public SubMasterData(int groupId, int itemId, String content){
            mGroupId = groupId;
            mItemId = itemId;
            mContent = content;
        }

        @Override
        public String getGroupId() {
            return String.valueOf(mGroupId);
        }

        @Override
        public String getItemId() {
            return String.valueOf(mItemId);
        }

        public String getContent() {
            return mContent;
        }
    }

    public static final SubMasterData[][] sMasterDataArray = new SubMasterData[][]{
            {new SubMasterData(0, 0, "content_0-0"), new SubMasterData(0, 1, "content_0-1"), new SubMasterData(0, 2, "content_0-2"), new SubMasterData(0, 3, "content_0-3")},
            {new SubMasterData(1, 0, "content_1-0"), new SubMasterData(1, 1, "content_1-1"), new SubMasterData(1, 2, "content_1-2"), new SubMasterData(1, 3, "content_1-3")},
            {new SubMasterData(2, 0, "content_2-0"), new SubMasterData(2, 1, "content_2-1"), new SubMasterData(2, 2, "content_2-2"), new SubMasterData(2, 3, "content_2-3")},
            {new SubMasterData(3, 0, "content_3-0"), new SubMasterData(3, 1, "content_3-1"), new SubMasterData(3, 2, "content_3-2"), new SubMasterData(3, 3, "content_3-3")},
            {new SubMasterData(4, 0, "content_4-0"), new SubMasterData(4, 1, "content_4-1"), new SubMasterData(4, 2, "content_4-2"), new SubMasterData(4, 3, "content_4-3")},
    };

    @Override
    public int getMasterDataItemCount() {
        int ret = 0;
        for(int i=0, size=sMasterDataArray.length; i<size; i++){
            ret += sMasterDataArray[i].length;
        }
        return ret;
    }

    @Override
    public int getGroupCount() {
        return sMasterDataArray.length;
    }

    @Override
    public int getGroupItemCount(int groupPosition) {
        return sMasterDataArray[groupPosition].length;
    }

    @Override
    public SelectItemManager.MasterDataItem getMasterDataItem(int groupPosition, int itemPosition) {
        SelectItemManager.MasterDataItem ret = null;

        try{
            ret = sMasterDataArray[groupPosition][itemPosition];
        }catch (Exception e){
            // nullで返却
        }

        return ret;
    }

    @Override
    public SelectItemManager.MasterDataItem getMasterDataItem(String groupId, String itemId) {
        SelectItemManager.MasterDataItem ret = null;

        try {
            int groupIdInt = Integer.parseInt(groupId);
            int itemIdInt = Integer.parseInt(itemId);

            ret = sMasterDataArray[groupIdInt][itemIdInt];
        }catch (NumberFormatException e){
            // nullで返却
        }

        return ret;
    }
}
