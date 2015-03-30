/**
 * Created by LiangTian on 3/29/15.
 */
public class PSeletion {

    private int type;
    private int lifeStyle;
    private int vacation;
    private int eCredit;
    private double salary;
    private double property;
    private int label;

    public PSeletion(String[] strs) {

        switch (strs[0]) {
            case "student":
                this.type = 1;
                break;
            case "engineer":
                this.type = 2;
                break;
            case "librarian":
                this.type = 3;
                break;
            case "professor":
                this.type = 4;
                break;
            case "doctor":
                this.type = 5;
                break;
        }

        switch (strs[1]) {
            case "spend<<saving" :
                this.lifeStyle = 1;
                break;
            case "spend<saving" :
                this.lifeStyle = 2;
                break;
            case "spend>saving" :
                this.lifeStyle = 3;
                break;
            case "spend>>saving" :
                this.lifeStyle = 4;
        }

        this.vacation = Integer.parseInt(strs[2]);
        this.eCredit = Integer.parseInt(strs[3]);
        this.salary = Double.parseDouble(strs[4]);
        this.property = Double.parseDouble(strs[5]);
        this.label = Integer.parseInt(strs[6].substring(1, 2));
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLifeStyle() {
        return lifeStyle;
    }

    public void setLifeStyle(int lifeStyle) {
        this.lifeStyle = lifeStyle;
    }

    public int getVacation() {
        return vacation;
    }

    public void setVacation(int vacation) {
        this.vacation = vacation;
    }

    public int geteCredit() {
        return eCredit;
    }

    public void seteCredit(int eCredit) {
        this.eCredit = eCredit;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getProperty() {
        return property;
    }

    public void setProperty(double property) {
        this.property = property;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
