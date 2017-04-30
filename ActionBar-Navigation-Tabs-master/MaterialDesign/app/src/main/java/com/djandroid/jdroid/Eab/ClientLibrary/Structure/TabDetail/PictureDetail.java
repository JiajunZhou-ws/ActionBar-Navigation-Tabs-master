package com.djandroid.jdroid.Eab.ClientLibrary.Structure.TabDetail;

/**
 * Created by Jimmy on 2016/12/6.
 */
public class PictureDetail {
    public String pictureName;
    public String pictureExplanation;
    public String pictureConsequence; //缺陷后果
    public String pictureSuggestion;  //解决建议
    public ViolationLevel pictureViolation = ViolationLevel.None;
}
