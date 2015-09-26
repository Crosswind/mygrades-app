package de.mygrades.view.adapter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Semester item used in GradesRecyclerViewAdapter.
 */
public class SemesterItem implements GradesAdapterItem {
    private int semesterNumber;
    private String semester;
    private float average;
    private float creditPoints;
    private List<GradeItem> grades;

    public SemesterItem() {
        grades = new ArrayList<>();
    }

    public SemesterItem(int semesterNumber, String semester, float average, float creditPoints) {
        this.semesterNumber = semesterNumber;
        this.semester = semester;
        this.average = average;
        this.creditPoints = creditPoints;
        grades = new ArrayList<>();
    }

    public void addGrade(GradeItem gradeItem) {
        grades.add(gradeItem);

        // update creditPoints and average
        // TODO: consider credit points for average calculation!
        float average = 0f;
        float creditPoints = 0f;
        for(GradeItem grade : grades) {
            average += (grade.getGrade() == null ? 0f : grade.getGrade());
            creditPoints += (grade.getCreditPoints() == null ? 0f : grade.getCreditPoints());
        }
        average /= grades.size();

        this.average = average;
        this.creditPoints = creditPoints;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }

    public void setSemesterNumber(int semesterNumber) {
        this.semesterNumber = semesterNumber;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public float getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(float creditPoints) {
        this.creditPoints = creditPoints;
    }
}
