package com.lin.toymall.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OmsOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  String id;
    private  String memberId;
    private  String orderCode;
    private Date createTime;
    private Date modifyTime;
    private BigDecimal  totalAmount;
    private BigDecimal finalAmount;
    private String receviceName;
    private String receviceCity;
    private String receviceDetailAddress;
    private String recevicePostCode;
    private String recevicePhone;
    private String receviceProvince;
    private String receviceRegion;
    private Date payTime;
    private Date postTime;
    private Date confirmTime;
    private String   status;
    private String note;

    private String memberName;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    @Transient
    private List<OmsOrderItem> omsOrderItems;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReceviceName() {
        return receviceName;
    }

    public void setReceviceName(String receviceName) {
        this.receviceName = receviceName;
    }

    public String getReceviceCity() {
        return receviceCity;
    }

    public void setReceviceCity(String receviceCity) {
        this.receviceCity = receviceCity;
    }

    public String getReceviceDetailAddress() {
        return receviceDetailAddress;
    }

    public void setReceviceDetailAddress(String receviceDetailAddress) {
        this.receviceDetailAddress = receviceDetailAddress;
    }

    public String getRecevicePostCode() {
        return recevicePostCode;
    }

    public void setRecevicePostCode(String recevicePostCode) {
        this.recevicePostCode = recevicePostCode;
    }

    public String getRecevicePhone() {
        return recevicePhone;
    }

    public void setRecevicePhone(String recevicePhone) {
        this.recevicePhone = recevicePhone;
    }

    public String getReceviceProvince() {
        return receviceProvince;
    }

    public void setReceviceProvince(String receviceProvince) {
        this.receviceProvince = receviceProvince;
    }

    public String getReceviceRegion() {
        return receviceRegion;
    }

    public void setReceviceRegion(String receviceRegion) {
        this.receviceRegion = receviceRegion;
    }

    public List<OmsOrderItem> getOmsOrderItems() {
        return omsOrderItems;
    }

    public void setOmsOrderItems(List<OmsOrderItem> omsOrderItems) {
        this.omsOrderItems = omsOrderItems;
    }

}
