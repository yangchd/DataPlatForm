package com.yangchd.data.table;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author yangchd  2017/12/18.
 */
@Entity
@Table(name = "syn_table")
public class TableConfig {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    @Column(name = "id")
    private String id;
    private String name;
    private String description;
    /**************数据源配置**************/
    private String datato;
    private String datafrom;
    /*****************表配置***************/
    private String tableto;
    private String tablekey;
    private String tablefrom;
    @Column(length = 2000)
    private String jointable;
    @Column(length = 2000)
    private String whereto;
    @Column(length = 2000)
    private String wherefrom;
    /*****************列配置***************/
    @Column(length = 2000)
    private String columnto;
    @Column(length = 2000)
    private String columnfrom;
    @Column(length = 2000)
    private String relation;
    /***************同步统计***************/
    private String lasttime;
    private String timecost;
    private String computetime;
    private String sqlquerytime;
    private String sqlruntime;
    private String synmsg;
    /***************同步模式***************/
    private String synflag;
    /**同步模式（normal，middle，trigger）**/
    private String syntype;
    /**同步量（update,insert,delete）**/
    private String synvalue;
    /**日志是否开启**/
    private String logflag;
    /**遇到错误是否继续**/
    private String errorflag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatato() {
        return datato;
    }

    public void setDatato(String datato) {
        this.datato = datato;
    }

    public String getDatafrom() {
        return datafrom;
    }

    public void setDatafrom(String datafrom) {
        this.datafrom = datafrom;
    }

    public String getTableto() {
        return tableto;
    }

    public void setTableto(String tableto) {
        this.tableto = tableto;
    }

    public String getTablekey() {
        return tablekey;
    }

    public void setTablekey(String tablekey) {
        this.tablekey = tablekey;
    }

    public String getTablefrom() {
        return tablefrom;
    }

    public void setTablefrom(String tablefrom) {
        this.tablefrom = tablefrom;
    }

    public String getJointable() {
        return jointable;
    }

    public void setJointable(String jointable) {
        this.jointable = jointable;
    }

    public String getWhereto() {
        return whereto;
    }

    public void setWhereto(String whereto) {
        this.whereto = whereto;
    }

    public String getWherefrom() {
        return wherefrom;
    }

    public void setWherefrom(String wherefrom) {
        this.wherefrom = wherefrom;
    }

    public String getColumnto() {
        return columnto;
    }

    public void setColumnto(String columnto) {
        this.columnto = columnto;
    }

    public String getColumnfrom() {
        return columnfrom;
    }

    public void setColumnfrom(String columnfrom) {
        this.columnfrom = columnfrom;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }

    public String getTimecost() {
        return timecost;
    }

    public void setTimecost(String timecost) {
        this.timecost = timecost;
    }

    public String getComputetime() {
        return computetime;
    }

    public void setComputetime(String computetime) {
        this.computetime = computetime;
    }

    public String getSqlquerytime() {
        return sqlquerytime;
    }

    public void setSqlquerytime(String sqlquerytime) {
        this.sqlquerytime = sqlquerytime;
    }

    public String getSqlruntime() {
        return sqlruntime;
    }

    public void setSqlruntime(String sqlruntime) {
        this.sqlruntime = sqlruntime;
    }

    public String getSynmsg() {
        return synmsg;
    }

    public void setSynmsg(String synmsg) {
        this.synmsg = synmsg;
    }

    public String getSynflag() {
        return synflag;
    }

    public void setSynflag(String synflag) {
        this.synflag = synflag;
    }

    public String getSyntype() {
        return syntype;
    }

    public void setSyntype(String syntype) {
        this.syntype = syntype;
    }

    public String getSynvalue() {
        return synvalue;
    }

    public void setSynvalue(String synvalue) {
        this.synvalue = synvalue;
    }

    public String getLogflag() {
        return logflag;
    }

    public void setLogflag(String logflag) {
        this.logflag = logflag;
    }

    public String getErrorflag() {
        return errorflag;
    }

    public void setErrorflag(String errorflag) {
        this.errorflag = errorflag;
    }

    @Override
    public String toString() {
        return "TableConfig{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", datato='" + datato + '\'' +
                ", datafrom='" + datafrom + '\'' +
                ", tableto='" + tableto + '\'' +
                ", tablekey='" + tablekey + '\'' +
                ", tablefrom='" + tablefrom + '\'' +
                ", jointable='" + jointable + '\'' +
                ", whereto='" + whereto + '\'' +
                ", wherefrom='" + wherefrom + '\'' +
                ", columnto='" + columnto + '\'' +
                ", columnfrom='" + columnfrom + '\'' +
                ", relation='" + relation + '\'' +
                ", lasttime='" + lasttime + '\'' +
                ", timecost='" + timecost + '\'' +
                ", computetime='" + computetime + '\'' +
                ", sqlquerytime='" + sqlquerytime + '\'' +
                ", sqlruntime='" + sqlruntime + '\'' +
                ", synmsg='" + synmsg + '\'' +
                ", synflag='" + synflag + '\'' +
                ", syntype='" + syntype + '\'' +
                ", synvalue='" + synvalue + '\'' +
                ", logflag='" + logflag + '\'' +
                ", errorflag='" + errorflag + '\'' +
                '}';
    }
}
