package com.djandroid.jdroid.materialdesign.ClientLibrary.Parameter.Task;

import java.util.ArrayList;
import java.util.List;
import com.djandroid.jdroid.materialdesign.ClientLibrary.Parameter.*;

/**
 * Created by Jimmy on 2016/10/10.
 */
public class TaskItem {
    private String itemId;
    private String item;
    private String compliance; //是否通过
    private String remark;
    private String violationLevel;
    private String explanation; //标题下面
    private String auditStatus; //no use now
    private List<String> picturePathList;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Compliance getCompliance() {
        return Compliance.valueOf(compliance);
    }

    public void setCompliance(Compliance compliance) {
        this.compliance = compliance.toString();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ViolationLevel getViolationLevel() {
        return ViolationLevel.valueOf(violationLevel);
    }

    public void setViolationLevel(ViolationLevel violationLevel) {
        this.violationLevel = violationLevel.toString();
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public AuditStatus getAuditStatus() {
        return AuditStatus.valueOf(auditStatus);
    }

    public void setAuditStatus(AuditStatus auditStatus) {
        this.auditStatus = auditStatus.toString();
    }

    public List<String> getPicturePathList() {
        return picturePathList;
    }

    public void setPicturePathList(List<String> picturePathList) {
        this.picturePathList = picturePathList;
    }

    public void addPicture(String picture)
    {
        if (this.picturePathList == null) this.picturePathList = new ArrayList<String>();
        this.picturePathList.add(picture);
    }

    public void removePicture(String picture)
    {
        this.picturePathList.remove(picture);
    }

    public TaskItem(){}

    public TaskItem(String _itemId, String _item, ViolationLevel _violationLevel, String _explanation)
    {
        this.itemId = _itemId;
        this.item = _item;
        this.compliance = Compliance.None.toString();
        this.remark = "";
        this.violationLevel = _violationLevel.toString();
        this.explanation = _explanation;
        this.auditStatus = AuditStatus.None.toString();
    }
}
