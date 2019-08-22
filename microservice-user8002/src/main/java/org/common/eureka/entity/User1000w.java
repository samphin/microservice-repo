package org.common.eureka.entity;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

public class User1000w implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Integer id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    private String sex;

    private Integer score;

    private Integer copyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName == null ? null : firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName == null ? null : lastName.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getCopyId() {
        return copyId;
    }

    public void setCopyId(Integer copyId) {
        this.copyId = copyId;
    }
}