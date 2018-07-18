package com.voucher.manage.daoModelJoin.Assets;

import java.io.Serializable;
import java.util.Date;

import com.voucher.manage.daoSQL.annotations.SQLDateTime;
import com.voucher.manage.daoSQL.annotations.SQLDouble;
import com.voucher.manage.daoSQL.annotations.SQLInteger;
import com.voucher.manage.daoSQL.annotations.SQLString;

public class Hidden_Neaten_Join implements Serializable{

	    @SQLInteger(name="id")
		private Integer id;

	    @SQLString(name="GUID")
		private String GUID;

	    @SQLInteger(name="exist")
		private Integer exist;
	    
	    @SQLString(name="neaten_id")
		private String neaten_id;

	    @SQLString(name="neaten_name")
		private String neaten_name;

	    @SQLString(name="principal")
		private String principal;

	    @SQLDateTime(name="happen_time")
		private Date happen_time;

	    @SQLString(name="neaten_instance")
		private String neaten_instance;

	    @SQLDateTime(name="update_time")
		private Date update_time;
	    
	    @SQLDateTime(name="date")
		private Date date;

	    @SQLString(name="remark")
		private String remark;

	    @SQLString(name="campusAdmin")
		private String campusAdmin;

	    @SQLString(name="terminal")
		private String terminal;
	    
	    /*
	     * Hidden
	     */
	    
	    @SQLString(name="name")
		private String name;
	    
	    @SQLInteger(name="hidden_level")
		private Integer hidden_level;	    
	    
	    @SQLString(name="detail")
		private String detail;
	    
	    @SQLString(name="progress")
		private String progress;
	    
	    @SQLInteger(name="type")
		private Integer type;
	    
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

		
		public void setNeaten_id(String neaten_id){
			this.neaten_id = neaten_id;
		}

		public String getNeaten_id(){
			return neaten_id;
		}

		public void setNeaten_name(String neaten_name){
			this.neaten_name = neaten_name;
		}

		public String getNeaten_name(){
			return neaten_name;
		}

		public void setPrincipal(String principal){
			this.principal = principal;
		}

		public String getPrincipal(){
			return principal;
		}


		public void setNeaten_instance(String neaten_instance){
			this.neaten_instance = neaten_instance;
		}

		public String getNeaten_instance(){
			return neaten_instance;
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

		public void setRemark(String remark){
			this.remark = remark;
		}

		public String getRemark(){
			return remark;
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
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getHidden_level() {
			return hidden_level;
		}

		public void setHidden_level(Integer hidden_level) {
			this.hidden_level = hidden_level;
		}

		public Date getHappen_time() {
			return happen_time;
		}

		public void setHappen_time(Date happen_time) {
			this.happen_time = happen_time;
		}

		public String getDetail() {
			return detail;
		}

		public void setDetail(String detail) {
			this.detail = detail;
		}

		public String getProgress() {
			return progress;
		}

		public void setProgress(String progress) {
			this.progress = progress;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
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
