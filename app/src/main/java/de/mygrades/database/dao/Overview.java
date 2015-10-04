package de.mygrades.database.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "OVERVIEW".
 */
public class Overview {

    private Long id;
    private Double average;
    private Integer participants;
    private Integer section1;
    private Integer section2;
    private Integer section3;
    private Integer section4;
    private Integer section5;
    private Integer userSection;

    public Overview() {
    }

    public Overview(Long id) {
        this.id = id;
    }

    public Overview(Long id, Double average, Integer participants, Integer section1, Integer section2, Integer section3, Integer section4, Integer section5, Integer userSection) {
        this.id = id;
        this.average = average;
        this.participants = participants;
        this.section1 = section1;
        this.section2 = section2;
        this.section3 = section3;
        this.section4 = section4;
        this.section5 = section5;
        this.userSection = userSection;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Integer getParticipants() {
        return participants;
    }

    public void setParticipants(Integer participants) {
        this.participants = participants;
    }

    public Integer getSection1() {
        return section1;
    }

    public void setSection1(Integer section1) {
        this.section1 = section1;
    }

    public Integer getSection2() {
        return section2;
    }

    public void setSection2(Integer section2) {
        this.section2 = section2;
    }

    public Integer getSection3() {
        return section3;
    }

    public void setSection3(Integer section3) {
        this.section3 = section3;
    }

    public Integer getSection4() {
        return section4;
    }

    public void setSection4(Integer section4) {
        this.section4 = section4;
    }

    public Integer getSection5() {
        return section5;
    }

    public void setSection5(Integer section5) {
        this.section5 = section5;
    }

    public Integer getUserSection() {
        return userSection;
    }

    public void setUserSection(Integer userSection) {
        this.userSection = userSection;
    }


    // KEEP METHODS - put your custom methods here

    @Override
    public String toString() {
        return "Overview{" +
                "id=" + id +
                ", average=" + average +
                ", participants=" + participants +
                ", section1=" + section1 +
                ", section2=" + section2 +
                ", section3=" + section3 +
                ", section4=" + section4 +
                ", section5=" + section5 +
                ", userSection=" + userSection +
                '}';
    }

    // KEEP METHODS END
}
