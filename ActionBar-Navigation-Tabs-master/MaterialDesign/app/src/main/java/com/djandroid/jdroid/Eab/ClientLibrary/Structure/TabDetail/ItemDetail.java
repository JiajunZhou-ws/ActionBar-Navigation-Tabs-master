package com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail;

import java.util.List;

/**
 * Created by Jimmy on 2016/12/6.
 */
public class ItemDetail
{
    public String itemId;
    public String itemDetail;
    public ScoreType scoreType;
    public int scoreValue;
    public ViolationLevel violationLevel;
    public String itemExplanation;
    public AuditStatus auditStatus;
    public List<PictureDetail> goodPictureList;
    public List<PictureDetail> badPictureList;
}
