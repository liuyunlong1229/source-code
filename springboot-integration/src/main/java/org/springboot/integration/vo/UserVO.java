package org.springboot.integration.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by thinkpad on 2017/5/13.
 */

@ApiModel
public class UserVO implements Serializable{

    @ApiModelProperty(value = "编码")
    private Long uid;

    @ApiModelProperty(value = "名称")
    private String userName;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @ApiModelProperty(value = "年龄")
    private int age;

    @ApiModelProperty(value = "地址")
    private String address;


    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
