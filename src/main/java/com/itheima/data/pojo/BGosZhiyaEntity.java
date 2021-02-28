package com.itheima.data.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zzj
 * @since 2021-02-22
 */
@TableName("b_gos_zhiya")
public class BGosZhiyaEntity extends Model<BGosZhiyaEntity> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @TableField("txHash")
    private String txHash;

    private Integer number;

    @TableField("fromAddr")
    private String fromAddr;

    @TableField("toAddr")
    private String toAddr;

    private String value;

    @TableField("valueToString")
    private BigDecimal valueToString;

    @TableField("tokenName")
    private String tokenName;

    @TableField("tokenSymbol")
    private String tokenSymbol;

    @TableField("tokenType")
    private Integer tokenType;

    private Integer decimals;

    private LocalDateTime timestamp;

    private String address;

    @TableField("logIndex")
    private String logIndex;

    private String outOrIn;

    /**
     * 交易还是流动性  true-交易  false-流动性
     */
    @TableField(exist = false)
    private  boolean isTranOrLp;

    public boolean isTranOrLp() {
        return isTranOrLp;
    }

    public void setTranOrLp(boolean tranOrLp) {
        isTranOrLp = tranOrLp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    public String getToAddr() {
        return toAddr;
    }

    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BigDecimal getValueToString() {
        return valueToString;
    }

    public void setValueToString(BigDecimal valueToString) {
        this.valueToString = valueToString;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public Integer getTokenType() {
        return tokenType;
    }

    public void setTokenType(Integer tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogIndex() {
        return logIndex;
    }

    public void setLogIndex(String logIndex) {
        this.logIndex = logIndex;
    }

    public String getOutOrIn() {
        return outOrIn;
    }

    public void setOutOrIn(String outOrIn) {
        this.outOrIn = outOrIn;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BGosZhiyaEntity{" +
        "id=" + id +
        ", txHash=" + txHash +
        ", number=" + number +
        ", fromAddr=" + fromAddr +
        ", toAddr=" + toAddr +
        ", value=" + value +
        ", valueToString=" + valueToString +
        ", tokenName=" + tokenName +
        ", tokenSymbol=" + tokenSymbol +
        ", tokenType=" + tokenType +
        ", decimals=" + decimals +
        ", timestamp=" + timestamp +
        ", address=" + address +
        ", logIndex=" + logIndex +
        ", outOrIn=" + outOrIn +
        "}";
    }
}
