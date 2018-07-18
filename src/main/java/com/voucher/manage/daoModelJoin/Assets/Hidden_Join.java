package com.voucher.manage.daoModelJoin.Assets;

import java.io.Serializable;
import java.util.Date;

import com.voucher.manage.daoSQL.annotations.SQLDateTime;
import com.voucher.manage.daoSQL.annotations.SQLDouble;
import com.voucher.manage.daoSQL.annotations.SQLInteger;
import com.voucher.manage.daoSQL.annotations.SQLString;

public class Hidden_Join implements Serializable{

	private static final long serialVersionUID = 1L;

    @SQLInteger(name="id")
	private Integer id;

    @SQLString(name="GUID")
	private String GUID;

    @SQLInteger(name="exist")
	private Integer exist;
    
    @SQLString(name="name")
	private String name;

    @SQLString(name="hidden_level")
	private String hidden_level;

    @SQLString(name="detail")
	private String detail;

    @SQLString(name="progress")
	private String progress;
    
    @SQLDateTime(name="happen_time")
	private Date happen_time;

    @SQLString(name="principal")
	private String principal;

    @SQLString(name="type")
	private String type;

    @SQLString(name="state")
	private String state;

    @SQLString(name="remark")
	private String remark;

    @SQLDateTime(name="update_time")
   	private Date update_time;
    
    @SQLDateTime(name="date")
	private Date date;
    
    @SQLString(name="campusAdmin")
	private String campusAdmin;

    @SQLString(name="terminal")
	private String terminal;
    
    @SQLString(name="ManageRegion")
	private String ManageRegion;
    
    /*
     * Hidden_Level
     */
    
    @SQLString(name="level_text")
	private String level_text;
    
    /*
     * Hidden_Type
     */
    
    @SQLString(name="hidden_type")
	private String hidden_type;
    
    /*
     * Hidden_User
     */
    @SQLString(name="principal_name")
   	private String principal_name;

    @SQLString(name="business")
   	private String business;
    
    /*
     * Position
     */   
    @SQLDouble(name="lng")
	private Double lng;

    @SQLDouble(name="lat")
	private Double lat;
    
    /*
     * WeiXin_User
     */
    @SQLString(name="phone")
	private String phone;

    @SQLString(name="user_name")
	private String user_name;
    
    public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return id;
	}

	public void setGUID(String GUID){
		this.GUID = GUID;
	}

	public String getGUID(){
		return GUID;
	}

	public void setExist(Integer exist){
		this.exist = exist;
	}

	public Integer getExist(){
		return exist;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setHidden_level(String hidden_level){
		this.hidden_level = hidden_level;
	}

	public String getHidden_level(){
		return hidden_level;
	}

	public void setDetail(String detail){
		this.detail = detail;
	}

	public String getDetail(){
		return detail;
	}

	public void setProgress(String progress){
		this.progress = progress;
	}

	public String getProgress(){
		return progress;
	}
	
	public void setHappen_time(Date happen_time){
		this.happen_time = happen_time;
	}

	public Date getHappen_time(){
		return happen_time;
	}

	public void setPrincipal(String principal){
		this.principal = principal;
	}

	public String getPrincipal(){
		return principal;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	public void setUpdate_time(Date update_time){
		this.update_time = update_time;
	}

	public Date getUpdate_time(){
		return update_time;
	}
	
	public void setDate(Date date){
		this.date = date;
	}

	public Date getDate(){
		return date;
	}
    
	public void setCampusAdmin(String campusAdmin){
		this.campusAdmin = campusAdmin;
	}

	public String getCampusAdmin(){
		return campusAdmin;
	}

	public void setTerminal(String terminal){
		this.terminal = terminal;
	}

	public String getTerminal(){
		return terminal;
	}
	
	public void setManageRegion(String ManageRegion){
		this.ManageRegion = ManageRegion;
	}

	public String getManageRegion(){
		return ManageRegion;
	}
	
	/*
     * Hidden_Level
     */
	
	public void setLevel_text(String level_text){
		this.level_text = level_text;
	}

	public String getLevel_text(){
		return level_text;
	}
	
	public void setHidden_type(String hidden_type){
		this.hidden_type = hidden_type;
	}

	public String getHidden_type(){
		return hidden_type;
	}
	

	public void setPrincipal_name(String principal_name){
		this.principal_name = principal_name;
	}

	public String getPrincipal_name(){
		return principal_name;
	}

	public void setBusiness(String business){
		this.business = business;
	}

	public String getBusiness(){
		return business;
	}
	
	public void setLng(Double lng){
		this.lng = lng;
	}

	public Double getLng(){
		return lng;
	}

	public void setLat(Double lat){
		this.lat = lat;
	}

	public Double getLat(){
		return lat;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	
}
