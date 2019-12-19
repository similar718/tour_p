package cn.xmzt.www.selfdrivingtools.bean;

import java.util.List;

public class ScenicFeedBackTypeBean {

    /**
     * id : 35
     * keyName : scenic_question_type_map
     * keyValue : 1
     * des : 问题类型
     * parentKey : scenic_question_type
     * sort : 1
     * children : [{"id":39,"keyName":"scenic_question_type_map_1","keyValue":"1","des":"景点信息有误","parentKey":"scenic_question_type_map","sort":"1"},{"id":40,"keyName":"scenic_question_type_map_2","keyValue":"2","des":"定位不准","parentKey":"scenic_question_type_map","sort":"2"},{"id":41,"keyName":"scenic_question_type_map_3","keyValue":"3","des":"缺少景点","parentKey":"scenic_question_type_map","sort":"3"},{"id":42,"keyName":"scenic_question_type_map_4","keyValue":"4","des":"路线错误","parentKey":"scenic_question_type_map","sort":"4"}]
     */

    private int id;
    private String keyName;
    private String keyValue;
    private String des;
    private String parentKey;
    private String sort;
    private List<ChildrenBean> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }

    public static class ChildrenBean {
        /**
         * id : 39
         * keyName : scenic_question_type_map_1
         * keyValue : 1
         * des : 景点信息有误
         * parentKey : scenic_question_type_map
         * sort : 1
         */

        private int id;
        private String keyName;
        private String keyValue;
        private String des;
        private String parentKey;
        private String sort;
        private boolean is_Checked = false;

        public boolean isIs_Checked() {
            return is_Checked;
        }

        public void setIs_Checked(boolean is_Checked) {
            this.is_Checked = is_Checked;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKeyName() {
            return keyName;
        }

        public void setKeyName(String keyName) {
            this.keyName = keyName;
        }

        public String getKeyValue() {
            return keyValue;
        }

        public void setKeyValue(String keyValue) {
            this.keyValue = keyValue;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getParentKey() {
            return parentKey;
        }

        public void setParentKey(String parentKey) {
            this.parentKey = parentKey;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
    }
}
